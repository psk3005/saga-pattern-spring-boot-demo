package com.appsdeveloperblog.payments.service.handler;

import com.appsdeveloperblog.core.dto.Payment;
import com.appsdeveloperblog.core.dto.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.core.events.PaymentFailedEvent;
import com.appsdeveloperblog.core.events.PaymentProcessedEvent;
import com.appsdeveloperblog.core.exceptions.CreditCardProcessorUnavailableException;
import com.appsdeveloperblog.payments.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {"${payments.commands.topic.name}"})
public class PaymentsCommandsHandler {

    private final PaymentService paymentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String paymentEventsTopicName;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PaymentsCommandsHandler(PaymentService paymentService,
                                   KafkaTemplate<String, Object> kafkaTemplate,
                                   @Value("${payments.events.topic.name}") String paymentEventsTopicName) {
        this.paymentService = paymentService;
        this.kafkaTemplate = kafkaTemplate;
        this.paymentEventsTopicName = paymentEventsTopicName;
    }

    @KafkaHandler
    public void handlePaymentsCommandsEvent (@Payload ProcessPaymentCommand event) {
        try {
            Payment payment = new Payment(event.getOrderId(), event.getProductId(), event.getProductPrice(), event.getProductQuantity());
            Payment processedPayment = paymentService.process(payment);

            PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(
                    processedPayment.getOrderId(),
                    processedPayment.getId()
            );

            kafkaTemplate.send(paymentEventsTopicName, paymentProcessedEvent);

        } catch (CreditCardProcessorUnavailableException e) {
            logger.error(e.getLocalizedMessage(), e);
            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(event.getOrderId(), event.getProductId(), event.getProductQuantity());
            kafkaTemplate.send(paymentEventsTopicName, paymentFailedEvent);
        }
    }
}
