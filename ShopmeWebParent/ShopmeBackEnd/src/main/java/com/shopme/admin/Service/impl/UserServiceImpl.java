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
import org.springframework.data.domain.Sort;
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
        List<User> listAll= (List<User>) userRepository.findAll(Sort.by("id").ascending());
        return listAll;
    }

    @Override
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    @Override
    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }

        } else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    @Override
    public boolean isEmailUnique(Integer id,String email) {
        User userByEmail= userRepository.findByEmailIs(email);
        if (userByEmail==null)return true;
        boolean isCreatingNew=(id==null);
        if (isCreatingNew){
            if (userByEmail!=null)return false;
            else{
                if (userByEmail.getId()!=id){
                    return false;
                }
            }
        }
        return true;
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
    public Page<User> listByPage(int pageNum,String sortField,String sortDir,String keyword) {

        Sort sort=Sort.by(sortField);

        sort=sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable= PageRequest.of(pageNum-1,USERS_PER_PAGE,sort);

        if (keyword!=null){
            return userRepository.findAll(keyword,pageable);
        }
        return userRepository.findAll(pageable);
    }

    private void encodePassword(User user){
        String encodePassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
    }
}
