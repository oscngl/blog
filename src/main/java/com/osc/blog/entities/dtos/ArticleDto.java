package com.osc.blog.entities.dtos;

import com.osc.blog.entities.concretes.Topic;
import com.osc.blog.entities.concretes.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    @NotEmpty(message = "Title is required!")
    @NotBlank(message = "Title is required!")
    private String title;

    @NotEmpty(message = "Text is required!")
    @NotBlank(message = "Text is required!")
    private String text;

    @NotNull(message = "User is required!")
    private User user;

    @NotNull(message = "Topic is required!")
    private Topic topic;

    private MultipartFile photo;

}
