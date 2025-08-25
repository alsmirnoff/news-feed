package com.learning.news_feed.mapper;

import org.springframework.stereotype.Component;

import com.learning.news_feed.dto.NewsDTO;
import com.learning.news_feed.entity.News;

@Component
public class NewsMapper {
    
    public NewsDTO toDto(News news) {
        if (news == null) return null;
        
        return new NewsDTO(
            news.getId(),
            news.getHeader(),
            news.getBody(),
            news.getDate()
        );
    }
    
    public News toEntity(NewsDTO newsDTO) {
        if (newsDTO == null) return null;
        
        News news = new News();
        news.setHeader(newsDTO.getHeader());
        news.setBody(newsDTO.getBody());
        news.setDate(newsDTO.getDate());
        
        return news;
    }
}
