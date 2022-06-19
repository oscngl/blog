package com.osc.blog.business.concretes;

import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.dal.abstracts.TopicDao;
import com.osc.blog.entities.concretes.Topic;
import com.osc.blog.entities.dtos.TopicDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TopicManagerTest {

    private TopicManager testManager;

    @Mock
    private TopicDao topicDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testManager = new TopicManager(
                topicDao,
                new ModelMapper()
        );
    }

    @Test
    void itShould_Save_WhenTopicWithNameDoesNotExists() {

        String name = "name";
        TopicDto topicDto = new TopicDto();
        topicDto.setName(name);

        given(topicDao.findByName(name)).willReturn(null);

        Result expected = testManager.save(topicDto);

        ArgumentCaptor<Topic> topicArgumentCaptor = ArgumentCaptor.forClass(Topic.class);
        verify(topicDao).save(topicArgumentCaptor.capture());
        Topic capturedTopic = topicArgumentCaptor.getValue();

        assertThat(expected.isSuccess()).isTrue();
        assertThat(capturedTopic.getName()).isEqualTo(topicDto.getName());

    }

    @Test
    void itShouldNot_Save_WhenTopicWithNameExists() {

        String name = "name";
        TopicDto topicDto = new TopicDto();
        topicDto.setName(name);

        given(topicDao.findByName(name)).willReturn(new Topic());

        Result expected = testManager.save(topicDto);

        assertThat(expected.isSuccess()).isFalse();

    }

    @Test
    void itShould_GetById_WhenTopicWithIdExists() {

        int id = 1;
        Topic topic = new Topic();
        topic.setId(id);

        given(topicDao.findById(id)).willReturn(Optional.of(topic));

        DataResult<Topic> expected = testManager.getById(id);

        assertThat(expected.isSuccess()).isTrue();
        assertThat(expected.getData()).isEqualTo(topic);

    }

    @Test
    void itShouldNot_GetById_WhenTopicWithIdDoesNotExists() {

        int id = 1;

        given(topicDao.findById(id)).willReturn(Optional.empty());

        DataResult<Topic> expected = testManager.getById(id);

        assertThat(expected.isSuccess()).isFalse();
        assertThat(expected.getData()).isNull();

    }

    @Test
    void itShould_GetAll() {

        testManager.getAll();

        verify(topicDao).findAll();

    }

    @Test
    void itShould_GetByName_WhenTopicWithNameExists() {

        String name = "name";
        Topic topic = new Topic();
        topic.setName(name);

        given(topicDao.findByName(name)).willReturn(topic);

        DataResult<Topic> expected = testManager.getByName(name);

        assertThat(expected.isSuccess()).isTrue();
        assertThat(expected.getData()).isEqualTo(topic);

    }

    @Test
    void itShould_GetByName_WhenTopicWithNameDoesNotExists() {

        String name = "name";

        given(topicDao.findByName(name)).willReturn(null);

        DataResult<Topic> expected = testManager.getByName(name);

        assertThat(expected.isSuccess()).isFalse();
        assertThat(expected.getData()).isNull();

    }

}