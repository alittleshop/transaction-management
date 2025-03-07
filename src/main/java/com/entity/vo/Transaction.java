package com.entity.vo;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 交易记录实体类
 * author:kdl
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(indexes = {@Index(name = "idx_transaction_serial_no", columnList = "transaction_serial_no", unique = true)})
public class Transaction {
    /**
     * id自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 银行交易流水号，唯一键
     */
    @NotBlank
    private String transactionSerialNo;

    /**
     * 交易描述
     */
    private String description;

    /**
     * 金额
     */
    @DecimalMin(value="0.0", inclusive = false, message = "金额应该大于0")
    private BigDecimal amount;

    /**
     * 交易时间
     */
    private LocalDateTime timestamp;

    /**
     * 付款账号
     */
    @NotBlank(message = "付款账号不能为空")
    private String payerAccount;

    /**
     * 付款人姓名
     */
    private String payerName;

    /**
     * 收款账号
     */
    @NotBlank(message = "收款账号不能为空")
    private String payeeAccount;

    /**
     * 收款人姓名
     */
    private String payeeName;

    /**
     * 交易方式
     */
    private String paymentMethod;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}