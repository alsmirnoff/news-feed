package com.learning.news_feed_client.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
    public Queue createNewsQueue() {
        return new Queue("news.create.queue");
    }

    @Bean
    public DirectExchange newsExchange() {
        return new DirectExchange("news.exchange");
    }

    @Bean
    public Binding requestBinding(Queue requestAllQueue, DirectExchange exchange) {
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
    public Binding bindingCreateNews(Queue createNewsQueue, DirectExchange exchange) {
        return BindingBuilder.bind(createNewsQueue)
                            .to(exchange)
                            .with("news.create.queue");
    }

    // нужно ли биндить очереди на клиенте, ведь это уже сделал сервер?

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
