package com.osc.blog.business.concretes;

import com.osc.blog.business.abstracts.ConfirmationTokenService;
import com.osc.blog.business.abstracts.UserService;
import com.osc.blog.core.adapters.abstracts.EmailSenderService;
import com.osc.blog.core.utilities.results.*;
import com.osc.blog.dal.abstracts.UserDao;
import com.osc.blog.entities.concretes.ConfirmationToken;
import com.osc.blog.entities.concretes.User;
import com.osc.blog.entities.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {

    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final EmailSenderService emailSenderService;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public Result save(UserDto userDto) {
        User exists = userDao.findByConfirmedIsTrueAndEmail(userDto.getEmail());
        if(exists != null) {
            return new ErrorResult("Email already taken!");
        }
        User user = modelMapper.map(userDto, User.class);
        userDao.save(user);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenService.save(confirmationToken);
        emailSenderService.sendConfirmationEmail(user.getEmail(), user.getFirstName(), confirmationToken.getToken());
        return new SuccessResult("User saved.");
    }

    @Override
    public DataResult<User> getById(int id) {
        User user = userDao.findById(id).orElse(null);
        if(user == null) {
            return new ErrorDataResult<>(null, "User not found!");
        }
        return new SuccessDataResult<>(user);
    }

    @Override
    public DataResult<List<User>> getAll() {
        return new SuccessDataResult<>(userDao.findAllByConfirmedIsTrue());
    }

    @Override
    public DataResult<User> getByEmail(String email) {
        User user = userDao.findByConfirmedIsTrueAndEmail(email);
        if(user == null) {
            return new ErrorDataResult<>(null, "User not found!");
        }
        return new SuccessDataResult<>(user);
    }

    @Override
    public Result setConfirmedTrue(int id) {
        User exists = userDao.findById(id).orElse(null);
        if(exists == null) {
            return new ErrorResult("User not found!");
        }
        userDao.setConfirmedTrue(id);
        return new SuccessResult("User confirmed.");
    }

    @Override
    public Result setPhotoUrl(int id, String photoUrl) {
        User exists = userDao.findById(id).orElse(null);
        if(exists == null) {
            return new ErrorResult("User not found!");
        }
        userDao.setPhotoUrl(id, photoUrl);
        return new SuccessResult("Photo Url uploaded.");
    }

}
