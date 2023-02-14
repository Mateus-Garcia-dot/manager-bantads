package com.bantads.manager.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RabbitMqConfiguration.class)
public class ManagerConfiguration {

    public static final String createQueueName = "manager.create";
    public static final String updateQueueName = "manager.update";
    public static final String deleteQueueName = "manager.delete";
    public static final String sortRequestQueueName = "manager.sort-request";
    public static final String sortResponseQueueName = "manager.sort-response";

    @Bean
    public Queue createQueueCreate() {
        return new Queue(createQueueName, true);
    }

    @Bean
    public Queue updateQueueUpdate() {
        return new Queue(updateQueueName, true);
    }

    @Bean
    public Queue deleteQueueDelete() {
        return new Queue(deleteQueueName, true);
    }

    @Bean
    Queue getRequestQueueGetRequest() {
        return new Queue(sortRequestQueueName, true);
    }
    @Bean
    Queue getResponseQueueGetResponse() {
        return new Queue(sortResponseQueueName, true);
    }

    @Bean
    Binding createBinding(Queue createQueueCreate, DirectExchange exchange) {
        return BindingBuilder.bind(createQueueCreate).to(exchange).with(createQueueName);
    }

    @Bean
    Binding updateBinding(Queue updateQueueUpdate, DirectExchange exchange) {
        return BindingBuilder.bind(updateQueueUpdate).to(exchange).with(updateQueueName);
    }

    @Bean
    Binding deleteBinding(Queue deleteQueueDelete, DirectExchange exchange) {
        return BindingBuilder.bind(deleteQueueDelete).to(exchange).with(deleteQueueName);
    }

    @Bean
    Binding getRequestBinding(Queue getRequestQueueGetRequest, DirectExchange exchange) {
        return BindingBuilder.bind(getRequestQueueGetRequest).to(exchange).with(sortRequestQueueName);
    }

    @Bean
    Binding getResponseBinding(Queue getResponseQueueGetResponse, DirectExchange exchange) {
        return BindingBuilder.bind(getResponseQueueGetResponse).to(exchange).with(sortResponseQueueName);
    }
}
