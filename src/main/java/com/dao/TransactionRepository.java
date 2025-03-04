package com.dao;
import com.data.vo.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * 交易记录分页查询
     * @param pageable 分页参数
     * @return
     */
    Page<Transaction> findAll(Pageable pageable);

    /**
     * 根据交易流水号搜索记录
     * @param transactionSerialNo 交易流水号
     * @param pageable 分页参数
     * @return
     */
    Page<Transaction> findListByTransactionSerialNo(String transactionSerialNo, Pageable pageable);

}