package com.osc.blog.dal.abstracts;

import com.osc.blog.entities.concretes.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDao extends JpaRepository<Article, Integer> {

    List<Article> findAllByEnabledIsTrueOrderByCreatedDateDesc(Pageable pageable);
    List<Article> findAllByEnabledIsTrueAndUserIdOrderByCreatedDateDesc(int userId, Pageable pageable);
    List<Article> findAllByEnabledIsTrueAndUser_UsrnameOrderByCreatedDateDesc(String username, Pageable pageable);
    List<Article> findAllByEnabledIsTrueAndTopicIdOrderByCreatedDateDesc(int topicId, Pageable pageable);
    List<Article> findAllByEnabledIsTrueAndTitleContainingIgnoreCaseOrderByCreatedDateDesc(String keywords, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.enabled= true")
    int countAllByEnabledIsTrue();

    @Query("SELECT COUNT(a) FROM Article a WHERE a.enabled= true AND a.user.id = :userId")
    int countAllByEnabledIsTrueAndUserId(@Param(value = "userId") int userId);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.enabled= true AND a.user.usrname = :username")
    int countAllByEnabledIsTrueAndUser_Usrname(@Param(value = "username") String username);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.enabled= true AND a.topic.id = :topicId")
    int countAllByEnabledIsTrueAndTopicId(@Param(value = "topicId") int topicId);

    @Query("SELECT COUNT(a) FROM Article a WHERE lower(a.title) LIKE lower(concat('%', :keywords, '%'))")
    int countAllByEnabledIsTrueAndKeywords(String keywords);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Article a SET a.enabled = false WHERE a.id = :articleId")
    void setEnabledFalse(@Param(value = "articleId") int articleId);

}
