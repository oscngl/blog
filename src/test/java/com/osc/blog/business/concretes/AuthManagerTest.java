package com.osc.blog.business.concretes;

import com.osc.blog.business.abstracts.ConfirmationTokenService;
import com.osc.blog.business.abstracts.RoleService;
import com.osc.blog.business.abstracts.UserService;
import com.osc.blog.core.adapters.abstracts.EmailSenderService;
import com.osc.blog.core.utilities.results.ErrorDataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.core.utilities.results.SuccessDataResult;
import com.osc.blog.entities.concretes.ConfirmationToken;
import com.osc.blog.entities.concretes.Role;
import com.osc.blog.entities.concretes.User;
import com.osc.blog.entities.dtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthManagerTest {

    private AuthManager testManager;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testManager = new AuthManager(
                userService,
                roleService,
                emailSenderService,
                confirmationTokenService
        );
    }

    @Test
    void itShould_Register_WhenUserWithEmailDoesNotExistsAndRoleExists() {

        String roleName = "roleName";
        UserDto userDto = new UserDto();
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setEmail("email@email.com");
        userDto.setPassword("password");
        userDto.setRoleName(roleName);

        given(roleService.getByName(roleName)).willReturn(new SuccessDataResult<>(new Role()));
        given(userService.save(userDto, new Role())).willReturn(new SuccessDataResult<>(new User()));

        Result expected = testManager.register(userDto);

        assertThat(expected.isSuccess()).isTrue();

    }

    @Test
    void itShouldNot_Register_WhenUserWithEmailDoesNotExistsAndRoleDoesNotExists() {

        String roleName = "roleName";
        UserDto userDto = new UserDto();
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setEmail("email@email.com");
        userDto.setPassword("password");
        userDto.setRoleName(roleName);

        given(roleService.getByName(roleName)).willReturn(new ErrorDataResult<>(null));

        Result expected = testManager.register(userDto);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShouldNot_Register_WhenUserWithEmailExists() {

        String roleName = "roleName";
        UserDto userDto = new UserDto();
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setEmail("email@email.com");
        userDto.setPassword("password");
        userDto.setRoleName(roleName);

        given(roleService.getByName(roleName)).willReturn(new SuccessDataResult<>(new Role()));
        given(userService.save(userDto, new Role())).willReturn(new ErrorDataResult<>(null));

        Result expected = testManager.register(userDto);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShould_ConfirmUser_WhenTokenExistsAndUserExistsAndUserNotConfirmedAndTokenNotConfirmedAndTokenNotExpired() {

        String token = "token";
        String email = "email@email.com";

        User user = new User();
        user.setEmail(email);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setUser(user);

        given(confirmationTokenService.getByToken(token)).willReturn(new SuccessDataResult<>(confirmationToken));
        given(userService.getByEmail(email)).willReturn(new SuccessDataResult<>(user));

        Result expected = testManager.confirmUser(token);

        assertThat(expected.isSuccess()).isTrue();

    }

    @Test
    void itShouldNot_ConfirmUser_WhenTokenDoesNotExists() {

        String token = "token";

        given(confirmationTokenService.getByToken(token)).willReturn(new ErrorDataResult<>(null));

        Result expected = testManager.confirmUser(token);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShouldNot_ConfirmUser_WhenUserDoesNotExists() {

        String token = "token";
        String email = "email@email.com";

        User user = new User();
        user.setEmail(email);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setUser(user);

        given(confirmationTokenService.getByToken(token)).willReturn(new SuccessDataResult<>(confirmationToken));
        given(userService.getByEmail(email)).willReturn(new ErrorDataResult<>(null));

        Result expected = testManager.confirmUser(token);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShouldNot_ConfirmUser_WhenUserConfirmed() {

        String token = "token";
        String email = "email@email.com";

        User user = new User();
        user.setEmail(email);
        user.setConfirmed(true);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setUser(user);

        given(confirmationTokenService.getByToken(token)).willReturn(new SuccessDataResult<>(confirmationToken));
        given(userService.getByEmail(email)).willReturn(new SuccessDataResult<>(user));

        Result expected = testManager.confirmUser(token);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShouldNot_ConfirmUser_WhenTokenConfirmed() {

        String token = "token";
        String email = "email@email.com";

        User user = new User();
        user.setEmail(email);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setUser(user);
        confirmationToken.setConfirmedDate(LocalDateTime.now());

        given(confirmationTokenService.getByToken(token)).willReturn(new SuccessDataResult<>(confirmationToken));
        given(userService.getByEmail(email)).willReturn(new SuccessDataResult<>(user));

        Result expected = testManager.confirmUser(token);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShouldNot_ConfirmUser_WhenTokenExpired() {

        String token = "token";
        String email = "email@email.com";

        User user = new User();
        user.setEmail(email);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setUser(user);
        confirmationToken.setExpiresDate(LocalDateTime.now().minusMinutes(1));

        given(confirmationTokenService.getByToken(token)).willReturn(new SuccessDataResult<>(confirmationToken));
        given(userService.getByEmail(email)).willReturn(new SuccessDataResult<>(user));

        Result expected = testManager.confirmUser(token);

        assertThat(expected.isSuccess()).isFalse();

    }

}