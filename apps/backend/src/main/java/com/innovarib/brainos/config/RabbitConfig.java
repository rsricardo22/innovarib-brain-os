package com.innovarib.brainos.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EVENTS_EXCHANGE = "brainos.events";

    @Bean
    TopicExchange brainOsEventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE, true, false);
    }
}
