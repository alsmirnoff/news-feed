package com.learning.news_feed_client.configuration;

import java.util.Collections;

import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper.TypePrecedence;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class RabbitMessageConverterConfig {
    // public static MessageConverter createSafeJsonMessageConverter() {
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //     objectMapper.registerModule(new JavaTimeModule());
    //     objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
    //     Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
   
    //     converter.setTypePrecedence(TypePrecedence.INFERRED);
    //     converter.setTypeIdPropertyName("");
    //     converter.setTypeIdMappings(Collections.emptyMap());
        
    //     return converter;
    // }
}
