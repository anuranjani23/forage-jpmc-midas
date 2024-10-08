package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${general.incentive-api-url}")
    private String incentiveApiUrl;

    @Transactional
    public boolean processTheTransaction(long senderId, long recipientId, float amount) {
        UserRecord sender = userRepository.findById(senderId);
        UserRecord recipient = userRepository.findById(recipientId);

        if (sender == null || recipient == null) {
            return false;
        }
        if (sender.getBalance() < amount) {
            System.out.println("Transaction failed: Sender " + sender.getName() + " has insufficient balance.");
            return false;
        }
        sender.setBalance(sender.getBalance() - amount);

        Transaction transaction = new Transaction(senderId, recipientId, amount);

        ResponseEntity<Incentive> response = restTemplate.postForEntity(incentiveApiUrl, transaction, Incentive.class);

        // To get the response and incentive amount safely, while handling edge case error
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Incentive incentiveResponse = response.getBody();

            float incentiveAmount = incentiveResponse.getAmount();

            System.out.println("Incentive amount received: " + incentiveAmount);

            recipient.setBalance(recipient.getBalance() + amount + incentiveAmount);

            System.out.println("Transaction successful!");
            System.out.println("Sender: " + sender.getName() + " | New Balance: " + sender.getBalance());
            System.out.println("Recipient: " + recipient.getName() + " | New Balance: " + recipient.getBalance());
            System.out.println("Incentive: " + incentiveAmount);

            userRepository.save(sender);
            userRepository.save(recipient);

            TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, amount, incentiveAmount);
            transactionRepository.save(transactionRecord);

        } else {
            System.out.println("Failed to get incentive from API. Status code: " + response.getStatusCode());
            if (response.getBody() == null) {
                System.out.println("Response body is null.");
            }
            return false;
        }
        return true;
    }
}
