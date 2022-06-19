package com.osc.blog.business.concretes;

import com.osc.blog.business.abstracts.ArticleService;
import com.osc.blog.core.adapters.abstracts.ImageUploadService;
import com.osc.blog.core.utilities.results.*;
import com.osc.blog.dal.abstracts.ArticleDao;
import com.osc.blog.entities.concretes.Article;
import com.osc.blog.entities.dtos.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleManager implements ArticleService {

    private final ArticleDao articleDao;
    private final ImageUploadService imageUploadService;

    @Override
    public Result save(ArticleDto articleDto) {
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setText(articleDto.getText());
        article.setUser(articleDto.getUser());
        article.setTopic(articleDto.getTopic());
        if(articleDto.getPhoto() != null) {
            DataResult<String> uploaded = imageUploadService.uploadArticlePhoto(articleDto.getPhoto());
            if (!uploaded.isSuccess()) {
                return new ErrorResult("Failed to upload!");
            }
            article.setPhotoUrl(uploaded.getData());
        }
        articleDao.save(article);
        return new SuccessResult("Article saved.");
    }

    @Override
    public DataResult<Article> getById(int id) {
        Article article = articleDao.findById(id).orElse(null);
        if(article == null) {
            return new ErrorDataResult<>(null, "Article not found!");
        }
        return new SuccessDataResult<>(article);
    }

    @Override
    public DataResult<List<Article>> getAll() {
        return new SuccessDataResult<>(articleDao.findAllByEnabledIsTrue());
    }

    @Override
    public DataResult<List<Article>> getAllByUserId(int userId) {
        return new SuccessDataResult<>(articleDao.findAllByEnabledIsTrueAndUserId(userId));
    }

    @Override
    public DataResult<List<Article>> getAllByTopicId(int topicId) {
        return new SuccessDataResult<>(articleDao.findAllByEnabledIsTrueAndTopicId(topicId));
    }

    @Override
    public Result setEnabledFalse(int id) {
        Article exists = articleDao.findById(id).orElse(null);
        if(exists == null) {
            return new ErrorResult("Article not found!");
        }
        articleDao.setEnabledFalse(id);
        return new SuccessResult("Article disabled!");
    }

}
