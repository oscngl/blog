package com.osc.blog.business.concretes;

import com.osc.blog.business.abstracts.AuthService;
import com.osc.blog.business.abstracts.ConfirmationTokenService;
import com.osc.blog.business.abstracts.RoleService;
import com.osc.blog.business.abstracts.UserService;
import com.osc.blog.core.adapters.abstracts.EmailSenderService;
import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.ErrorResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.core.utilities.results.SuccessResult;
import com.osc.blog.entities.concretes.ConfirmationToken;
import com.osc.blog.entities.concretes.Role;
import com.osc.blog.entities.concretes.User;
import com.osc.blog.entities.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthManager implements AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final EmailSenderService emailSenderService;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public Result register(UserDto userDto) {
        DataResult<Role> role = roleService.getByName(userDto.getRoleName());
        if(role == null || !role.isSuccess()) {
            return new ErrorResult("Something went wrong!");
        }
        DataResult<User> user = userService.save(userDto, role.getData());
        if(user == null || !user.isSuccess()) {
            return new ErrorResult("Something went wrong!");
        }
        ConfirmationToken confirmationToken = new ConfirmationToken(user.getData());
        confirmationTokenService.save(confirmationToken);
        emailSenderService.sendConfirmationEmail(
                user.getData().getEmail(),
                user.getData().getFirstName(),
                confirmationToken.getToken()
        );
        return new SuccessResult("User registered.");
    }

}
