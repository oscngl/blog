package com.osc.blog.business.abstracts;

import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.Article;
import com.osc.blog.entities.dtos.ArticleDto;

import java.util.List;

public interface ArticleService {

    Result save(ArticleDto articleDto);
    DataResult<Article> getById(int id);
    DataResult<List<Article>> getAll();
    DataResult<List<Article>> getAllByUserId(int userId);
    DataResult<List<Article>> getAllByTopicId(int topicId);
    Result setEnabledFalse(int id);

}
