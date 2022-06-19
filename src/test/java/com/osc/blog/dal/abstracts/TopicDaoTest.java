package com.osc.blog.dal.abstracts;

import com.osc.blog.entities.concretes.Topic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TopicDaoTest {

    @Autowired
    private TopicDao testDao;

    @AfterEach
    void tearDown() {
        testDao.deleteAll();
    }

    @Test
    void itShould_FindByName_WhenTopicWithNameExists() {

        String name = "name";
        Topic topic = new Topic();
        topic.setName(name);
        testDao.save(topic);

        Topic expected = testDao.findByName(name);

        assertThat(expected).isEqualTo(topic);

    }

    @Test
    void itShouldNot_FindByName_WhenTopicWithNameDoesNotExists() {

        Topic expected = testDao.findByName("name");

        assertThat(expected).isNull();

    }

}