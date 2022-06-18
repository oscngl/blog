package com.osc.blog.dal.abstracts;

import com.osc.blog.entities.concretes.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicDao extends JpaRepository<Topic, Integer> {

    Topic findByName(String name);

}
