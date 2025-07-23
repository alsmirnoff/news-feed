package com.learning.news_feed.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.news_feed.entity.News;
import com.learning.news_feed.service.NewsService;

@RestController
@RequestMapping("/api")
public class NewsRESTController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/feed")
    public List<News> showAllNews() {
        List<News> allNews = newsService.getAllNews();
        return allNews;
    }

    @GetMapping("/feed/{id}")
    public News getNews(@PathVariable int id) {
        News news = newsService.getNews(id);
        return news;
    }

    @PostMapping("/create")
    public News addNewPost(@RequestBody News news) {
        News newPost = newsService.saveNews(news);
        return newPost;
    }

    @PutMapping("/feed")
    public News editPost(@RequestBody News news) {
        News newPost = newsService.saveNews(news);
        return newPost;
    }

    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable int id) {
        newsService.deleteNews(id);
        return "News with id = " + id + " was deleted";
    }
}
