package com.osc.blog.entities.concretes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "topic_id")
    private int topicId;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now();

}
