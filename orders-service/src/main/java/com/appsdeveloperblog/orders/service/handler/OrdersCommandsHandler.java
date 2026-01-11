package com.appsdeveloperblog.orders.service.handler;

import com.appsdeveloperblog.core.dto.commands.ApprovedOrderCommand;
import com.appsdeveloperblog.orders.service.OrderService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${orders.commands.topic.name}")
public class OrdersCommandsHandler {

    private final OrderService orderService;

    public OrdersCommandsHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaHandler
    public void handleCommand(@Payload ApprovedOrderCommand approvedOrderCommand) {
        orderService.approveOrder(approvedOrderCommand.getOrderId());

    }
}
