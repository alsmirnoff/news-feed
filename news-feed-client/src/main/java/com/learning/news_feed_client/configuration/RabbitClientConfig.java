package com.learning.news_feed_client.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper.TypePrecedence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RabbitClientConfig {

    // @Bean
    // public Queue requestAllQueue() {
    //     return new Queue("news.request.all.queue");
    // }

    // @Bean
    // public Queue requestOneQueue() {
    //     return new Queue("news.request.one.queue");
    // }

    // @Bean
    // public Queue createNewsQueue() {
    //     return new Queue("news.create.queue");
    // }

    // @Bean
    // public Queue editNewsQueue() {
    //     return new Queue("news.edit.queue", true);
    // }

    // @Bean
    // public Queue deleteNewsQueue() {
    //     return new Queue("news.delete.queue", true);
    // }

    // @Bean
    // public DirectExchange newsExchange() {
    //     return new DirectExchange("news.exchange");
    // }

    // @Bean
    // public Binding requestBinding(Queue requestAllQueue, DirectExchange exchange) {
    //     return BindingBuilder.bind(requestAllQueue)
    //             .to(exchange)
    //             .with("news.request");
    // }

    // @Bean
    // public Binding bindingRequestOne(Queue requestOneQueue, DirectExchange exchange) {
    //     return BindingBuilder.bind(requestOneQueue)
    //                         .to(exchange)
    //                         .with("news.request");
    // }

    // @Bean
    // public Binding bindingCreateNews(Queue createNewsQueue, DirectExchange exchange) {
    //     return BindingBuilder.bind(createNewsQueue)
    //                         .to(exchange)
    //                         .with("news.create.queue");
    // }

    // @Bean
    // public Binding bindingEditNews(Queue editNewsQueue, DirectExchange exchange) {
    //     return BindingBuilder.bind(editNewsQueue)
    //                         .to(exchange)
    //                         .with("news.edit.queue");
    // }

    // @Bean
    // public Binding bindingDeleteNews(Queue deleteNewsQueue, DirectExchange exchange) {
    //     return BindingBuilder.bind(deleteNewsQueue)
    //                         .to(exchange)
    //                         .with("news.delete.queue");
    // }

    // нужно ли биндить очереди на клиенте, ведь это уже сделал сервер?

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setTypePrecedence(TypePrecedence.INFERRED); // Важно!
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

}
