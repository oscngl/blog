package com.osc.blog.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osc.blog.dal.abstracts.ConfirmationTokenDao;
import com.osc.blog.dal.abstracts.RoleDao;
import com.osc.blog.dal.abstracts.UserDao;
import com.osc.blog.entities.concretes.ConfirmationToken;
import com.osc.blog.entities.concretes.Role;
import com.osc.blog.entities.concretes.User;
import com.osc.blog.entities.dtos.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private ConfirmationTokenDao confirmationTokenDao;

    @AfterEach
    void tearDown() {
        confirmationTokenDao.deleteAll();
        userDao.deleteAll();
        roleDao.deleteAll();
    }

    @Test
    @Transactional
    void itShould_Register_WhenRequestIsValid_IsOk() throws Exception {

        String name = "name";
        Role role = new Role();
        role.setName(name);
        roleDao.save(role);

        UserDto userDto = new UserDto();
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setEmail("email@email.com");
        userDto.setPassword("password");
        userDto.setRoleName(name);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    @Transactional
    void itShouldNot_Register_WhenRoleNameDoesNotExists_IsBadRequest() throws Exception {

        UserDto userDto = new UserDto();
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setEmail("email@email.com");
        userDto.setPassword("password");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void itShould_ConfirmUser_WhenRequestIsValid_IsOk() throws Exception {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        userDao.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenDao.save(confirmationToken);

        String token = confirmationToken.getToken();

        mockMvc.perform(post("/api/v1/auth/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShould_ConfirmUser_WhenRequestIsNotValid_IsBadRequest() throws Exception {

        mockMvc.perform(post("/api/v1/auth/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

}