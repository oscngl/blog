package com.osc.blog.business.abstracts;

import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.User;
import com.osc.blog.entities.dtos.UserDto;

import java.util.List;

public interface UserService {

    Result save(UserDto userDto);
    DataResult<User> getById(int id);
    DataResult<List<User>> getAll();
    DataResult<User> getByEmail(String email);
    Result setConfirmedTrue(int id);
    Result setPhotoUrl(int id, String photoUrl);

}
