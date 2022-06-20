package com.osc.blog.business.concretes;

import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.dal.abstracts.RoleDao;
import com.osc.blog.entities.concretes.Role;
import com.osc.blog.entities.dtos.RoleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleManagerTest {

    private RoleManager testManager;

    @Mock
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testManager = new RoleManager(
                roleDao,
                new ModelMapper()
        );
    }

    @Test
    void itShould_Save_WhenRoleWithNameDoesNotExists() {

        String name = "name";
        RoleDto roleDto = new RoleDto();
        roleDto.setName(name);

        given(roleDao.findByName(name)).willReturn(null);

        Result expected = testManager.save(roleDto);

        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleDao).save(roleArgumentCaptor.capture());
        Role capturedRole = roleArgumentCaptor.getValue();

        assertThat(expected.isSuccess()).isTrue();
        assertThat(capturedRole.getName()).isEqualTo(roleDto.getName());

    }

    @Test
    void itShouldNot_Save_WhenRoleWithNameExists() {

        String name = "name";
        RoleDto roleDto = new RoleDto();
        roleDto.setName(name);

        given(roleDao.findByName(name)).willReturn(new Role());

        Result expected = testManager.save(roleDto);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShould_GetById_WhenRoleWithIdExists() {

        int id = 1;
        Role role = new Role();
        role.setId(id);

        given(roleDao.findById(id)).willReturn(Optional.of(role));

        DataResult<Role> expected = testManager.getById(id);

        assertThat(expected.isSuccess()).isTrue();
        assertThat(expected.getData()).isEqualTo(role);

    }

    @Test
    void itShouldNot_GetById_WhenRoleWithIdDoesNotExists() {

        int id = 1;

        given(roleDao.findById(id)).willReturn(Optional.empty());

        DataResult<Role> expected = testManager.getById(id);

        assertThat(expected.isSuccess()).isFalse();
        assertThat(expected.getData()).isNull();

    }

    @Test
    void getAll() {

        testManager.getAll();

        verify(roleDao).findAll();

    }

    @Test
    void itShould_GetByName_WhenRoleWithNameExists() {

        String name = "name";
        Role role = new Role();
        role.setName(name);

        given(roleDao.findByName(name)).willReturn(role);

        DataResult<Role> expected = testManager.getByName(name);

        assertThat(expected.isSuccess()).isTrue();
        assertThat(expected.getData()).isEqualTo(role);

    }

    @Test
    void itShouldNot_GetByName_WhenRoleWithNameDoesNotExists() {

        String name = "name";

        given(roleDao.findByName(name)).willReturn(null);

        DataResult<Role> expected = testManager.getByName(name);

        assertThat(expected.isSuccess()).isFalse();
        assertThat(expected.getData()).isNull();

    }

}