package com.learning.news_feed.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NewsDTO {
    private Long id;
    private String header;
    private String body;
    private LocalDate date;
    
    @JsonCreator
    public NewsDTO(
        @JsonProperty("id") Long id, 
        @JsonProperty("header") String header, 
        @JsonProperty("body") String body, 
        @JsonProperty("date") LocalDate date) {
        this.id = id;
        this.header = header;
        this.body = body;
        this.date = date;
    }

    public NewsDTO(String header, String body, LocalDate date) {
        this.header = header;
        this.body = body;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "NewsDTO [id=" + id + ", header=" + header + ", body=" + body + ", date=" + date + "]";
    }
    
}