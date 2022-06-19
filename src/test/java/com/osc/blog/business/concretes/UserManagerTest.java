package com.osc.blog.business.concretes;

import com.osc.blog.business.abstracts.ConfirmationTokenService;
import com.osc.blog.core.adapters.abstracts.EmailSenderService;
import com.osc.blog.core.adapters.abstracts.ImageUploadService;
import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.ErrorDataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.core.utilities.results.SuccessDataResult;
import com.osc.blog.dal.abstracts.UserDao;
import com.osc.blog.entities.concretes.User;
import com.osc.blog.entities.dtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserManagerTest {

    private UserManager testManager;

    @Mock
    private UserDao userDao;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private ImageUploadService imageUploadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testManager = new UserManager(
                userDao,
                new ModelMapper(),
                emailSenderService,
                confirmationTokenService,
                imageUploadService
        );
    }

    @Test
    void itShould_Save_WhenUserWithEmailDoesNotExists() {

        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email@email.com";
        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setEmail(email);
        userDto.setPassword("password");

        given(userDao.findByConfirmedIsTrueAndEmail(userDto.getEmail())).willReturn(null);

        Result expected = testManager.save(userDto);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(expected.isSuccess()).isTrue();
        assertThat(capturedUser.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(capturedUser.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(capturedUser.getEmail()).isEqualTo(userDto.getEmail());

    }

    @Test
    void itShouldNot_Save_WhenUserWithEmailExists() {

        String email = "email@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);

        given(userDao.findByConfirmedIsTrueAndEmail(email)).willReturn(new User());

        Result expected = testManager.save(userDto);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShould_GetById_WhenUserWithIdExists() {

        int id = 1;
        User user = new User();
        user.setId(id);

        given(userDao.findById(id)).willReturn(Optional.of(user));

        DataResult<User> expected = testManager.getById(id);

        assertThat(expected.isSuccess()).isTrue();
        assertThat(expected.getData()).isEqualTo(user);

    }

    @Test
    void itShouldNot_GetById_WhenUserWithIdDoesNotExists() {

        int id = 1;

        given(userDao.findById(id)).willReturn(Optional.empty());

        DataResult<User> expected = testManager.getById(id);

        assertThat(expected.isSuccess()).isFalse();
        assertThat(expected.getData()).isNull();

    }

    @Test
    void itShould_GetAll() {

        testManager.getAll();

        verify(userDao).findAllByConfirmedIsTrue();

    }

    @Test
    void itShould_GetByEmail_WhenUserWithEmailExists() {

        String email = "email@email.com";
        User user = new User();
        user.setId(1);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(email);
        user.setPassword("password");
        user.setConfirmed(true);

        given(userDao.findByConfirmedIsTrueAndEmail(email)).willReturn(user);

        DataResult<User> expected = testManager.getByEmail(email);

        assertThat(expected.isSuccess()).isTrue();
        assertThat(expected.getData()).isEqualTo(user);

    }

    @Test
    void itShouldNot_GetByEmail_WhenUserWithEmailDoesNotExists() {

        String email = "email@email.com";

        given(userDao.findByConfirmedIsTrueAndEmail(email)).willReturn(null);

        DataResult<User> expected = testManager.getByEmail(email);

        assertThat(expected.isSuccess()).isFalse();
        assertThat(expected.getData()).isNull();

    }

    @Test
    void itShould_SetConfirmedTrue_WhenUserWithIdExists() {

        int id = 1;
        User user = new User();
        user.setId(id);

        given(userDao.findById(id)).willReturn(Optional.of(user));

        Result expected = testManager.setConfirmedTrue(id);

        assertThat(expected.isSuccess()).isTrue();

    }

    @Test
    void itShouldNot_SetConfirmedTrue_WhenUserWithIdDoesNotExists() {

        int id = 1;

        given(userDao.findById(id)).willReturn(Optional.empty());

        Result expected = testManager.setConfirmedTrue(id);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShould_SetPhotoUrl_WhenUserWithIdExists() {

        MultipartFile image = new MockMultipartFile("photo", new byte[]{1});
        int id = 1;
        User user = new User();
        user.setId(id);

        given(userDao.findById(id)).willReturn(Optional.of(user));
        given(imageUploadService.uploadUserPhoto(image)).willReturn(new SuccessDataResult<>("url", null));

        Result expected = testManager.setPhotoUrl(id, image);

        assertThat(expected.isSuccess()).isTrue();

    }

    @Test
    void itShouldNot_SetPhotoUrl_WhenUserWithIdDoesNotExists() {

        MultipartFile image = new MockMultipartFile("photo", new byte[]{1});
        int id = 1;

        given(userDao.findById(id)).willReturn(Optional.empty());

        Result expected = testManager.setPhotoUrl(id, image);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShouldNot_SetPhotoUrl_WhenPhotoDoesNotExists() {

        int id = 1;
        User user = new User();
        user.setId(id);

        given(userDao.findById(id)).willReturn(Optional.of(user));

        Result expected = testManager.setPhotoUrl(id, null);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShouldNot_SetPhotoUrl_WhenPhotoCanNotUpload() {

        MultipartFile image = new MockMultipartFile("photo", new byte[]{1});
        int id = 1;
        User user = new User();
        user.setId(id);

        given(userDao.findById(id)).willReturn(Optional.of(user));
        given(imageUploadService.uploadUserPhoto(image)).willReturn(new ErrorDataResult<>());

        Result expected = testManager.setPhotoUrl(id, image);

        assertThat(expected.isSuccess()).isFalse();

    }

}