package com.osc.blog.dal.abstracts;

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
class UserDaoTest {

    @Autowired
    private UserDao testDao;

    @AfterEach
    void tearDown() {
        testDao.deleteAll();
    }

    @Test
    void itShould_FindByConfirmedIsTrueAndEmail_WhenConfirmedIsTrueAndUserWithEmailExists() {

        String email = "email@email.com";
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(email);
        user.setPassword("password");
        user.setConfirmed(true);
        testDao.save(user);

        User expected = testDao.findByConfirmedIsTrueAndEmail(email);

        assertThat(expected).isEqualTo(user);

    }

    @Test
    void itShouldNot_FindByConfirmedIsTrueAndEmail_WhenConfirmedIsFalseAndUserWithEmailExists() {

        String email = "email@email.com";
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(email);
        user.setPassword("password");
        user.setConfirmed(false);
        testDao.save(user);

        User expected = testDao.findByConfirmedIsTrueAndEmail(email);

        assertThat(expected).isNull();

    }

    @Test
    void itShouldNot_FindByConfirmedIsTrueAndEmail_WhenUserWithEmailDoesNotExists() {

        String email = "email@email.com";

        User expected = testDao.findByConfirmedIsTrueAndEmail(email);

        assertThat(expected).isNull();

    }

    @Test
    void itShould_FindAllByConfirmedIsTrue_WhenConfirmedIsTrueAndUserExists() {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setConfirmed(true);
        testDao.save(user);

        List<User> expected = testDao.findAllByConfirmedIsTrue();

        assertThat(expected.isEmpty()).isFalse();

    }

    @Test
    void itShouldNot_FindAllByConfirmedIsTrue_WhenConfirmedIsFalseAndUserExists() {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setConfirmed(false);
        testDao.save(user);

        List<User> expected = testDao.findAllByConfirmedIsTrue();

        assertThat(expected.isEmpty()).isTrue();

    }

    @Test
    void itShouldNot_FindAllByConfirmedIsTrue_WhenUserDoesNotExists() {

        List<User> expected = testDao.findAllByConfirmedIsTrue();

        assertThat(expected.isEmpty()).isTrue();

    }

    @Test
    void itShould_SetConfirmedTrue() {

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user.setConfirmed(false);
        testDao.save(user);

        testDao.setConfirmedTrue(testDao.findAll().get(0).getId());

        User expected = testDao.findAll().get(0);

        assertThat(expected.isConfirmed()).isTrue();

    }

}