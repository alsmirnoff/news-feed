package com.learning.news_feed.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitServerConfig {
    
    @Bean
    public Queue newsCrudQueue() {
        return new Queue("news.crud.queue", true);
    }

    // @Bean
    // public TopicExchange newsExchange() {
    //     return new TopicExchange("news.topic.exchange");
    // }

    // @Bean
    // public Binding bindingRequestAll(Queue requestAllQueue, DirectExchange exchange) {
    //     return BindingBuilder.bind(requestAllQueue)
    //                         .to(exchange)
    //                         .with("news.request");
    // }

    // @Bean
    // public Binding bindingRequestOne(Queue requestOneQueue, DirectExchange exchange) {
    //     return BindingBuilder.bind(requestOneQueue)
    //                         .to(exchange)
    //                         .with("news.request");
    // }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
