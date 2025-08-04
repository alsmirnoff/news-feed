package com.learning.news_feed.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    
    @Bean
    public Queue newsRequestQueue() {
        return new Queue("news.request.queue", true);
    }

    @Bean
    public Queue newsResponceQueue() {
        return new Queue("news.responce.queue", true);
    }

    @Bean
    public TopicExchange newsExchange() {
        return new TopicExchange("news.exchange");
    }

    @Bean
    public Binding bindingRequest(Queue newsRequestQueue, TopicExchange exchange) {
        return BindingBuilder.bind(newsRequestQueue)
                            .to(exchange)
                            .with("news.request");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
