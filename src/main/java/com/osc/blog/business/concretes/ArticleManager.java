package com.osc.blog.business.concretes;

import com.osc.blog.business.abstracts.ArticleService;
import com.osc.blog.core.adapters.abstracts.ImageUploadService;
import com.osc.blog.core.utilities.results.*;
import com.osc.blog.dal.abstracts.ArticleDao;
import com.osc.blog.entities.concretes.Article;
import com.osc.blog.entities.dtos.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Result update(Article article) {
        Article exists = articleDao.findById(article.getId()).orElse(null);
        if(exists == null) {
            return new ErrorResult("Article not found!");
        }
        articleDao.saveAndFlush(article);
        return new SuccessResult("Article updated.");
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
    public DataResult<List<Article>> getAll(int pageNumber, int pageSize) {
        return new SuccessDataResult<>(articleDao.findAllByEnabledIsTrueOrderByCreatedDateDesc(PageRequest.of(pageNumber,pageSize)));
    }

    @Override
    public DataResult<List<Article>> getAllByUserId(int userId, int pageNumber, int pageSize) {
        return new SuccessDataResult<>(articleDao.findAllByEnabledIsTrueAndUserIdOrderByCreatedDateDesc(userId, PageRequest.of(pageNumber,pageSize)));
    }

    @Override
    public DataResult<List<Article>> getAllByUsrname(String username, int pageNumber, int pageSize) {
        return new SuccessDataResult<>(articleDao.findAllByEnabledIsTrueAndUser_UsrnameOrderByCreatedDateDesc(username, PageRequest.of(pageNumber,pageSize)));
    }

    @Override
    public DataResult<List<Article>> getAllByTopicId(int topicId, int pageNumber, int pageSize) {
        return new SuccessDataResult<>(articleDao.findAllByEnabledIsTrueAndTopicIdOrderByCreatedDateDesc(topicId, PageRequest.of(pageNumber,pageSize)));
    }

    @Override
    public DataResult<List<Article>> getAllByKeywords(String keywords, int pageNumber, int pageSize) {
        return new SuccessDataResult<>(articleDao.findAllByEnabledIsTrueAndTitleContainingIgnoreCaseOrderByCreatedDateDesc(keywords, PageRequest.of(pageNumber,pageSize)));
    }

    @Override
    public DataResult<Integer> countAll() {
        return new SuccessDataResult<>(articleDao.countAllByEnabledIsTrue());
    }

    @Override
    public DataResult<Integer> countAllByUserId(int userId) {
        return new SuccessDataResult<>(articleDao.countAllByEnabledIsTrueAndUserId(userId));
    }

    @Override
    public DataResult<Integer> countAllByTopicId(int topicId) {
        return new SuccessDataResult<>(articleDao.countAllByEnabledIsTrueAndTopicId(topicId));
    }

    @Override
    public DataResult<Integer> countAllByUsername(String username) {
        return new SuccessDataResult<>(articleDao.countAllByEnabledIsTrueAndUser_Usrname(username));
    }

    @Override
    public DataResult<Integer> countAllByKeywords(String keywords) {
        return new SuccessDataResult<>(articleDao.countAllByEnabledIsTrueAndKeywords(keywords));
    }

    @Override
    @Transactional
    public Result setEnabledFalse(int id) {
        Article exists = articleDao.findById(id).orElse(null);
        if(exists == null) {
            return new ErrorResult("Article not found!");
        }
        articleDao.setEnabledFalse(id);
        return new SuccessResult("Article disabled!");
    }

}
