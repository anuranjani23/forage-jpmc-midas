package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    private final TransactionService transactionService;

    public TransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @KafkaListener(topics = {"topic"}, groupId = "my-consumer-group")
    public void listen(Transaction transaction) {
        boolean isValid = transactionService.processTheTransaction(transaction.getSenderId(),
                transaction.getRecipientId(), transaction.getAmount());
        if (!isValid) {
            System.out.println("Invalid transaction from senderId: " + transaction.getSenderId());
        } else {
            System.out.println("Transaction processed from senderId: " + transaction.getSenderId());
        }
    }
}
