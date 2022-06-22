package com.osc.blog.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osc.blog.dal.abstracts.ArticleDao;
import com.osc.blog.dal.abstracts.TopicDao;
import com.osc.blog.dal.abstracts.UserDao;
import com.osc.blog.entities.concretes.Article;
import com.osc.blog.entities.concretes.Topic;
import com.osc.blog.entities.concretes.User;
import com.osc.blog.entities.dtos.ArticleDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticlesControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TopicDao topicDao;

    @AfterEach
    void tearDown() {
        articleDao.deleteAll();
        userDao.deleteAll();
        topicDao.deleteAll();
    }

    @Test
    @Transactional
    void itShould_Save_WhenRequestIsValid_IsOk() throws Exception {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        userDao.save(user);

        Topic topic = new Topic();
        topic.setName("name");
        topicDao.save(topic);

        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("title");
        articleDto.setText("text");
        articleDto.setUser(user);
        articleDto.setTopic(topic);

        mockMvc.perform(post("/api/v1/articles/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShouldNot_Save_WhenRequestIsNotValid_IsBadRequest() throws Exception {

        mockMvc.perform(post("/api/v1/articles/save")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    void itShould_GetById_WhenIdExists_IsOk() throws Exception {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        userDao.save(user);

        Topic topic = new Topic();
        topic.setName("name");
        topicDao.save(topic);

        Article article = new Article();
        article.setTitle("title");
        article.setText("text");
        article.setUser(user);
        article.setTopic(topic);
        articleDao.save(article);

        int id = articleDao.findAll().get(0).getId();

        mockMvc.perform(get("/api/v1/articles/getById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShouldNot_GetById_WhenIdDoesNotExists_IsOk() throws Exception {

        mockMvc.perform(get("/api/v1/articles/getById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));

    }

    @Test
    void itShould_GetAll_IsOk() throws Exception {

        mockMvc.perform(get("/api/v1/articles/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShould_GetAllByUserId_WhenUserIdExists_IsOk() throws Exception {

        String email = "email@email.com";
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(email);
        user.setPassword("password");
        user.setConfirmed(true);
        userDao.save(user);

        int userId = userDao.findByConfirmedIsTrueAndEmail(email).getId();

        mockMvc.perform(get("/api/v1/articles/getAllByUserId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShould_GetAllByTopicId_WhenTopicIdExists_IsOk() throws Exception {

        String name = "name";
        Topic topic = new Topic();
        topic.setName(name);
        topicDao.save(topic);

        int topicId = topicDao.findByName(name).getId();

        mockMvc.perform(get("/api/v1/articles/getAllByTopicId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("topicId", String.valueOf(topicId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    @Transactional
    void itShould_SetEnabledFalse_WhenArticleExists_IsOk() throws Exception {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        userDao.save(user);

        Topic topic = new Topic();
        topic.setName("name");
        topicDao.save(topic);

        Article article = new Article();
        article.setTitle("title");
        article.setText("text");
        article.setUser(user);
        article.setTopic(topic);
        articleDao.save(article);

        int id = articleDao.findAll().get(0).getId();

        mockMvc.perform(put("/api/v1/articles/setEnabledFalse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShouldNot_SetEnabledFalse_WhenArticleDoeNotExists_IsOk() throws Exception {

        mockMvc.perform(put("/api/v1/articles/setEnabledFalse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));

    }

}