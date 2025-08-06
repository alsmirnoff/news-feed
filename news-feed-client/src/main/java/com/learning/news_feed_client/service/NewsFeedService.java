package com.learning.news_feed_client.service;

import java.text.NumberFormat.Style;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessageProperties;
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

    private WebClient webClient;
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.news.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.queue.ttl}")
    private int queueTtl;
    
    @Value("${rabbitmq.message.timeout}")
    private int messageResponseTimeout;

    @Value("${rabbitmq.request.all.routingKey}")
    private String requestAllRoutingKey;

    @Value("${rabbitmq.request.one.routingKey}")
    private String requestOneRoutingKey;

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
    public List<NewsDTO> getAllNews() throws NewsServiceUnavailableException {
        String correlationId = "req_" + UUID.randomUUID();
        String responseQueueName = createTemporaryQueue(queueTtl);

        System.out.println("responseQueueName: " + responseQueueName);

        try {
            rabbitTemplate.convertAndSend(
                requestAllRoutingKey,
                correlationId,
                message -> {
                    MessageProperties props = message.getMessageProperties();
                    props.setReplyTo(responseQueueName);
                    props.setExpiration(String.valueOf(messageResponseTimeout));
                    return message;
                }
            );

            List<NewsDTO> news = rabbitTemplate.receiveAndConvert(
                responseQueueName,
                messageResponseTimeout,
                new ParameterizedTypeReference<List<NewsDTO>>() {}
            );

            if(news == null) {
                throw new NewsServiceUnavailableException("No response from RabbitMQ within " + messageResponseTimeout + " ms");
            }
            return news;
        } catch (AmqpException e){
            throw new RuntimeException("RabbitMQ error: " + e.getMessage(), e);
        }
    }

    // public Mono<NewsResponse> getNewsById(Long id) {
    //     return webClient.get()
    //             .uri("/api/feed/{id}", id)
    //             .retrieve()
    //             .bodyToMono(NewsResponse.class);
    // }

    public NewsDTO getNewsById(int id) throws NewsServiceUnavailableException {
        //String correlationId = "req_" + UUID.randomUUID();
        String responseQueueName = createTemporaryQueue(queueTtl);

        try {
            rabbitTemplate.convertAndSend(
                requestOneRoutingKey,
                id,
                message -> {
                    MessageProperties props = message.getMessageProperties();
                    props.setReplyTo(responseQueueName);
                    props.setExpiration(String.valueOf(messageResponseTimeout));
                    return message;
                }
            );

            NewsDTO news = rabbitTemplate.receiveAndConvert(
                responseQueueName,
                messageResponseTimeout,
                new ParameterizedTypeReference<NewsDTO>() {}
            );

            if(news == null) {
                throw new NewsServiceUnavailableException("No response from RabbitMQ within " + messageResponseTimeout + " ms");
            }
            return news;
        } catch (AmqpException e){
            throw new RuntimeException("RabbitMQ error: " + e.getMessage(), e);
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

    // private String createResponseQueue() {
    //     return rabbitTemplate.execute(channel -> {
    //        String responseQueueName = "client.response." + UUID.randomUUID().toString();
    //        channel.queueDeclare(responseQueueName, false, true, true, null);
    //        channel.queueBind(responseQueueName, "news.exchange", "news.response.*");
    //        return responseQueueName; 
    //     });
    // }

    // private void deleteResponseQueue(String responseQueueName) {
    //     rabbitTemplate.execute(channel -> {
    //         channel.queueDelete(responseQueueName);
    //         return null;
    //     });
    // }

    private String createTemporaryQueue(int ttlMs) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-expires", ttlMs);
        return rabbitTemplate.execute(channel -> 
            channel.queueDeclare("", false, false, true, args).getQueue()
        );
    }
}
