package com.learning.news_feed_client.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record NewsRequest(
    String header,
    String body,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date
) {}
