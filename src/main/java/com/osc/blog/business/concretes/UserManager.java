package com.osc.blog.business.concretes;

import com.osc.blog.business.abstracts.UserService;
import com.osc.blog.core.adapters.abstracts.ImageUploadService;
import com.osc.blog.core.utilities.results.*;
import com.osc.blog.dal.abstracts.UserDao;
import com.osc.blog.entities.concretes.Role;
import com.osc.blog.entities.concretes.User;
import com.osc.blog.entities.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {

    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final ImageUploadService imageUploadService;

    @Override
    public DataResult<User> save(UserDto userDto, Role role) {
        User exists = userDao.findByConfirmedIsTrueAndEmail(userDto.getEmail());
        if(exists != null) {
            return new ErrorDataResult<>(null, "Email already taken!");
        }
        exists = userDao.findByConfirmedIsTrueAndUsrname(userDto.getUsrname());
        if(exists != null) {
            return new ErrorDataResult<>(null, "Username already taken!");
        }
        User user = modelMapper.map(userDto, User.class);
        user.getRoles().add(role);
        userDao.save(user);
        return new SuccessDataResult<>(user, "User saved.");
    }

    @Override
    public Result update(User user) {
        User exists = userDao.findById(user.getId()).orElse(null);
        if(exists == null) {
            return new ErrorResult("User not found!");
        }
        userDao.saveAndFlush(user);
        return new SuccessResult("User updated.");
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
    public DataResult<User> getByUsername(String username) {
        User user = userDao.findByConfirmedIsTrueAndUsrname(username);
        if(user == null) {
            return new ErrorDataResult<>(null, "User not found!");
        }
        return new SuccessDataResult<>(user);
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
    @Transactional
    public Result setPhotoUrl(int id, MultipartFile photo) {
        User exists = userDao.findById(id).orElse(null);
        if(exists == null) {
            return new ErrorResult("User not found!");
        }
        if(photo == null) {
            return new ErrorResult("Photo not found!");
        }
        DataResult<String> uploaded = imageUploadService.uploadUserPhoto(photo);
        if (uploaded.getData() == null) {
            return new ErrorResult("Failed to upload!");
        }
        userDao.setPhotoUrl(id, uploaded.getData());
        return new SuccessResult("Photo Url uploaded.");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByConfirmedIsTrueAndEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("User not found in the database!");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

}
