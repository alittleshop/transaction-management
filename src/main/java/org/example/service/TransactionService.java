package org.example.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.example.vo.Transaction;
import org.springframework.stereotype.Service;

/**
 * 交易记录服务类
 * author:kdl
 */
@Service
public class TransactionService {
    // 原子计数器
    private static final AtomicLong idCounter = new AtomicLong(1);

    // 交易记录内存map
    private final Map<Long, Transaction> transactions = new ConcurrentHashMap<>();

    /**
     * 创建交易记录
     * @param transaction
     * @return
     */
    public Transaction createTransaction(Transaction transaction) {
        Long id = idCounter.getAndIncrement();
        transaction.setId(id);
        transaction.setTimestamp(LocalDateTime.now());
        transactions.put(id, transaction);
        return transaction;
    }

    /**
     * 获取交易记录列表
     * @return
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions.values());
    }

    /**
     * 更新对应id值的交易记录
     * @param id
     * @param updatedTransaction
     * @return
     */
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        Transaction existingTransaction = transactions.get(id);
        if (existingTransaction != null) {
            updatedTransaction.setId(id);
            updatedTransaction.setTimestamp(LocalDateTime.now());
            transactions.put(id, updatedTransaction);
            return updatedTransaction;
        }
        return null;
    }

    /**
     * 删除交易记录
     * @param id
     * @return
     */
    public boolean deleteTransaction(Long id) {
        return transactions.remove(id) != null;
    }
}