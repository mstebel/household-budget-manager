package pl.ms.householdbudget.model;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class Transaction {
    private Integer id;
    private TransactionType type;
    private String description;
    private double amount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public Transaction(int id, TransactionType type, String description, double amount, LocalDate date) {
        this (type, description, amount, date);
        this.id = id;
    }

    public Transaction() {
    }

    public Transaction(TransactionType type, String description, double amount, LocalDate date) {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("typ: %s, id: %d, opis: %s, kwota: %.2fpln, data: %s",
                type.getDescription(), id, description, amount, date.toString());
    }
}
