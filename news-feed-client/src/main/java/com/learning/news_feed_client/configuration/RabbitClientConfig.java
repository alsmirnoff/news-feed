package com.learning.news_feed_client.configuration;

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
    public Queue requestAllQueue() {
        return new Queue("news.request.all");
    }

    @Bean
    public Queue requestOneQueue() {
        return new Queue("news.request.one");
    }

    @Bean
    public TopicExchange newsExchange() {
        return new TopicExchange("news.exchange");
    }

    @Bean
    public Binding requestBinding(Queue requestAllQueue, TopicExchange exchange) {
        return BindingBuilder.bind(requestAllQueue)
                .to(exchange)
                .with("news.request");
    }

    @Bean
    public Binding bindingRequestOne(Queue requestOneQueue, TopicExchange exchange) {
        return BindingBuilder.bind(requestOneQueue)
                            .to(exchange)
                            .with("news.request");
    }

    // нужно ли биндить очереди на клиенте, ведь это уже сделал сервер?

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
