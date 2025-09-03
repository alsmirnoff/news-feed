package com.learning.news_feed.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.news_feed.entity.News;

public interface NewsRepository extends JpaRepository<News, Long>{
}
