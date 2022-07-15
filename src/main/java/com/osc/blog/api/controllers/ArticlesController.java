package com.osc.blog.api.controllers;

import com.osc.blog.business.abstracts.ArticleService;
import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.Article;
import com.osc.blog.entities.dtos.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticlesController {

    private final ArticleService articleService;

    @PostMapping(value = "/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Result save(@ModelAttribute @Valid ArticleDto articleDto) {
        return articleService.save(articleDto);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Article article) {
        return articleService.update(article);
    }

    @GetMapping("/getById")
    public DataResult<Article> getById(int id) {
        return articleService.getById(id);
    }

    @GetMapping("/getAll")
    public DataResult<List<Article>> getAll(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return articleService.getAll(pageNumber, pageSize);
    }

    @GetMapping("/getAllByUserId")
    public DataResult<List<Article>> getAllByUserId(int userId, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return articleService.getAllByUserId(userId, pageNumber, pageSize);
    }

    @GetMapping("/getAllByUsername")
    public DataResult<List<Article>> getAllByUsrname(String username, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return articleService.getAllByUsrname(username, pageNumber, pageSize);
    }

    @GetMapping("/getAllByTopicId")
    public DataResult<List<Article>> getAllByTopicId(int topicId, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return articleService.getAllByTopicId(topicId, pageNumber, pageSize);
    }

    @GetMapping("/getAllByKeywords")
    public DataResult<List<Article>> getAllByKeywords(String keywords, @RequestParam int pageNumber, @RequestParam int pageSize) {
        return articleService.getAllByKeywords(keywords, pageNumber, pageSize);
    }

    @GetMapping("/countAll")
    public DataResult<Integer> countAllByEnabledIsTrue() {
        return articleService.countAll();
    }

    @GetMapping("/countAllByUserId")
    public DataResult<Integer> countAllByUserId(int userId) {
        return articleService.countAllByUserId(userId);
    }

    @GetMapping("/countAllByTopicId")
    public DataResult<Integer> countAllByTopicId(int topicId) {
        return articleService.countAllByTopicId(topicId);
    }

    @GetMapping("/countAllByUsername")
    public DataResult<Integer> countAllByUsername(String username) {
        return articleService.countAllByUsername(username);
    }

    @GetMapping("/countAllByKeywords")
    public DataResult<Integer> countAllByKeywords(String keywords) {
        return articleService.countAllByKeywords(keywords);
    }

    @PutMapping("/setEnabledFalse")
    public Result setEnabledFalse(int id) {
        return articleService.setEnabledFalse(id);
    }

}
