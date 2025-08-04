package com.learning.news_feed_client.service;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.news_feed_client.dto.NewsDTO;
import com.learning.news_feed_client.dto.NewsRequest;
import com.learning.news_feed_client.dto.NewsResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NewsFeedService {

    private final WebClient webClient;
    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;

    // private Queue responcQueue;

    public NewsFeedService(WebClient webClient) {
        this.webClient = webClient;
    }

    // public Flux<NewsResponse> getAllNews() {
    //     return webClient.get()
    //                 .uri("/api/feed")
    //                 .retrieve()
    //                 .bodyToFlux(NewsResponse.class);
    // }

    public List<NewsDTO> getAllNews() {
        String correlationId = UUID.randomUUID().toString();
        List<NewsDTO> responce = (List<NewsDTO>) rabbitTemplate.convertSendAndReceive("news.exchange", "", correlationId);

        return responce.stream()
            .map(dto -> new NewsDTO(
                dto.getId(), dto.getHeader(), dto.getBody(), dto.getDate())).toList();
    }

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
}
