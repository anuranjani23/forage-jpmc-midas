package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import org.springframework.aot.generate.Generated;


@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId", nullable = false)
    private UserRecord sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipientId", nullable = false)
    private UserRecord recipient;

    @Column(nullable = false)
    private float amount;

    private float incentive;

    protected TransactionRecord(){}

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
    }

    public long getId() {
        return Id;
    }

    public UserRecord getSender() {
        return sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public float getAmount() {
        return amount;
    }
    public float getIncentive() {
        return incentive;
    }

    @Override
    public String toString() {
        return "TransactionRecord{sender=" + sender.getName() + ", recipient=" + recipient.getName() +
                ", amount=" + amount + ", incentive=" + incentive + '}';
    }

}
