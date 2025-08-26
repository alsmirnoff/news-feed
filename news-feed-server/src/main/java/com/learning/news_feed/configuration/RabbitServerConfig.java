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
    public TopicExchange newsExchange() {
        return new TopicExchange("news.exchange");
    }

    @Bean
    public Queue newsQueue() {
        return new Queue("news.queue");
    }

    @Bean
    public Binding createBinding() {
        return BindingBuilder.bind(newsQueue())
                .to(newsExchange())
                .with("news.create");
    }

    @Bean
    public Binding getBinding() {
        return BindingBuilder.bind(newsQueue())
                .to(newsExchange())
                .with("news.get");
    }

    @Bean
    public Binding getAllBinding() {
        return BindingBuilder.bind(newsQueue())
                .to(newsExchange())
                .with("news.get.all");
    }

    @Bean
    public Binding updateBinding() {
        return BindingBuilder.bind(newsQueue())
                .to(newsExchange())
                .with("news.update");
    }

    @Bean
    public Binding deleteBinding() {
        return BindingBuilder.bind(newsQueue())
                .to(newsExchange())
                .with("news.delete");
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
