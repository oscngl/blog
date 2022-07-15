package com.osc.blog.business.abstracts;

import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.Article;
import com.osc.blog.entities.dtos.ArticleDto;

import java.util.List;

public interface ArticleService {

    Result save(ArticleDto articleDto);
    Result update(Article article);
    DataResult<Article> getById(int id);
    DataResult<List<Article>> getAll(int pageNumber, int pageSize);
    DataResult<List<Article>> getAllByUserId(int userId, int pageNumber, int pageSize);
    DataResult<List<Article>> getAllByUsrname(String username, int pageNumber, int pageSize);
    DataResult<List<Article>> getAllByTopicId(int topicId, int pageNumber, int pageSize);
    DataResult<List<Article>> getAllByKeywords(String keywords, int pageNumber, int pageSize);
    DataResult<Integer> countAll();
    DataResult<Integer> countAllByUserId(int userId);
    DataResult<Integer> countAllByTopicId(int topicId);
    DataResult<Integer> countAllByUsername(String username);
    DataResult<Integer> countAllByKeywords(String keywords);
    Result setEnabledFalse(int id);

}
