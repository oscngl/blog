package com.osc.blog.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osc.blog.dal.abstracts.RoleDao;
import com.osc.blog.entities.concretes.Role;
import com.osc.blog.entities.dtos.RoleDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RolesControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleDao roleDao;

    @AfterEach
    void tearDown() {
        roleDao.deleteAll();
    }

    @Test
    void itShould_Save_WhenRequestIsValid_IsOk() throws Exception {

        RoleDto roleDto = new RoleDto();
        roleDto.setName("role");

        mockMvc.perform(post("/api/v1/roles/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShouldNot_Save_WhenRequestIsNotValid_IsBadRequest() throws Exception {

        mockMvc.perform(post("/api/v1/roles/save")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void itShould_GetById_WhenIdExists_IsOk() throws Exception {

        String name = "name";
        Role role = new Role();
        role.setName(name);
        roleDao.save(role);

        int id = roleDao.findByName(name).getId();

        mockMvc.perform(get("/api/v1/roles/getById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShouldNot_GetById_WhenIdDoesNotExists_IsOk() throws Exception {

        mockMvc.perform(get("/api/v1/roles/getById")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));

    }

    @Test
    void itShould_GetAll_IsOk() throws Exception {

        mockMvc.perform(get("/api/v1/roles/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShould_GetByName_WhenEmailExists_IsOk() throws Exception {

        String name = "name";
        Role role = new Role();
        role.setName(name);
        roleDao.save(role);

        mockMvc.perform(get("/api/v1/roles/getByName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void itShouldNot_GetByName_WhenNameDoesNotExists_IsOk() throws Exception {

        mockMvc.perform(get("/api/v1/roles/getByName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));

    }

}