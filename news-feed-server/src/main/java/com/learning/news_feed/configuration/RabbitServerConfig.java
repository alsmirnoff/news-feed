package com.learning.news_feed.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitServerConfig {
    
    @Bean
    public Queue requestAllQueue() {
        return new Queue("news.request.all", true);
    }

    @Bean
    public Queue requestOneQueue() {
        return new Queue("news.request.one", true);
    }

    @Bean
    public DirectExchange newsExchange() {
        return new DirectExchange("news.exchange");
    }

    @Bean
    public Binding bindingRequestAll(Queue requestAllQueue, DirectExchange exchange) {
        return BindingBuilder.bind(requestAllQueue)
                            .to(exchange)
                            .with("news.request");
    }

    @Bean
    public Binding bindingRequestOne(Queue requestOneQueue, DirectExchange exchange) {
        return BindingBuilder.bind(requestOneQueue)
                            .to(exchange)
                            .with("news.request");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
