package com.osc.blog.business.concretes;

import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.dal.abstracts.ConfirmationTokenDao;
import com.osc.blog.entities.concretes.ConfirmationToken;
import com.osc.blog.entities.concretes.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenManagerTest {

    private ConfirmationTokenManager testManager;

    @Mock
    private ConfirmationTokenDao confirmationTokenDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testManager = new ConfirmationTokenManager(
                confirmationTokenDao
        );
    }

    @Test
    void itShould_Save() {

        ConfirmationToken confirmationToken = new ConfirmationToken(new User());

        Result expected = testManager.save(confirmationToken);

        assertThat(expected.isSuccess()).isTrue();

    }

    @Test
    void itShould_GetByToken_WhenConfirmationTokenExists() {

        ConfirmationToken confirmationToken = new ConfirmationToken(new User());

        given(confirmationTokenDao.findByToken(confirmationToken.getToken())).willReturn(confirmationToken);

        DataResult<ConfirmationToken> expected = testManager.getByToken(confirmationToken.getToken());

        assertThat(expected.isSuccess()).isTrue();
        assertThat(expected.getData()).isEqualTo(confirmationToken);

    }

    @Test
    void itShould_GetByToken_WhenConfirmationTokenDoesNotExists() {

        String token = "token";

        given(confirmationTokenDao.findByToken(token)).willReturn(null);

        DataResult<ConfirmationToken> expected = testManager.getByToken(token);

        assertThat(expected.isSuccess()).isFalse();
        assertThat(expected.getData()).isNull();

    }

    @Test
    void itShould_ConfirmToken_WhenConfirmationTokenExists() {

        ConfirmationToken confirmationToken = new ConfirmationToken(new User());

        given(confirmationTokenDao.findByToken(confirmationToken.getToken())).willReturn(confirmationToken);

        Result expected = testManager.confirmToken(confirmationToken.getToken());

        assertThat(expected.isSuccess()).isTrue();

    }

    @Test
    void itShouldNot_ConfirmToken_WhenConfirmationTokenDoesNotExists() {

        String token = "token";

        given(confirmationTokenDao.findByToken(token)).willReturn(null);

        Result expected = testManager.confirmToken(token);

        assertThat(expected.isSuccess()).isFalse();

    }

}