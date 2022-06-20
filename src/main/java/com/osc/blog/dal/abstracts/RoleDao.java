package com.osc.blog.dal.abstracts;

import com.osc.blog.entities.concretes.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {

    Role findByName(String name);

}
