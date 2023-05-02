package com.shopme.admin.Repository;

import com.shopme.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User,Integer> {
    User findByEmailIs(String email);
    Long countById(Integer id);
    @Query("UPDATE User u SET u.enabled=?2 where u.id=?1")
    @Modifying
    void updateEnabledStatus(Integer id,Boolean enabled);
}
