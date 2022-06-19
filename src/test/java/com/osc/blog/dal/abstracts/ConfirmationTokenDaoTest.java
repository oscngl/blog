package com.osc.blog.dal.abstracts;

import com.osc.blog.entities.concretes.ConfirmationToken;
import com.osc.blog.entities.concretes.User;
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
class ConfirmationTokenDaoTest {

    @Autowired
    private ConfirmationTokenDao testDao;

    @Autowired
    private UserDao userDao;

    @AfterEach
    void tearDown() {
        testDao.deleteAll();
        userDao.deleteAll();
    }

    @Test
    void itShould_FindByToken_WhenConfirmationTokenWithTokenExists() {

        String email = "email@email.com";
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(email);
        user.setPassword("password");
        user.setConfirmed(true);
        userDao.save(user);
        user = userDao.findByConfirmedIsTrueAndEmail(email);

        String token = "token";
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setUser(user);
        testDao.save(confirmationToken);

        ConfirmationToken expected = testDao.findByToken(token);

        assertThat(expected).isEqualTo(confirmationToken);

    }

    @Test
    void itShouldNot_FindByToken_WhenConfirmationTokenWithTokenDoesNotExists() {

        String token = "token";

        ConfirmationToken expected = testDao.findByToken(token);

        assertThat(expected).isNull();

    }

    @Test
    void itShould_SetConfirmedDate() {

        String email = "email@email.com";
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(email);
        user.setPassword("password");
        user.setConfirmed(true);
        userDao.save(user);
        user = userDao.findByConfirmedIsTrueAndEmail(email);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(user);
        confirmationToken.setConfirmedDate(null);
        testDao.save(confirmationToken);

        testDao.setConfirmedDate(confirmationToken.getToken());
        ConfirmationToken expected = testDao.findByToken(confirmationToken.getToken());

        assertThat(expected.getConfirmedDate()).isNotNull();

    }

}