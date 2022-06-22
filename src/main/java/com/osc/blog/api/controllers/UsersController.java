package com.osc.blog.api.controllers;

import com.osc.blog.business.abstracts.UserService;
import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UserService userService;

    @GetMapping("/getById")
    public DataResult<User> getById(int id) {
        return userService.getById(id);
    }

    @GetMapping("/getAll")
    public DataResult<List<User>> getAll() {
        return userService.getAll();
    }

    @GetMapping("/getByEmail")
    public DataResult<User> getByEmail(String email) {
        return userService.getByEmail(email);
    }

    @PostMapping("/setPhotoUrl")
    public Result setPhotoUrl(@RequestParam("id") int id, @RequestParam("photo") MultipartFile photo) {
        return userService.setPhotoUrl(id, photo);
    }

}
