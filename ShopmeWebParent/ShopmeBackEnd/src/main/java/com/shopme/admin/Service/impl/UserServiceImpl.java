package com.shopme.admin.Service.impl;

import com.shopme.admin.Exception.UserNotFountException;
import com.shopme.admin.Repository.RoleRepository;
import com.shopme.admin.Repository.UserRepository;
import com.shopme.admin.Service.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public static final int USERS_PER_PAGE=4;

    @Override
    public List<User> listAllUser() {
        List<User> listAll= (List<User>) userRepository.findAll();
        return listAll;
    }

    @Override
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    @Override
    public User save(User user) {
        boolean isUpdatingUser=(user.getId()!=null);
        if (isUpdatingUser) {
            User existingUser=userRepository.findById(user.getId()).get();
            if (user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }else{
                encodePassword(user);
            }
        }else{
            encodePassword(user);
        }
        return userRepository.save(user);

    }

    @Override
    public boolean isEmailUnique(String email) {
        User userByEmail= userRepository.findByEmailIs(email);
        return userByEmail==null;
    }

    @Override
    public User getUserById(Integer id) throws UserNotFountException {
        try {
           return userRepository.findById(id).get();
        }catch (NoSuchElementException e){
            throw new UserNotFountException("Could not find any user ID"+id);
        }

    }

    @Override
    public void delete(Integer id) throws UserNotFountException {
        Long countById=userRepository.countById(id);
        if (countById==null||countById==0){
           throw new UserNotFountException("Could not find any user ID"+id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public void updateUserEnabledStatus(Integer id, Boolean enabled) {
        userRepository.updateEnabledStatus(id,enabled);
    }

    @Override
    public Page<User> listByPage(int pageNum) {
        Pageable pageable= PageRequest.of(pageNum-1,USERS_PER_PAGE);
        return userRepository.findAll(pageable);
    }

    private void encodePassword(User user){
        String encodePassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
    }
}
