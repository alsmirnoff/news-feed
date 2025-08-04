package com.learning.news_feed_client.configuration;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitClientConfig {

    @Bean
    public Queue clientResponceQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding bindingResponce(Queue clientResponceQueue, TopicExchange exchange) {
        return BindingBuilder.bind(clientResponceQueue)
                            .to(exchange)
                            .with("news.responce.*");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
