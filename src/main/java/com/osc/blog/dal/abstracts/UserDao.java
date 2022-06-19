package com.osc.blog.dal.abstracts;

import com.osc.blog.entities.concretes.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    User findByConfirmedIsTrueAndEmail(String email);
    List<User> findAllByConfirmedIsTrue();

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.confirmed = true WHERE u.id = :userId")
    void setConfirmedTrue(@Param(value = "userId") int userId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.photoUrl = :photoUrl WHERE u.id = :userId")
    void setPhotoUrl(@Param(value = "userId") int userId, @Param(value = "photoUrl") String photoUrl);

}
