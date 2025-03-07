package com.service.impl;

import java.time.LocalDateTime;

import com.dao.TransactionRepository;
import com.service.ITransactionService;
import com.entity.vo.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 交易记录服务类
 * author:kdl
 */
@Service
public class TransactionServiceImpl implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * 创建交易记录
     * @param transaction
     * @return
     */
    @CacheEvict(value="transactions", allEntries = true)
    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        Pageable pageable = PageRequest.of(0, 1);
        // 判断交易流水号是否存在，用于判断是否交易重复
        Page<Transaction> page =  transactionRepository.findListByTransactionSerialNo(transaction.getTransactionSerialNo(), pageable);
        if(!CollectionUtils.isEmpty(page.getContent())){
            throw new RuntimeException("银行交易流水号已经存在 : " + transaction.getTransactionSerialNo());
        }
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setUpdateTime(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }


    /**
     * 分页获取交易记录列表
     * @param transactionSerialNo 流水号
     * @param pageable 分页参数
     * @return
     */
    @Cacheable(value="transactions",
            key="#transactionSerialNo !=null ? #transactionSerialNo + '&' + #pageable.pageNumber + '&' + #pageable.pageSize" +
                    ": #pageable.pageNumber + '&' + #pageable.pageSize",
            unless= "#result == null"
    )
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
    @CacheEvict(value="transactions", allEntries = true)
    @Transactional
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setDescription(updatedTransaction.getDescription());
            transaction.setAmount(updatedTransaction.getAmount());
            transaction.setPayerAccount(updatedTransaction.getPayerAccount());
            transaction.setPayerName(updatedTransaction.getPayerName());
            transaction.setPayeeAccount(updatedTransaction.getPayeeAccount());
            transaction.setPayeeName(updatedTransaction.getPayeeName());
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
    @CacheEvict(value="transactions", allEntries = true)
    @Transactional
    public boolean deleteTransaction(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}