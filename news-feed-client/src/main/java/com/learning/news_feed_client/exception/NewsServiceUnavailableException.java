package com.learning.news_feed_client.exception;

public class NewsServiceUnavailableException extends RuntimeException{

    public NewsServiceUnavailableException(String message) {
        super(message);
    }
}
