package com.learning.news_feed.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.news_feed.dto.NewsDTO;
import com.learning.news_feed.entity.News;
import com.learning.news_feed.service.NewsService;

@RestController
@RequestMapping("/api")
public class NewsController {

    // @Value("${rabbitmq.news.exchange}")
    // private String exchangeName;

    private static final Logger log = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("/feed")
    public List<News> showAllNews() {
        List<News> allNews = newsService.getAllNews();
        return allNews;
    }

    // @RabbitListener(queues = "news.request.all")
    // public void handleAllNewsRequest(Message message) {
    //     String replyTo = message.getMessageProperties().getReplyTo();
    //     String correlationId = message.getMessageProperties().getCorrelationId();

    //     List<News> news = newsService.getAllNews();

    //     List<NewsDTO> response = news.stream()
    //         .map(NewsDTO::new)
    //         .toList();

    //     rabbitTemplate.convertAndSend(
    //         "",
    //         replyTo,
    //         response,
    //         m -> {
    //             m.getMessageProperties().setCorrelationId(correlationId);
    //             return m;
    //         }
    //     );
    // }

    @RabbitListener(queues = "news.request.all.queue")
    public List<NewsDTO> handleAllNewsRequest(Message message) {
        return newsService.getAllNews()
            .stream()
            .map(NewsDTO::new)
            .toList();
    }

    @GetMapping("/feed/{id}")
    public News getNews(@PathVariable int id) {
        News news = newsService.getNews(id);
        return news;
    }

    // @RabbitListener(queues = "news.request.one")
    // public void handleOneNewsRequest(Message message) {
    //     String replyTo = message.getMessageProperties().getReplyTo();
    //     String correlationId = message.getMessageProperties().getCorrelationId();
    //     Integer newsId = Integer.valueOf(new String(message.getBody()));

    //     News news = newsService.getNews(newsId);
    //     NewsDTO response = new NewsDTO(news);
    //     rabbitTemplate.convertAndSend(
    //         "",
    //         replyTo,
    //         response,
    //         m -> {
    //             m.getMessageProperties().setCorrelationId(correlationId);
    //             return m;
    //         }
    //     );
    // }

    @RabbitListener(queues = "news.request.one.queue")
    public NewsDTO handleOneNewsRequest(Message message) {
        Integer newsId = Integer.valueOf(new String(message.getBody()));
        News news = newsService.getNews(newsId);
        return new NewsDTO(news);
    }

    @PostMapping("/create")
    public News addNewPost(@RequestBody News news) {
        News newPost = newsService.saveNews(news);
        return newPost;
    }

    @RabbitListener(queues = "news.create.queue")
    public NewsDTO handleCreateNewPost(NewsDTO newsDTO) {
        News news = convertToEntity(newsDTO);
        News newPost = newsService.saveNews(news);
        return new NewsDTO(newPost);
    }

    @PutMapping("/edit/{id}")
    public News editPost(@RequestBody News news) {
        News newPost = newsService.saveNews(news);
        return newPost;
    }

    @RabbitListener(queues = "news.edit.queue")
    public NewsDTO handleEditPost(NewsDTO newsDTO) {
        // log.info("Received NewsDTO from queue: {}", newsDTO);
        News news = convertToEntity(newsDTO);
        // log.debug("Converted to entity: {}", news);
        News newPost = newsService.saveNews(news);
        // log.info("News saved successfully with ID: {}", newPost.getId());
        return new NewsDTO(newPost);
    }

    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable int id) {
        newsService.deleteNews(id);
        return "News with id = " + id + " was deleted";
    }

    @RabbitListener(queues = "news.delete.queue")
    public void handleDeleteNews(Message message) {
        Integer newsId = Integer.valueOf(new String(message.getBody()));
        newsService.deleteNews(newsId);
    }

    private News convertToEntity(NewsDTO dto) {
        News news = new News();
        news.setId(dto.getId());
        news.setHeader(dto.getHeader());
        news.setBody(dto.getBody());
        news.setDate(dto.getDate());
        return news;
    }
}
