package com.learning.news_feed_client.service;

import java.text.NumberFormat.Style;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.learning.news_feed_client.dto.NewsDTO;
import com.learning.news_feed_client.dto.NewsRequest;
import com.learning.news_feed_client.dto.NewsResponse;
import com.learning.news_feed_client.exception.NewsServiceUnavailableException;
import com.rabbitmq.client.AMQP;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NewsFeedService {

    // private WebClient webClient;
    private RabbitTemplate rabbitTemplate;

    public NewsFeedService(RabbitTemplate rabbitTemplate){
        // REST
        //this.webClient = webClient;

        // rabbitMQ
        this.rabbitTemplate = rabbitTemplate;
    }

// REST get all
    // public Flux<NewsResponse> getAllNewsRest() {
    //     return webClient.get()
    //                 .uri("/api/feed")
    //                 .retrieve()
    //                 .bodyToFlux(NewsResponse.class);
    // }

// REST get one
    // public Mono<NewsResponse> getNewsById(Long id) {
    //     return webClient.get()
    //             .uri("/api/feed/{id}", id)
    //             .retrieve()
    //             .bodyToMono(NewsResponse.class);
    // }

// REST create
    // public Mono<NewsResponse> createNews(NewsRequest request) {
    //     return webClient.post()
    //             .uri("/api/create")
    //             .bodyValue(request)
    //             .retrieve()
    //             .bodyToMono(NewsResponse.class);
    // }

// REST delete
    // public Mono<Void> deleteNews(Long id) {
    //     return webClient.post()
    //             .uri("/api/delete/{id}", id)
    //             .retrieve()
    //             .bodyToMono(Void.class);
    // }

// ================================================================

// rabbitMQ SYNC RPC

// rabbitMQ get all
    // очередь для Direct Reply-To устанавливается автоматически Spring AMQP
    // public List<NewsDTO> getAllNews() {
    //     return rabbitTemplate.convertSendAndReceiveAsType(
    //         "news.request.all.queue", 
    //         "", 
    //         new ParameterizedTypeReference<List<NewsDTO>>() {});
    // }

// rabbitMQ get one
    public NewsDTO getNewsById(int id) {
        return rabbitTemplate.convertSendAndReceiveAsType(
            "news.request.one.queue", 
            id,
            new ParameterizedTypeReference<NewsDTO>() {});
    }

// rabbitMQ create
   public NewsDTO createNews(NewsDTO request) {
        return rabbitTemplate.convertSendAndReceiveAsType(
            "news.create.queue",
            request,
            new ParameterizedTypeReference<NewsDTO>() {});
   }

// rabbitMQ edit
   public NewsDTO editNews(NewsDTO request) {
        return rabbitTemplate.convertSendAndReceiveAsType(
            "news.edit.queue",
            request,
            new ParameterizedTypeReference<NewsDTO>() {});
   }

// rabbitMQ delete
    public void deleteNews(int id) {
        rabbitTemplate.convertSendAndReceive(
            "news.delete.queue", 
            id);
    }

// ================================================================

// rabbitMQ ASYNC RPC

// rabbitMQ get all async
    public List<NewsDTO> getAllNews() {
        return rabbitTemplate.convertSendAndReceiveAsType(
            "news.request.all.queue", 
            "", 
            new ParameterizedTypeReference<List<NewsDTO>>() {});
    }

    public CompletableFuture<List<NewsDTO>> getAllNewsAsync(){
        return CompletableFuture.supplyAsync(() -> rabbitTemplate.convertSendAndReceiveAsType(
                "news.requests.exchange",
                "",
                new ParameterizedTypeReference<List<NewsDTO>>() {}
            )
        );
    }

}
