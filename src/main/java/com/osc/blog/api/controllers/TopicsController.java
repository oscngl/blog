package com.osc.blog.api.controllers;

import com.osc.blog.business.abstracts.TopicService;
import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.Topic;
import com.osc.blog.entities.dtos.TopicDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/topics")
public class TopicsController {

    private final TopicService topicService;

    @PostMapping("/save")
    public Result save(@RequestBody @Valid TopicDto topicDto) {
        return topicService.save(topicDto);
    }

    @GetMapping("/getById")
    public DataResult<Topic> getById(int id) {
        return topicService.getById(id);
    }

    @GetMapping("/getAll")
    public DataResult<List<Topic>> getAll() {
        return topicService.getAll();
    }

    @GetMapping("/getByName")
    public DataResult<Topic> getByName(String name) {
        return topicService.getByName(name);
    }

}
