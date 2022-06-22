package com.osc.blog.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osc.blog.dal.abstracts.UserDao;
import com.osc.blog.entities.concretes.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDao userDao;

    @AfterEach
    void tearDown() {
        userDao.deleteAll();
    }

    @Test
    void itShould_GetById_WhenIdExists_IsOk() throws Exception {

        String email = "email@email.com";
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(email);
        user.setPassword("password");
        user.setConfirmed(true);
        userDao.save(user);

        int id = userDao.findByConfirmedIsTrueAndEmail(email).getId();

        mockMvc.perform(get("/api/v1/users/getById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShouldNot_GetById_WhenIdDoesNotExists_IsOk() throws Exception {

        mockMvc.perform(get("/api/v1/users/getById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));

    }

    @Test
    void itShould_GetAll_IsOk() throws Exception {

        mockMvc.perform(get("/api/v1/users/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShould_GetByEmail_WhenEmailExists() throws Exception {

        String email = "email@email.com";
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(email);
        user.setPassword("password");
        user.setConfirmed(true);
        userDao.save(user);

        mockMvc.perform(get("/api/v1/users/getByEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShouldNot_GetByEmail_WhenEmailDoesNotExists() throws Exception {

        mockMvc.perform(get("/api/v1/users/getByEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", "email@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));

    }

    @Test
    void itShould_SetPhotoUrl_WhenIdExistsAndPhotoExists_IsOk() throws Exception {

        FileInputStream fileInputStream = new FileInputStream("/home/osc/Pictures/Wallpapers/wallpaper.jpeg");
        MockMultipartFile sampleImage = new MockMultipartFile(
                "photo",
                "wallpaper.jpeg",
                "multipart/form-data",
                fileInputStream
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/v1/users/setPhotoUrl")
                        .file(sampleImage)
                        .param("id", objectMapper.writeValueAsString(1)))
                .andExpect(status().isOk());

    }

    @Test
    void itShould_SetPhotoUrl_WhenIdDoesNotExistsAndPhotoExists_IsBadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/v1/users/setPhotoUrl"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void itShould_SetPhotoUrl_WhenIdExistsAndPhotoDoesNotExists_IsBadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/v1/users/setPhotoUrl")
                        .param("id", objectMapper.writeValueAsString(1)))
                .andExpect(status().isBadRequest());

    }

}
