package com.learning.news_feed.service;

import java.util.List;

import com.learning.news_feed.dto.NewsDTO;

public interface NewsService {
    public List<NewsDTO> getAllNews();

    public NewsDTO saveNews(NewsDTO newsDTO);

    public NewsDTO getNews(Long id);

    public void deleteNews(Long id);
}
