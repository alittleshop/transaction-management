package org.example.controller;

import java.net.URI;
import java.util.List;

import org.example.service.TransactionService;
import org.example.vo.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * 交易记录控制器类
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * 创建交易记录
     * @param transaction
     * @return
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        // 检查必填要素
        if (transaction.getAmount() == null || !StringUtils.hasText(transaction.getPayerAccount())
                || !StringUtils.hasText(transaction.getPayeeAccount())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    /**
     * 获取所有交易记录
     * @return
     */
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }


    /**
     * 更新单条记录
     * @param id
     * @param updatedTransaction
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction updatedTransaction) {
        if (updatedTransaction.getAmount() == null || !StringUtils.hasText(updatedTransaction.getPayerAccount())
                || !StringUtils.hasText(updatedTransaction.getPayeeAccount())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.updateTransaction(id, updatedTransaction);
        if (transaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(transaction);
    }

    /**
     * 删除单条记录
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        boolean deleted = transactionService.deleteTransaction(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }
}
