package com.learning.news_feed.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.news_feed.dao.NewsRepository;
import com.learning.news_feed.entity.News;

import jakarta.transaction.Transactional;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    @Transactional
    public List<News> getAllNews() {
        return newsRepository.findAll(); 
    }

    @Override
    @Transactional
    public News saveNews(News news) {
        News newPost = newsRepository.save(news);
        return newPost;
    }

    @Override
    @Transactional
    public News getNews(int id) {
        return newsRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteNews(int id) {
        newsRepository.deleteById(id);
    }

}
