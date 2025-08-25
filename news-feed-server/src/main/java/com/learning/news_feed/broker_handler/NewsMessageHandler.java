package com.learning.news_feed.broker_handler;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learning.news_feed.dto.NewsDTO;
import com.learning.news_feed.service.NewsService;

@Component
public class NewsMessageHandler {

    @Autowired
    private final NewsService newsService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public NewsMessageHandler(NewsService newsService) {
        this.newsService = newsService;
    }

    @RabbitListener(queues = "news.crud.queue")
    public Object handleMessage(Message message) {
        String operation = message.getMessageProperties().getHeader("operation");
        Object payload = rabbitTemplate.getMessageConverter().fromMessage(message);

        return switch (operation) {
            case "CREATE" -> newsService.saveNews((NewsDTO) payload);
            case "GET" -> newsService.getNews((Long) payload);
            case "GET_ALL" -> newsService.getAllNews();
            case "UPDATE" -> newsService.saveNews((NewsDTO) payload);
            case "DELETE" -> {
                newsService.deleteNews((Long) payload);
                yield null;
            }
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        };
    }

}
