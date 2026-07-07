package com.innovarib.brainos.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EVENTS_EXCHANGE = "brain-os.events";

    @Bean
    TopicExchange brainOsEventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE, true, false);
    }
}
