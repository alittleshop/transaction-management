package service;

import com.entity.vo.Transaction;
import com.dao.TransactionRepository;
import com.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // 测试创建交易记录 - 正常情况
    @Test
    public void testCreateTransaction_Success() {
        Transaction transaction = new Transaction();
        transaction.setTransactionSerialNo("123456"); // 设置交易流水号
        transaction.setDescription("Description"); // 设置交易描述
        transaction.setAmount(new BigDecimal("100")); // 设置金额
        transaction.setPayerAccount("12345"); // 设置付款账号
        transaction.setPayerName("kdl"); // 设置付款人姓名
        transaction.setPayeeAccount("67890"); // 设置收款账号
        transaction.setPayeeName("kdl"); // 设置收款人姓名
        transaction.setPaymentMethod("Cash"); // 设置交易方式

        Pageable pageable = PageRequest.of(0, 1);
        when(transactionRepository.findListByTransactionSerialNo(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction createdTransaction = transactionServiceImpl.createTransaction(transaction);

        assertNotNull(createdTransaction);
        assertEquals("Description", createdTransaction.getDescription());
        assertNotNull(createdTransaction.getTimestamp());
        assertNotNull(createdTransaction.getUpdateTime());
    }

    // 测试创建交易记录 - 交易流水号已存在，抛出异常
    @Test
    public void testCreateTransaction_DuplicateSerialNo() {
        Transaction transaction = new Transaction();
        transaction.setTransactionSerialNo("123456"); // 设置交易流水号
        transaction.setDescription("Description"); // 设置交易描述
        transaction.setAmount(new BigDecimal("100")); // 设置金额
        transaction.setPayerAccount("12345"); // 设置付款账号
        transaction.setPayerName("kdl"); // 设置付款人姓名
        transaction.setPayeeAccount("67890"); // 设置收款账号
        transaction.setPayeeName("kdl"); // 设置收款人姓名
        transaction.setPaymentMethod("Cash"); // 设置交易方式
        Pageable pageable = PageRequest.of(0, 1);
        List<Transaction> existingTransactions = new ArrayList<>();
        existingTransactions.add(new Transaction());
        Page<Transaction> page = new PageImpl<>(existingTransactions);
        when(transactionRepository.findListByTransactionSerialNo(anyString(), any(Pageable.class))).thenReturn(page);

        assertThrows(RuntimeException.class, () -> {
            transactionServiceImpl.createTransaction(transaction);
        });
    }

    // 测试分页获取交易记录列表 - 无流水号
    @Test
    public void testGetTransactionsPage_NoSerialNo() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        Page<Transaction> expectedPage = new PageImpl<>(transactions);
        when(transactionRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Transaction> resultPage = transactionServiceImpl.getTransactionsPage(null, pageable);

        assertEquals(expectedPage, resultPage);
    }

    // 测试分页获取交易记录列表 - 有流水号
    @Test
    public void testGetTransactionsPage_WithSerialNo() {
        String serialNo = "12345";
        Pageable pageable = PageRequest.of(0, 10);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        Page<Transaction> expectedPage = new PageImpl<>(transactions);
        when(transactionRepository.findListByTransactionSerialNo(anyString(), any(Pageable.class))).thenReturn(expectedPage);

        Page<Transaction> resultPage = transactionServiceImpl.getTransactionsPage(serialNo, pageable);

        assertEquals(expectedPage, resultPage);
    }

    // 测试更新交易记录 - 记录存在
    @Test
    public void testUpdateTransaction_RecordExists() {
        Long id = 1L;
        Transaction existingTransaction = new Transaction();
        existingTransaction.setTransactionSerialNo("123456"); // 设置交易流水号
        existingTransaction.setDescription("Description"); // 设置交易描述
        existingTransaction.setAmount(new BigDecimal("100")); // 设置金额
        existingTransaction.setPayerAccount("12345"); // 设置付款账号
        existingTransaction.setPayerName("kdl"); // 设置付款人姓名
        existingTransaction.setPayeeAccount("67890"); // 设置收款账号
        existingTransaction.setPayeeName("kdl"); // 设置收款人姓名
        existingTransaction.setPaymentMethod("Cash"); // 设置交易方式
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionSerialNo("123456"); // 设置交易流水号
        updatedTransaction.setDescription("New Description"); // 设置交易描述
        updatedTransaction.setAmount(new BigDecimal("200")); // 设置金额
        updatedTransaction.setPayerAccount("67890"); // 设置付款账号
        updatedTransaction.setPayerName("kdl"); // 设置付款人姓名
        updatedTransaction.setPayeeAccount("123456"); // 设置收款账号
        updatedTransaction.setPayeeName("kdl"); // 设置收款人姓名
        updatedTransaction.setPaymentMethod("Cash"); // 设置交易方式
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(existingTransaction);

        Transaction result = transactionServiceImpl.updateTransaction(id, updatedTransaction);

        assertNotNull(result);
        assertEquals("New Description", result.getDescription());
        assertEquals(new BigDecimal("200"), result.getAmount());
        assertEquals("67890", result.getPayerAccount());
        assertEquals("123456", result.getPayeeAccount());
        assertNotNull(result.getUpdateTime());
    }

    // 测试更新交易记录 - 记录不存在
    @Test
    public void testUpdateTransaction_RecordNotExists() {
        Long id = 1L;
        Transaction transaction = new Transaction();
        transaction.setTransactionSerialNo("123456"); // 设置交易流水号
        transaction.setDescription("Description"); // 设置交易描述
        transaction.setAmount(new BigDecimal("100")); // 设置金额
        transaction.setPayerAccount("12345"); // 设置付款账号
        transaction.setPayerName("kdl"); // 设置付款人姓名
        transaction.setPayeeAccount("67890"); // 设置收款账号
        transaction.setPayeeName("kdl"); // 设置收款人姓名
        transaction.setPaymentMethod("Cash"); // 设置交易方式
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        Transaction result = transactionServiceImpl.updateTransaction(id, transaction);

        assertNull(result);
    }

    // 测试删除交易记录 - 记录存在
    @Test
    public void testDeleteTransaction_RecordExists() {
        Long id = 1L;
        when(transactionRepository.existsById(anyLong())).thenReturn(true);

        boolean deleted = transactionServiceImpl.deleteTransaction(id);

        assertTrue(deleted);
        verify(transactionRepository, times(1)).deleteById(id);
    }

    // 测试删除交易记录 - 记录不存在
    @Test
    public void testDeleteTransaction_RecordNotExists() {
        Long id = 1L;
        when(transactionRepository.existsById(anyLong())).thenReturn(false);

        boolean deleted = transactionServiceImpl.deleteTransaction(id);

        assertFalse(deleted);
        verify(transactionRepository, never()).deleteById(id);
    }
}