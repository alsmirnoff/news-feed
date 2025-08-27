package com.learning.news_feed.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper.TypePrecedence;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learning.news_feed.dto.NewsDTO;

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

    // @Bean
    // public Binding newsBinding() {
    //     return BindingBuilder.bind(newsQueue())
    //             .to(newsExchange())
    //             .with("news.*");
    // }

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

    // @Bean
    // public MessageConverter jsonMessageConverter() {
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //     objectMapper.registerModule(new JavaTimeModule());
    //     objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
    //     Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
    //     converter.setTypePrecedence(TypePrecedence.INFERRED);
    //     return converter;
    // }

    // @Bean
    // public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
    //     RabbitTemplate template = new RabbitTemplate(connectionFactory);
    //     template.setMessageConverter(jsonMessageConverter);
    //     return template;
    // }

    // @Bean
    // public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
    //     SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    //     factory.setConnectionFactory(connectionFactory);
    //     factory.setMessageConverter(jsonMessageConverter);
    //     return factory;
    // }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("news.dto", NewsDTO.class);
        
        converter.setTypeIdMappings(idClassMapping);
        converter.setTypePrecedence(TypePrecedence.TYPE_ID);
        
        return converter;
    }
}
