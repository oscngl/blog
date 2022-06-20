package com.osc.blog.business.abstracts;

import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.dtos.UserDto;

public interface AuthService {

    Result register(UserDto userDto);
    Result confirmUser(String token);

}
