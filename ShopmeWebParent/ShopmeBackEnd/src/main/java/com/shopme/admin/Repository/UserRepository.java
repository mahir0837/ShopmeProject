package com.shopme.admin.Repository;

import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends PagingAndSortingRepository<User,Integer> {
    User findByEmailIs(String email);
    Long countById(Integer id);
    @Query("UPDATE User u SET u.enabled=?2 where u.id=?1")
    @Modifying
    void updateEnabledStatus(Integer id,Boolean enabled);
//    @Query(value = "SELECT * FROM users WHERE users.first_name ILIKE %?1% OR users.last_name ILIKE %?1% " +
//            "OR users.email ILIKE %?1% ",nativeQuery = true)
    @Query("SELECT u FROM User u WHERE CONCAT(u.id,' ',u.email,' ',u.firstName,' '," +
            "u.lastName) LIKE %?1%")
    Page<User> findAll(String keyword, Pageable pageable);
}
