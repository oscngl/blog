package com.osc.blog.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty(message = "Name is required!")
    @NotBlank(message = "Name is required!")
    private String firstName;

    @NotEmpty(message = "Last Name is required!")
    @NotBlank(message = "Last Name is required!")
    private String lastName;

    @NotEmpty(message = "Name is required!")
    @NotBlank(message = "Name is required!")
    @Email(message = "Email is not valid!")
    private String email;

    @NotEmpty(message = "Password is required!")
    @NotBlank(message = "Password is required!")
    @Size(min = 8, max = 20, message = "Password must contain at least 8 and maximum 20 characters!")
    @Pattern(regexp = "^[0-9a-zA-Z]*$", message = "Password is not valid!")
    private String password;

    @NotEmpty(message = "Role is required!")
    @NotBlank(message = "Role is required!")
    private String roleName;

}
