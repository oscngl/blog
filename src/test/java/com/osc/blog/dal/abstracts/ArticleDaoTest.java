package com.osc.blog.dal.abstracts;

import com.osc.blog.entities.concretes.Article;
import com.osc.blog.entities.concretes.Topic;
import com.osc.blog.entities.concretes.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleDaoTest {

    @Autowired
    private ArticleDao testDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TopicDao topicDao;

    @AfterEach
    void tearDown() {
        testDao.deleteAll();
        userDao.deleteAll();
        topicDao.deleteAll();
    }

    @Test
    void itShould_FindAllByEnabledIsTrue_WhenEnabledIsTrueAndArticleExists() {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setConfirmed(false);
        userDao.save(user);

        Topic topic = new Topic();
        topic.setName("topic");
        topicDao.save(topic);

        Article article = new Article();
        article.setUser(user);
        article.setTopic(topic);
        article.setTitle("title");
        article.setText("text");
        article.setEnabled(true);
        testDao.save(article);

        List<Article> expected = testDao.findAllByEnabledIsTrue();

        assertThat(expected.isEmpty()).isFalse();

    }

    @Test
    void itShouldNot_FindAllByEnabledIsTrue_WhenEnabledIsFalseAndArticleExists() {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setConfirmed(false);
        userDao.save(user);

        Topic topic = new Topic();
        topic.setName("topic");
        topicDao.save(topic);

        Article article = new Article();
        article.setUser(user);
        article.setTopic(topic);
        article.setTitle("title");
        article.setText("text");
        article.setEnabled(false);
        testDao.save(article);

        List<Article> expected = testDao.findAllByEnabledIsTrue();

        assertThat(expected.isEmpty()).isTrue();

    }

    @Test
    void itShouldNot_FindAllByEnabledIsTrue_WhenArticleDoesNotExists() {

        List<Article> expected = testDao.findAllByEnabledIsTrue();

        assertThat(expected.isEmpty()).isTrue();

    }

    @Test
    void itShould_FindAllByEnabledIsTrueAndUserId_WhenEnabledIsTrueAndArticleWithUserIdExists() {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setConfirmed(false);
        userDao.save(user);

        Topic topic = new Topic();
        topic.setName("topic");
        topicDao.save(topic);

        Article article = new Article();
        article.setUser(user);
        article.setTopic(topic);
        article.setTitle("title");
        article.setText("text");
        article.setEnabled(true);
        testDao.save(article);

        int userId = userDao.findAll().get(0).getId();
        List<Article> expected = testDao.findAllByEnabledIsTrueAndUserId(userId);

        assertThat(expected.isEmpty()).isFalse();

    }

    @Test
    void itShouldNot_FindAllByEnabledIsTrueAndUserId_WhenEnabledIsFalseAndArticleWithUserIdExists() {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setConfirmed(false);
        userDao.save(user);

        Topic topic = new Topic();
        topic.setName("topic");
        topicDao.save(topic);

        Article article = new Article();
        article.setUser(user);
        article.setTopic(topic);
        article.setTitle("title");
        article.setText("text");
        article.setEnabled(false);
        testDao.save(article);

        int userId = userDao.findAll().get(0).getId();
        List<Article> expected = testDao.findAllByEnabledIsTrueAndUserId(userId);

        assertThat(expected.isEmpty()).isTrue();

    }

    @Test
    void itShouldNot_FindAllByEnabledIsTrueAndUserId_WhenArticleWithUserIdDoesNotExists() {

        List<Article> expected = testDao.findAllByEnabledIsTrueAndUserId(1);

        assertThat(expected.isEmpty()).isTrue();

    }

    @Test
    void itShould_FindAllByEnabledIsTrueAndTopicId_WhenEnabledIsTrueAndArticleWithTopicIdExists() {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setConfirmed(false);
        userDao.save(user);

        Topic topic = new Topic();
        topic.setName("topic");
        topicDao.save(topic);

        Article article = new Article();
        article.setUser(user);
        article.setTopic(topic);
        article.setTitle("title");
        article.setText("text");
        article.setEnabled(true);
        testDao.save(article);

        int topicId = topicDao.findAll().get(0).getId();
        List<Article> expected = testDao.findAllByEnabledIsTrueAndTopicId(topicId);

        assertThat(expected.isEmpty()).isFalse();

    }

    @Test
    void itShouldNot_FindAllByEnabledIsTrueAndTopicId_WhenEnabledIsFalseAndArticleWithTopicIdExists() {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setConfirmed(false);
        userDao.save(user);

        Topic topic = new Topic();
        topic.setName("topic");
        topicDao.save(topic);

        Article article = new Article();
        article.setUser(user);
        article.setTopic(topic);
        article.setTitle("title");
        article.setText("text");
        article.setEnabled(false);
        testDao.save(article);

        int topicId = topicDao.findAll().get(0).getId();
        List<Article> expected = testDao.findAllByEnabledIsTrueAndTopicId(topicId);

        assertThat(expected.isEmpty()).isTrue();

    }

    @Test
    void itShouldNot_FindAllByEnabledIsTrueAndTopicId_WhenArticleWithTopicIdDoesNotExists() {

        List<Article> expected = testDao.findAllByEnabledIsTrueAndTopicId(1);

        assertThat(expected.isEmpty()).isTrue();

    }

    @Test
    void itShould_SetEnabledFalse() {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setConfirmed(false);
        userDao.save(user);

        Topic topic = new Topic();
        topic.setName("topic");
        topicDao.save(topic);

        Article article = new Article();
        article.setUser(user);
        article.setTopic(topic);
        article.setTitle("title");
        article.setText("text");
        article.setEnabled(true);
        testDao.save(article);

        testDao.setEnabledFalse(testDao.findAll().get(0).getId());

        Article expected = testDao.findAll().get(0);

        assertThat(expected.isEnabled()).isFalse();

    }

}