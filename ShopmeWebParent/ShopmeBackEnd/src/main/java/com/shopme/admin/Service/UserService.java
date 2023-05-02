package com.shopme.admin.Service;

import com.shopme.admin.Exception.UserNotFountException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.hibernate.hql.spi.id.local.LocalTemporaryTableBulkIdStrategy;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    List<User>listAllUser();
    List<Role>listRoles();
    User save(User user);
    boolean isEmailUnique(String email);
    User getUserById(Integer id) throws UserNotFountException;
    void delete(Integer id) throws UserNotFountException;
    void updateUserEnabledStatus(Integer id,Boolean enabled);
    Page<User> listByPage(int pageNum, String sortField,String sortDir);
}
