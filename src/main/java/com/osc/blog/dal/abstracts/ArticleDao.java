package com.osc.blog.dal.abstracts;

import com.osc.blog.entities.concretes.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDao extends JpaRepository<Article, Integer> {

    List<Article> findAllByEnabledIsTrue();
    List<Article> findAllByEnabledIsTrueAndUserId(int userId);
    List<Article> findAllByEnabledIsTrueAndTopicId(int topicId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Article a SET a.enabled = false WHERE a.id = :articleId")
    void setEnabledFalse(@Param(value = "articleId") int articleId);

}
