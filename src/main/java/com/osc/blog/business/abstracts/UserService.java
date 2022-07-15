package com.osc.blog.business.abstracts;

import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.Role;
import com.osc.blog.entities.concretes.User;
import com.osc.blog.entities.dtos.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {

    DataResult<User> save(UserDto userDto, Role role);
    Result update(User user);
    DataResult<User> getById(int id);
    DataResult<List<User>> getAll();
    DataResult<User> getByUsername(String username);
    DataResult<User> getByEmail(String email);
    Result setConfirmedTrue(int id);
    Result setPhotoUrl(int id, MultipartFile photo);

}
