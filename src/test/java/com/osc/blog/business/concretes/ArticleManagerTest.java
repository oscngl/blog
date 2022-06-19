package com.osc.blog.business.concretes;

import com.osc.blog.core.adapters.abstracts.ImageUploadService;
import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.ErrorDataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.core.utilities.results.SuccessDataResult;
import com.osc.blog.dal.abstracts.ArticleDao;
import com.osc.blog.entities.concretes.Article;
import com.osc.blog.entities.concretes.Topic;
import com.osc.blog.entities.concretes.User;
import com.osc.blog.entities.dtos.ArticleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArticleManagerTest {

    private ArticleManager testManager;

    @Mock
    private ArticleDao articleDao;

    @Mock
    private ImageUploadService imageUploadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testManager = new ArticleManager(
                articleDao,
                imageUploadService
        );
    }

    @Test
    void itShould_SaveWhenPhotoCanUpload() {

        MultipartFile image = new MockMultipartFile("photo", new byte[]{1});
        String title = "title";
        String text = "text";
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(title);
        articleDto.setText(text);
        articleDto.setUser(new User());
        articleDto.setTopic(new Topic());
        articleDto.setPhoto(image);

        given(imageUploadService.uploadArticlePhoto(image)).willReturn(new SuccessDataResult<>("url", null));

        Result expected = testManager.save(articleDto);

        ArgumentCaptor<Article> articleArgumentCaptor = ArgumentCaptor.forClass(Article.class);
        verify(articleDao).save(articleArgumentCaptor.capture());
        Article capturedArticle = articleArgumentCaptor.getValue();

        assertThat(expected.isSuccess()).isTrue();
        assertThat(capturedArticle.getTitle()).isEqualTo(articleDto.getTitle());
        assertThat(capturedArticle.getText()).isEqualTo(articleDto.getText());

    }

    @Test
    void itShouldNot_Save_WhenPhotoCanNotUpload() {

        MultipartFile image = new MockMultipartFile("photo", new byte[]{1});
        String title = "title";
        String text = "text";
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(title);
        articleDto.setText(text);
        articleDto.setUser(new User());
        articleDto.setTopic(new Topic());
        articleDto.setPhoto(image);

        given(imageUploadService.uploadArticlePhoto(image)).willReturn(new ErrorDataResult<>());

        Result expected = testManager.save(articleDto);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShould_GetById_WhenArticleWithIdExists() {

        int id = 1;
        Article article = new Article();
        article.setId(id);

        given(articleDao.findById(id)).willReturn(Optional.of(article));

        DataResult<Article> expected = testManager.getById(id);

        assertThat(expected.isSuccess()).isTrue();
        assertThat(expected.getData()).isEqualTo(article);

    }

    @Test
    void itShouldNot_GetById_WhenArticleWithIdDoesNotExists() {

        int id = 1;

        given(articleDao.findById(id)).willReturn(Optional.empty());

        DataResult<Article> expected = testManager.getById(id);

        assertThat(expected.isSuccess()).isFalse();
        assertThat(expected.getData()).isNull();

    }

    @Test
    void itShould_GetAll() {

        testManager.getAll();

        verify(articleDao).findAllByEnabledIsTrue();

    }

    @Test
    void itShould_GetAllByUserId() {

        int userId = 1;

        testManager.getAllByUserId(userId);

        verify(articleDao).findAllByEnabledIsTrueAndUserId(userId);

    }

    @Test
    void itShould_GetAllByTopicId() {

        int topicId = 1;

        testManager.getAllByTopicId(topicId);

        verify(articleDao).findAllByEnabledIsTrueAndTopicId(topicId);

    }

    @Test
    void itShould_SetEnabledFalse_WhenArticleWithIdExists() {

        int id = 1;
        Article article = new Article();
        article.setId(id);

        given(articleDao.findById(id)).willReturn(Optional.of(article));

        Result expected = testManager.setEnabledFalse(id);

        assertThat(expected.isSuccess()).isTrue();

    }

    @Test
    void itShouldNot_SetEnabledFalse_WhenArticleWithIdDoesNotExists() {

        int id = 1;

        given(articleDao.findById(id)).willReturn(Optional.empty());

        Result expected = testManager.setEnabledFalse(id);

        assertThat(expected.isSuccess()).isFalse();

    }

}