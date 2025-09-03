package com.learning.news_feed.exception;

public class NewsNotFoundException extends RuntimeException {
    
    public NewsNotFoundException(String message) {
        super(message);
    }
}
