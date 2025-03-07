package controller;

import com.controller.TransactionController;
import com.entity.BaseResponse;
import com.entity.ErrorCodeEnum;
import com.entity.vo.Transaction;
import com.service.ITransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private ITransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 测试创建交易记录 - 正常情况
     */
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

        when(transactionService.createTransaction(transaction)).thenReturn(transaction);

        BaseResponse<Transaction> response = transactionController.createTransaction(transaction);

        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals("success", response.getMessage());
        assertEquals(transaction, response.getData());
    }

    /**
     * 测试创建交易记录 - 异常情况
     */
    @Test
    public void testCreateTransaction_Exception() {
        Transaction transaction = new Transaction();
        transaction.setTransactionSerialNo("123456"); // 设置交易流水号
        transaction.setDescription("Description"); // 设置交易描述
        transaction.setAmount(new BigDecimal("100")); // 设置金额
        transaction.setPayerAccount("12345"); // 设置付款账号
        transaction.setPayerName("kdl"); // 设置付款人姓名
        transaction.setPayeeAccount("67890"); // 设置收款账号
        transaction.setPayeeName("kdl"); // 设置收款人姓名
        transaction.setPaymentMethod("Cash"); // 设置交易方式

        when(transactionService.createTransaction(transaction)).thenThrow(new RuntimeException("银行交易流水号已经存在 :" +transaction.getTransactionSerialNo()));

        BaseResponse<Transaction> response = transactionController.createTransaction(transaction);

        assertEquals(ErrorCodeEnum.SYSTEM_ERROR.getCode(), response.getCode());
        assertEquals("银行交易流水号已经存在 :123456", response.getMessage());
    }

    /**
     * 测试获取所有交易记录 - 正常情况
     */
    @Test
    public void testGetTransactionsPage_Success() {
        String transactionSerialNo = null;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Transaction> mockPage = new PageImpl<>(Collections.emptyList());

        when(transactionService.getTransactionsPage(transactionSerialNo, pageable)).thenReturn(mockPage);

        BaseResponse<Page<Transaction>> response = transactionController.getTransactionsPage(transactionSerialNo, page, size);

        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals("success", response.getMessage());
        assertEquals(mockPage, response.getData());
    }

    /**
     * 测试获取所有交易记录 - 异常情况
     */
    @Test
    public void testGetTransactionsPage_Exception() {
        String transactionSerialNo = null;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        when(transactionService.getTransactionsPage(transactionSerialNo, pageable)).thenThrow(new RuntimeException("Test Exception"));

        BaseResponse<Page<Transaction>> response = transactionController.getTransactionsPage(transactionSerialNo, page, size);

        assertEquals(ErrorCodeEnum.SYSTEM_ERROR.getCode(), response.getCode());
        assertEquals("系统繁忙，请稍后再试", response.getMessage());
    }

    /**
     * 测试更新单条记录 - 正常情况
     */
    @Test
    public void testUpdateTransaction_Success() {
        Long id = 1L;
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionSerialNo("123456"); // 设置交易流水号
        updatedTransaction.setDescription("Description"); // 设置交易描述
        updatedTransaction.setAmount(new BigDecimal("100")); // 设置金额
        updatedTransaction.setPayerAccount("12345"); // 设置付款账号
        updatedTransaction.setPayerName("kdl"); // 设置付款人姓名
        updatedTransaction.setPayeeAccount("67890"); // 设置收款账号
        updatedTransaction.setPayeeName("kdl"); // 设置收款人姓名
        updatedTransaction.setPaymentMethod("Cash"); // 设置交易方式

        when(transactionService.updateTransaction(id, updatedTransaction)).thenReturn(updatedTransaction);

        BaseResponse<Transaction> response = transactionController.updateTransaction(id, updatedTransaction);

        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals("success", response.getMessage());
        assertEquals(updatedTransaction, response.getData());
    }

    /**
     * 测试更新单条记录 - 记录不存在
     */
    @Test
    public void testUpdateTransaction_RecordNotExists() {
        Long id = 1L;
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionSerialNo("123456"); // 设置交易流水号
        updatedTransaction.setDescription("Description"); // 设置交易描述
        updatedTransaction.setAmount(new BigDecimal("100")); // 设置金额
        updatedTransaction.setPayerAccount("12345"); // 设置付款账号
        updatedTransaction.setPayerName("kdl"); // 设置付款人姓名
        updatedTransaction.setPayeeAccount("67890"); // 设置收款账号
        updatedTransaction.setPayeeName("kdl"); // 设置收款人姓名
        updatedTransaction.setPaymentMethod("Cash"); // 设置交易方式

        when(transactionService.updateTransaction(id, updatedTransaction)).thenReturn(null);

        BaseResponse<Transaction> response = transactionController.updateTransaction(id, updatedTransaction);

        assertEquals(ErrorCodeEnum.BIZ_ERROR.getCode(), response.getCode());
        assertEquals("记录不存在，请刷新页面重试", response.getMessage());
    }

    /**
     * 测试更新单条记录 - 异常情况
     */
    @Test
    public void testUpdateTransaction_Exception() {
        Long id = 1L;
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionSerialNo("123456"); // 设置交易流水号
        updatedTransaction.setDescription("Description"); // 设置交易描述
        updatedTransaction.setAmount(new BigDecimal("100")); // 设置金额
        updatedTransaction.setPayerAccount("12345"); // 设置付款账号
        updatedTransaction.setPayerName("kdl"); // 设置付款人姓名
        updatedTransaction.setPayeeAccount("67890"); // 设置收款账号
        updatedTransaction.setPayeeName("kdl"); // 设置收款人姓名
        updatedTransaction.setPaymentMethod("Cash"); // 设置交易方式

        when(transactionService.updateTransaction(id, updatedTransaction)).thenThrow(new RuntimeException("Test Exception"));

        BaseResponse<Transaction> response = transactionController.updateTransaction(id, updatedTransaction);

        assertEquals(ErrorCodeEnum.SYSTEM_ERROR.getCode(), response.getCode());
        assertEquals("系统繁忙，请稍后再试", response.getMessage());
    }

    /**
     * 测试删除单条记录 - 正常情况
     */
    @Test
    public void testDeleteTransaction_Success() {
        Long id = 1L;
        when(transactionService.deleteTransaction(id)).thenReturn(true);

        BaseResponse response = transactionController.deleteTransaction(id);

        assertEquals(HttpStatus.OK.value(), response.getCode());
        assertEquals("success", response.getMessage());
    }

    /**
     * 测试删除单条记录 - 记录不存在
     */
    @Test
    public void testDeleteTransaction_RecordNotExists() {
        Long id = 1L;
        when(transactionService.deleteTransaction(id)).thenReturn(false);

        BaseResponse response = transactionController.deleteTransaction(id);

        assertEquals(ErrorCodeEnum.BIZ_ERROR.getCode(), response.getCode());
        assertEquals("记录不存在，请刷新页面重试", response.getMessage());
    }

    /**
     * 测试删除单条记录 - 异常情况
     */
    @Test
    public void testDeleteTransaction_Exception() {
        Long id = 1L;
        when(transactionService.deleteTransaction(id)).thenThrow(new RuntimeException("Test Exception"));

        BaseResponse response = transactionController.deleteTransaction(id);

        assertEquals(ErrorCodeEnum.SYSTEM_ERROR.getCode(), response.getCode());
        assertEquals("系统繁忙，请稍后再试", response.getMessage());
    }
}