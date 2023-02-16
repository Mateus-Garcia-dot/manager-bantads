package com.bantads.manager.config;

import org.springframework.amqp.core.*;
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
    public static final String delayedSortRequestQueueName = "manager.delayed-sort-request";
    public static final String sortResponseQueueName = "manager.sort-response";
    public static final String delayedSortResponseQueueName = "manager.delayed-sort-response";

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
    public Queue sortRequestQueue() {
        return QueueBuilder.durable(sortRequestQueueName)
                .withArgument("x-dead-letter-exchange", RabbitMqConfiguration.exchangeName)
                .withArgument("x-dead-letter-routing-key", delayedSortRequestQueueName)
                .build();
    }

    @Bean
    public Queue delayedSortRequestQueue() {
        return QueueBuilder.durable(delayedSortRequestQueueName)
                .withArgument("x-dead-letter-exchange", RabbitMqConfiguration.exchangeName)
                .withArgument("x-dead-letter-routing-key", sortRequestQueueName)
                .withArgument("x-message-ttl", 5000)
                .build();
    }

    @Bean
    public Queue sortResponseQueue() {
        return QueueBuilder.durable(sortResponseQueueName)
                .withArgument("x-dead-letter-exchange", RabbitMqConfiguration.exchangeName)
                .withArgument("x-dead-letter-routing-key", delayedSortResponseQueueName)
                .build();
    }

    @Bean
    public Queue delayedSortResponseQueue() {
        return QueueBuilder.durable(delayedSortResponseQueueName)
                .withArgument("x-dead-letter-exchange", RabbitMqConfiguration.exchangeName)
                .withArgument("x-dead-letter-routing-key", sortResponseQueueName)
                .withArgument("x-message-ttl", 5000)
                .build();
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
    Binding sortRequestBinding(Queue sortRequestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(sortRequestQueue).to(exchange).with(sortRequestQueueName);
    }

    @Bean
    Binding sortResponseBinding(Queue sortResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(sortResponseQueue).to(exchange).with(sortResponseQueueName);
    }

    @Bean
    Binding delayedSortResponseBinding(Queue delayedSortResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(delayedSortResponseQueue).to(exchange).with(delayedSortResponseQueueName);
    }

    @Bean
    Binding delayedSortRequestBinding(Queue delayedSortRequestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(delayedSortRequestQueue).to(exchange).with(delayedSortRequestQueueName);
    }

}
