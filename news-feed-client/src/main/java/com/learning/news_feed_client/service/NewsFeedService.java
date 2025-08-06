package com.learning.news_feed_client.service;

import java.text.NumberFormat.Style;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.learning.news_feed_client.dto.NewsDTO;
import com.learning.news_feed_client.dto.NewsRequest;
import com.learning.news_feed_client.dto.NewsResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NewsFeedService {

    private WebClient webClient;
    private RabbitTemplate rabbitTemplate;
    private String responseQueueName;

    // public NewsFeedService(WebClient webClient) {
    //     this.webClient = webClient;
    // }

    public NewsFeedService(RabbitTemplate rabbitTemplate){
        // REST
        //this.webClient = webClient;

        // rabbitMQ
        this.rabbitTemplate = rabbitTemplate;
    }

    // REST
    // public Flux<NewsResponse> getAllNewsRest() {
    //     return webClient.get()
    //                 .uri("/api/feed")
    //                 .retrieve()
    //                 .bodyToFlux(NewsResponse.class);
    // }

    // rabbitMQ
    public List<NewsDTO> getAllNews() {
        String correlationId = "req_" + System.currentTimeMillis();
        String responseQueueName = createResponceQueue();

        try {
            rabbitTemplate.convertAndSend(
                "news.request.all",
                correlationId,
                message -> {
                    message.getMessageProperties().setReplyTo(responseQueueName);
                    return message;
                }
            );

            List<NewsDTO> news = rabbitTemplate.receiveAndConvert(
                responseQueueName,
                5000,
                new ParameterizedTypeReference<List<NewsDTO>>() {}
            );

            System.out.println("All news from queue: " + news);

            return news != null ? news : Collections.emptyList();
        } finally {
            deleteResponceQueue(responseQueueName);
        }
    }

    // public Mono<NewsResponse> getNewsById(Long id) {
    //     return webClient.get()
    //             .uri("/api/feed/{id}", id)
    //             .retrieve()
    //             .bodyToMono(NewsResponse.class);
    // }

    public NewsDTO getNewsById(int id) {
        String correlationId = "req_" + System.currentTimeMillis();
        String responseQueueName = createResponceQueue();

        try {
            rabbitTemplate.convertAndSend(
                "news.request.one",
                correlationId,
                message -> {
                    message.getMessageProperties().setReplyTo(responseQueueName);
                    return message;
                }
            );

            NewsDTO news = rabbitTemplate.receiveAndConvert(
                responseQueueName,
                5000,
                new ParameterizedTypeReference<NewsDTO>() {}
            );

            return news != null ? news : null;
        } finally {
            deleteResponceQueue(responseQueueName);
        }
    }

    public Mono<NewsResponse> createNews(NewsRequest request) {
        return webClient.post()
                .uri("/api/create")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(NewsResponse.class);
    }
    
    public Mono<Void> deleteNews(Long id) {
        return webClient.post()
                .uri("/api/delete/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private String createResponceQueue() {
        return rabbitTemplate.execute(channel -> {
           String responseQueueName = "client.response." + UUID.randomUUID().toString();
           channel.queueDeclare(responseQueueName, false, true, true, null);
           channel.queueBind(responseQueueName, "news.exchange", "news.response.*");
           return responseQueueName; 
        });
    }

    private void deleteResponceQueue(String responseQueueName) {
        rabbitTemplate.execute(channel -> {
            channel.queueDelete(responseQueueName);
            return null;
        });
    }
}
