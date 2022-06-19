package com.osc.blog.business.abstracts;

import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.Topic;
import com.osc.blog.entities.dtos.TopicDto;

import java.util.List;

public interface TopicService {

    Result save(TopicDto topicDto);
    DataResult<Topic> getById(int id);
    DataResult<List<Topic>> getAll();
    DataResult<Topic> getByName(String name);

}
