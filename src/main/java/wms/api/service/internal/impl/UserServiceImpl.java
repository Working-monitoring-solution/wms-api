package wms.api.service.internal.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import wms.api.common.request.AdminLoginRequest;
import wms.api.common.response.ManagerResponse;
import wms.api.constant.WMSConstant;
import wms.api.dao.entity.Department;
import wms.api.dao.entity.Position;
import wms.api.dao.repo.DepartmentRepository;
import wms.api.dao.repo.PositionRepository;
import wms.api.exception.WMSException;
import wms.api.service.internal.FirebaseService;
import wms.api.util.Constant;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.dao.entity.User;
import wms.api.dao.repo.UserRepository;
import wms.api.service.impl.BaseServiceImpl;
import wms.api.service.internal.UserService;
import wms.api.util.Utils;
import wms.api.util.InputValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserRepository, User, Long> implements UserService {

    @Value("${spring.jwt.expired}")
    private String expired;

    @Autowired
    FirebaseService firebaseService;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    PositionRepository positionRepository;

    @Override
    public String login(UserLoginRequest request) {
        User user = handleLogin(request.getEmail(), request.getPassword());
        String token = generateToken(user);
        repo.save(user);
        return token;
    }

    @Override
    public String loginAdmin(AdminLoginRequest request) {
        User user = handleLogin(request.getUsername(), request.getPassword());
        if (!user.isRoleAdmin()) {
            throw new WMSException.AuthorizationErrorException();
        }
        String token = generateToken(user);
        repo.save(user);
        return token;
    }

    @Override
    public String logout(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        User user = tokenService.validateUserToken(token);
        user.setToken("");
        repo.save(user);
        return "success";
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest, HttpServletRequest request) {
        checkHighestRole(adminValidateToken(request));
        if (repo.existsByEmail(createUserRequest.getEmail())) {
            throw new WMSException.EmailExistException();
        }
        User manager = repo.findByIdAndRoleAdminIsTrue(Utils.toLong(createUserRequest.getManagerId()));
        if (ObjectUtils.isEmpty(manager)) {
            throw new WMSException.NotFoundEntityException("manager");
        }
        Department department = departmentRepository.findById(Utils.toLong(createUserRequest.getDepartmentId())).orElseThrow(
                () -> new WMSException.NotFoundEntityException("department")
        );
        Position position = positionRepository.findById(Utils.toLong(createUserRequest.getPositionId())).orElseThrow(
                () -> new WMSException.NotFoundEntityException("position")
        );
        User user = User.builder()
                .email(createUserRequest.getEmail())
                .name(createUserRequest.getName())
                .password(hashPassword(createUserRequest.getPassword()))
                .avatar(WMSConstant.DEFAULT_AVATAR_URL)
                .createdDate(new Date())
                .active(true)
                .roleAdmin(createUserRequest.isRoleAdmin())
                .manager(manager)
                .department(department)
                .position(position)
                .build();
        user = repo.save(user);
        generateToken(user);
        return user;
    }

    @Override
    public User changeActiveStatus(String id, HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        tokenService.validateAdminToken(token);
        User user = repo.findById(Utils.toLong(id)).orElseThrow(
                () -> new WMSException.NotFoundEntityException("user")
        );
        user.setActive(!user.isActive());
        repo.save(user);
        return user;
    }

    @Override
    public User changeRoleAdminStatus(String id, HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        tokenService.validateAdminToken(token);
        User user = repo.findById(Utils.toLong(id)).orElseThrow(
                () -> new WMSException.NotFoundEntityException("user")
        );
        user.setRoleAdmin(!user.isRoleAdmin());
        repo.save(user);
        return user;
    }

    @Override
    public User changeUserInfo(String userId, String managerId, boolean status, String departmentId,
                               String positionId, HttpServletRequest request) {
        checkHighestRole(adminValidateToken(request));
        User user = repo.findById(Utils.toLong(userId)).orElseThrow(
                () -> new WMSException.NotFoundEntityException("user")
        );
        User manager = repo.findById(Utils.toLong(managerId)).orElseThrow(
                () -> new WMSException.NotFoundEntityException("manager")
        );
        Department department = departmentRepository.findById(Utils.toLong(departmentId)).orElseThrow(
                () -> new WMSException.NotFoundEntityException("department")
        );
        Position position = positionRepository.findById(Utils.toLong(positionId)).orElseThrow(
                () -> new WMSException.NotFoundEntityException("position")
        );
        user.setManager(manager);
        user.setActive(status);
        user.setPosition(position);
        user.setDepartment(department);
        repo.save(user);
        return user;
    }

    @Override
    public List<ManagerResponse> getAllManager(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        User user = tokenService.validateAdminToken(token);
        List<ManagerResponse> listManager = new ArrayList<>();
        if (checkHighestRole(user)) {
            List<User> managers = repo.findAllByActiveIsTrueAndRoleAdminIsTrue();
            managers.forEach(manager -> {
                ManagerResponse managerResponse = ManagerResponse.builder()
                        .email(manager.getEmail())
                        .name(manager.getName())
                        .id(manager.getId())
                        .build();
                listManager.add(managerResponse);
            });
        } else {
            ManagerResponse managerResponse = ManagerResponse.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .id(user.getId())
                    .build();
            listManager.add(managerResponse);
        }
        return listManager;
    }

    @Override
    public User changePassword(String password, String currentPassword, HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        User user = tokenService.validateUserToken(token);
        if (!authenticate(user.getPassword(), currentPassword)) {
            throw new WMSException.InvalidInputException("Password");
        }
        user.setPassword(hashPassword(password));
        return repo.save(user);
    }

    @Override
    public User changeAvatar(MultipartFile file, HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        User user = tokenService.validateUserToken(token);
        MultipartFile avatar = file;
        if (avatar.getSize() > WMSConstant.MAX_SIZE_AVATAR) {
            throw new WMSException.InvalidInputException("avatar");
        }
        if (!avatar.isEmpty()) {
            String url = firebaseService.saveImage(avatar, user.getId().toString());
            user.setAvatar(url);
        }
        return repo.save(user);
    }

    @Override
    public User getUserById(String id, HttpServletRequest request) {
        User manager = adminValidateToken(request);
        User user = repo.findById(Utils.toLong(id)).orElseThrow(
                () -> new WMSException.NotFoundEntityException("user")
        );
        if (!ObjectUtils.isEmpty(manager.getManager()) && !user.getManager().equals(manager) && !ObjectUtils.isEmpty(manager.getManager())) {
            throw new WMSException.AuthorizationErrorException();
        }
        return user;
    }

    @Override
    public User adminValidateToken(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        return tokenService.validateAdminToken(token);

    }

    @Override
    public User getUserInfo(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        return tokenService.validateUserToken(token);
    }

    @Override
    public Page<User> searchUser(String name, String email, String position, String department, int page, HttpServletRequest request) {
        User manager = adminValidateToken(request);
        if (checkHighestRole(manager)) {
            manager = null;
        }
        Pageable pageable = PageRequest.of(page, WMSConstant.PAGE_SIZE_DEFAULT, Sort.by("id").ascending());
        Page<User> users = repo.searchUser(name, email, manager, pageable);
        return users;
    }

    @Override
    public List<Department> getDepartments(HttpServletRequest request) {
        checkHighestRole(adminValidateToken(request));
        return departmentRepository.findAll();
    }

    @Override
    public List<Position> getPosition(String departmentId, HttpServletRequest request) {
        checkHighestRole(adminValidateToken(request));
        Department department = departmentRepository.findById(Utils.toLong(departmentId)).orElseThrow(
                () -> new WMSException.NotFoundEntityException("department")
        );
        return positionRepository.getByDepartment(department);
    }

    private boolean checkHighestRole(User manager) {
        return ObjectUtils.isEmpty(manager.getManager());
    }

    private User handleLogin(String email, String password) {
        if (!InputValidator.validateEmail(email)) {
            throw new WMSException.InvalidInputException("email");
        }
        User user = repo.findByEmail(email);
        if (ObjectUtils.isEmpty(user)) {
            throw new WMSException.NotFoundEntityException();
        }
        if (!authenticate(user.getPassword(), password)) {
            throw new WMSException.AuthenticationErrorException();
        }
        if (!user.isActive()) {
            throw new WMSException.NotActiveEntityException("user");
        }
        return user;
    }

    private String generateToken(User user) {
        Date now = new Date();
        Long expiredIn = Long.valueOf(expired);
        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .setExpiration(new Date(now.getTime() + expiredIn))
                .compact();
        user.setToken(token);
        return token;
    }

    private boolean authenticate(String hashed, String password) {
        return BCrypt.checkpw(password, hashed);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(Constant.SALT_ROUND));
    }
}
