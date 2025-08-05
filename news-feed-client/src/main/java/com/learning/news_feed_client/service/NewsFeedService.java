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
    private String responceQueueName;

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
        String responceQueueName = createResponceQueue();

        try {
            rabbitTemplate.convertAndSend(
                "news.request.queue",
                correlationId,
                message -> {
                    message.getMessageProperties().setReplyTo(responceQueueName);
                    return message;
                }
            );

            List<NewsDTO> news = rabbitTemplate.receiveAndConvert(
                responceQueueName,
                5000,
                new ParameterizedTypeReference<List<NewsDTO>>() {}
            );

            return news != null ? news : Collections.emptyList();
        } finally {
            deleteResponceQueue(responceQueueName);
        }
    }


    // public List<NewsDTO> getAllNews() {
    //     String correlationId = UUID.randomUUID().toString();
    //     List<NewsDTO> responce = (List<NewsDTO>) rabbitTemplate.convertSendAndReceive("news.exchange", "", correlationId);

    //     return responce.stream()
    //         .map(dto -> new NewsDTO(
    //             dto.getId(), dto.getHeader(), dto.getBody(), dto.getDate())).toList();
    // }

    public Mono<NewsResponse> getNewsById(Long id) {
        return webClient.get()
                .uri("/api/feed/{id}", id)
                .retrieve()
                .bodyToMono(NewsResponse.class);
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
           String responceQueueName = "client.respone." + UUID.randomUUID().toString();
           channel.queueDeclare(responceQueueName, false, true, true, null);
           channel.queueBind(responceQueueName, "news.exchange", "news.responce.*");
           return responceQueueName; 
        });
    }

    private void deleteResponceQueue(String responceQueueName) {
        rabbitTemplate.execute(channel -> {
            channel.queueDelete(responceQueueName);
            return null;
        });
    }
}
