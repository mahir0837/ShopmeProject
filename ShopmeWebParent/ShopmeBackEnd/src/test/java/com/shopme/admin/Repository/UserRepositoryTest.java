package com.shopme.admin.Repository;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole(){

        Role roleAdmin=entityManager.find(Role.class,1);
        User userMahir=new User("mahir@veratechsupport.com","Abc123","Mahir","Sarac");
        userMahir.addRole(roleAdmin);
        repo.save(userMahir);

        User savedUser=repo.save(userMahir);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateNewUserWithTwoRoles(){

        User userNihal=new User("nihal@veratechsupport.com","Abc123","Nihal","Sarac");
        Role roleEditor=new Role(3);
        Role roleAsistant=new Role(5);
        userNihal.addRole(roleEditor);
        userNihal.addRole(roleAsistant);
        User savedUser=repo.save(userNihal);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }
    @Test
    public void testLisAllUser(){
        Iterable<User>listUsers=repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }
    @Test
    public void testGetUserById(){
       User userName= repo.findById(1).get();
       System.out.println(userName);
       assertThat(userName).isNotNull();
    }
    @Test
    public void testUpdateUSerDetails(){
        User userName= repo.findById(1).get();
        userName.setEnabled(true);
        userName.setEmail("mahirsarac89@gmail.com");
        repo.save(userName);
    }
    @Test
    public void testUpdateUserRoles(){
        User userNihal= repo.findById(2).get();
        Role roleEditor=new Role(3);
        Role roleSalesPerson=new Role(2);
        userNihal.getRoles().remove(roleEditor);
        userNihal.addRole(roleSalesPerson);
        repo.save(userNihal);
    }
    @Test
    public void testDeleteUser(){
        Integer userId=1;
        repo.deleteById(userId);
        repo.findById(userId);


    }
    @Test
    public void testGetUserByEmail(){
        String email="nihal@veratechsupport.com";
        User user=repo.findByEmailIs(email);
        assertThat(user).isNotNull();
    }
    @Test
    public void testCountById(){
        Integer id=2;
        Long countById=repo.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);

    }
    @Test
    public void testDisabledUser(){
        Integer id=2;
        repo.updateEnabledStatus(id,false);
    }
    @Test
    public void testEnabledUser(){
        Integer id=2;
        repo.updateEnabledStatus(id,true);
    }
}
