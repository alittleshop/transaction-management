package com.service;

import com.entity.vo.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ITransactionService {

    /**
     * 创建交易记录
     * @param transaction
     * @return
     */
    Transaction createTransaction(Transaction transaction);


    /**
     * 分页获取交易记录列表
     * @param transactionSerialNo 流水号
     * @param pageable 分页参数
     * @return
     */
    Page<Transaction> getTransactionsPage(String transactionSerialNo, Pageable pageable);

    /**
     * 更新对应id值的交易记录
     * @param id
     * @param updatedTransaction
     * @return
     */
    Transaction updateTransaction(Long id, Transaction updatedTransaction);

    /**
     * 删除交易记录
     * @param id
     * @return
     */
    boolean deleteTransaction(Long id);
}
