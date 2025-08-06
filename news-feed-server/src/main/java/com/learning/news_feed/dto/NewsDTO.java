package com.learning.news_feed.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learning.news_feed.entity.News;

public class NewsDTO {
    private int id;
    private String header;
    private String body;
    private LocalDate date;
    
    /*@JsonCreator
    public NewsDTO(
        @JsonProperty("id") int id, 
        @JsonProperty("header") String header, 
        @JsonProperty("body") String body, 
        @JsonProperty("date") LocalDate date) {
        this.id = id;
        this.header = header;
        this.body = body;
        this.date = date;
    }*/

    public NewsDTO(News news) {
        this.id = news.getId();
        this.header = news.getHeader();
        this.body = news.getBody();
        this.date = news.getDate().toLocalDate();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
}