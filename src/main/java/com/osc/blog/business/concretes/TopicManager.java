package com.osc.blog.business.concretes;

import com.osc.blog.business.abstracts.TopicService;
import com.osc.blog.core.utilities.results.*;
import com.osc.blog.dal.abstracts.TopicDao;
import com.osc.blog.entities.concretes.Topic;
import com.osc.blog.entities.dtos.TopicDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicManager implements TopicService {

    private final TopicDao topicDao;
    private final ModelMapper modelMapper;

    @Override
    public Result save(TopicDto topicDto) {
        Topic exists = topicDao.findByName(topicDto.getName());
        if(exists != null) {
            return new ErrorResult("Topic already exists!");
        }
        Topic topic = modelMapper.map(topicDto, Topic.class);
        topicDao.save(topic);
        return new SuccessResult("Topic saved.");
    }

    @Override
    public DataResult<Topic> getById(int id) {
        Topic topic = topicDao.findById(id).orElse(null);
        if(topic == null) {
            return new ErrorDataResult<>(null, "Topic not found!");
        }
        return new SuccessDataResult<>(topic);
    }

    @Override
    public DataResult<List<Topic>> getAll() {
        return new SuccessDataResult<>(topicDao.findAll());
    }

    @Override
    public DataResult<Topic> getByName(String name) {
        Topic topic = topicDao.findByName(name);
        if(topic == null) {
            return new ErrorDataResult<>(null, "Topic not found!");
        }
        return new SuccessDataResult<>(topic);
    }

}
