package com.data.vo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@AllArgsConstructor
@Data
@Entity
@Table(indexes = {@Index(name = "idx_transaction_serial_no", columnList = "transaction_serial_no", unique = true)})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id自增主键
    private String transactionSerialNo; // 银行交易流水号，唯一键
    private String description;  // 交易描述
    private BigDecimal amount;  // 金额
    private LocalDateTime timestamp;  // 交易时间
    private String payerAccount; // 付款账号
    private String payeeAccount; // 收款账号
    private String paymentMethod; // 交易方式
    private LocalDateTime updateTime; // 更新时间

}