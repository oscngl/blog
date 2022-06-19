package com.osc.blog.dal.abstracts;

import com.osc.blog.entities.concretes.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenDao extends JpaRepository<ConfirmationToken, Integer> {

    ConfirmationToken findByToken(String token);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ConfirmationToken c SET c.confirmedDate = CURRENT_TIMESTAMP WHERE c.token = :token")
    void setConfirmedDate(@Param(value = "token") String token);

}
