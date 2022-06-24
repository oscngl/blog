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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthManager implements AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final EmailSenderService emailSenderService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public Result register(UserDto userDto) {
        DataResult<Role> role = roleService.getByName(userDto.getRoleName());
        if(!role.isSuccess()) {
            return new ErrorResult(role.getMessage());
        }
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        DataResult<User> user = userService.save(userDto, role.getData());
        if(!user.isSuccess()) {
            return new ErrorResult(user.getMessage());
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

    @Override
    @Transactional
    public Result confirmUser(String token) {
        DataResult<ConfirmationToken> confirmationToken = confirmationTokenService.getByToken(token);
        if(!confirmationToken.isSuccess()) {
            return new ErrorResult(confirmationToken.getMessage());
        }
        if(confirmationToken.getData().getUser().isConfirmed()) {
            return new ErrorResult("User already confirmed!");
        }
        if(confirmationToken.getData().getConfirmedDate() != null) {
            return new ErrorResult("Token already confirmed!");
        }
        if(confirmationToken.getData().getExpiresDate().isBefore(LocalDateTime.now())) {
            return new ErrorResult("Token expired!");
        }
        confirmationTokenService.confirmToken(token);
        userService.setConfirmedTrue(confirmationToken.getData().getUser().getId());
        return new SuccessResult("User confirmed!");
    }

}
