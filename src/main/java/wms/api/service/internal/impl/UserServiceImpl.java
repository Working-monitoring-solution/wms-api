package wms.api.service.internal.impl;

import wms.api.dao.entity.User;
import wms.api.dao.repo.UserRepository;
import wms.api.service.impl.BaseServiceImpl;
import wms.api.service.internal.UserService;

public class UserServiceImpl extends BaseServiceImpl<UserRepository, User, Long> implements UserService {
}
