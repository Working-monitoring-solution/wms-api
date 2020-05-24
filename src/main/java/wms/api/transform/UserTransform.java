package wms.api.transform;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import wms.api.common.response.ManagerResponse;
import wms.api.common.response.UserInfoResponse;
import wms.api.dao.entity.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserTransform {
    public UserInfoResponse toUserResponse(User user) {
        ManagerResponse managerResponse = null;
        if (!ObjectUtils.isEmpty(user.getManager())) {
            managerResponse = ManagerResponse.builder()
                    .id(user.getManager().getId())
                    .name(user.getManager().getName())
                    .email(user.getManager().getEmail())
                    .build();
        }
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .active(user.isActive())
                .avatar(user.getAvatar())
                .createdDate(user.getCreatedDate())
                .roleAdmin(user.isRoleAdmin())
                .manager(managerResponse)
                .department(user.getDepartment())
                .position(user.getPosition())
                .build();
    }

    public Page<UserInfoResponse> toPageUserResponse(Page<User> userPage) {
        List<UserInfoResponse> listUserResponse = new ArrayList<>();
        List<User> userList = userPage.getContent();
        for (User user : userList) {
            listUserResponse.add(toUserResponse(user));
        }
        Page<UserInfoResponse> page = new PageImpl<>(listUserResponse, userPage.getPageable(), userPage.getTotalElements());
        return page;
    }
}
