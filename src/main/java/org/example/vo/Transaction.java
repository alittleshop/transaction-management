package org.example.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录实体类
 * author:kdl
 */
public class Transaction {
    private Long id; // id主键
    private String description;  //交易描述
    private BigDecimal amount;  // 金额
    private LocalDateTime timestamp;  // 交易时间
    private String payerAccount; // 付款账号
    private String payeeAccount; // 收款账号
    private String paymentMethod; // 交易方式

    public Transaction(Long id, String description, BigDecimal amount, String payerAccount, String payeeAccount, String paymentMethod) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.payerAccount = payerAccount;
        this.payeeAccount = payeeAccount;
        this.paymentMethod = paymentMethod;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod){
        this.paymentMethod=paymentMethod;
    }
}