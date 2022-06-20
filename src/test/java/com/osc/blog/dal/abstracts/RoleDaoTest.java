package com.osc.blog.dal.abstracts;

import com.osc.blog.entities.concretes.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleDaoTest {

    @Autowired
    private RoleDao testDao;

    @AfterEach
    void tearDown() {
        testDao.deleteAll();
    }

    @Test
    void itShould_FindByName_WhenRoleWithNameExists() {

        String name = "name";
        Role role = new Role();
        role.setName(name);
        testDao.save(role);

        Role expected = testDao.findByName(name);

        assertThat(expected).isEqualTo(role);

    }

    @Test
    void itShouldNot_FindByName_WhenRoleWithNameDoesNotExists() {

        String name = "name";

        Role expected = testDao.findByName(name);

        assertThat(expected).isNull();

    }

}