package com.osc.blog.api.controllers;

import com.osc.blog.business.abstracts.AuthService;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Result register(@RequestBody @Valid UserDto userDto) {
        return authService.register(userDto);
    }

    @GetMapping("/confirmUser")
    public Result confirmUser(@RequestParam("token") String token) {
        return authService.confirmUser(token);
    }

}
