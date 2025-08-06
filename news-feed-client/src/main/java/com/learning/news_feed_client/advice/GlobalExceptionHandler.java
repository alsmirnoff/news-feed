package com.learning.news_feed_client.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.learning.news_feed_client.exception.NewsServiceUnavailableException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NewsServiceUnavailableException.class)
    public String handleServiceUnavailable(NewsServiceUnavailableException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "news/error";
    }
}
