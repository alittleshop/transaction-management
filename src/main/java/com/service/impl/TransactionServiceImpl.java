package com.service.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dao.TransactionRepository;
import com.service.ITransactionService;
import com.data.vo.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交易记录服务类
 * author:kdl
 */
@Service
public class TransactionServiceImpl implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;


    // 交易记录内存map
    private final Map<Long, Transaction> transactions = new ConcurrentHashMap<>();

    /**
     * 创建交易记录
     * @param transaction
     * @return
     */
    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        transaction.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }


    /**
     * 分页获取交易记录列表
     * @param transactionSerialNo 流水号
     * @param pageable 分页参数
     * @return
     */
    public Page<Transaction> getTransactionsPage(String transactionSerialNo, Pageable pageable) {
        if(StringUtils.isBlank(transactionSerialNo)){
            return transactionRepository.findAll(pageable);
        }else{
            return transactionRepository.findListByTransactionSerialNo(transactionSerialNo, pageable);
        }
    }

    /**
     * 更新对应id值的交易记录
     * @param id
     * @param updatedTransaction
     * @return
     */
    @Transactional
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setDescription(updatedTransaction.getDescription());
            transaction.setAmount(updatedTransaction.getAmount());
            transaction.setPayerAccount(updatedTransaction.getPayerAccount());
            transaction.setPayeeAccount(updatedTransaction.getPayeeAccount());
            transaction.setPaymentMethod(updatedTransaction.getPaymentMethod());
            transaction.setUpdateTime(LocalDateTime.now());
            return transactionRepository.save(transaction);
        }).orElse(null);
    }

    /**
     * 删除交易记录
     * @param id
     * @return
     */
    @Transactional
    public boolean deleteTransaction(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}