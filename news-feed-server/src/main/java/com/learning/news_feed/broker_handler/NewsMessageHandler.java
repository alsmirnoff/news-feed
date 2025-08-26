package com.learning.news_feed.broker_handler;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
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

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue("news.queue"),
            exchange = @Exchange(value = "news.exchange", type = ExchangeTypes.TOPIC),
            key = {"news.create", "news.get", "news.get.all", "news.update", "news.delete"}
        )
    )
    public Object handleMessage(Message message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        Object payload = rabbitTemplate.getMessageConverter().fromMessage(message);
        
        return switch (routingKey) {
            case "news.create" -> newsService.saveNews((NewsDTO) payload);
            case "news.get" -> newsService.getNews((Long) payload);
            case "news.get.all" -> newsService.getAllNews();
            case "news.update" -> newsService.saveNews((NewsDTO) payload);
            case "news.delete" -> {
                newsService.deleteNews((Long) payload);
                yield null;
            }
            default -> throw new IllegalArgumentException("Unknown operation: " + routingKey);
        };
    }

}
