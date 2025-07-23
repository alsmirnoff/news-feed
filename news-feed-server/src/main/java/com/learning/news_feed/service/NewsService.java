package com.learning.news_feed.service;

import java.util.List;

import com.learning.news_feed.entity.News;

public interface NewsService {
    public List<News> getAllNews();

    public News saveNews(News news);

    public News getNews(int id);

    public void deleteNews(int id);
}
