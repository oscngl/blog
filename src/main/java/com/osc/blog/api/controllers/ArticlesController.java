package com.osc.blog.api.controllers;

import com.osc.blog.business.abstracts.ArticleService;
import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.Article;
import com.osc.blog.entities.dtos.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticlesController {

    private final ArticleService articleService;

    @PostMapping("/save")
    public Result save(@RequestBody @Valid ArticleDto articleDto) {
        return articleService.save(articleDto);
    }

    @GetMapping("/getById")
    public DataResult<Article> getById(int id){
        return articleService.getById(id);
    }

    @GetMapping("/getAll")
    public DataResult<List<Article>> getAll(){
        return articleService.getAll();
    }

    @GetMapping("/getAllByUserId")
    public DataResult<List<Article>> getAllByUserId(int userId){
        return articleService.getAllByUserId(userId);
    }

    @GetMapping("/getAllByTopicId")
    public DataResult<List<Article>> getAllByTopicId(int topicId){
        return articleService.getAllByTopicId(topicId);
    }

    @PutMapping("/setEnabledFalse")
    public Result setEnabledFalse(int id){
        return articleService.setEnabledFalse(id);
    }

}
