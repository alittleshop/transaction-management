package controller;

import com.controller.TransactionController;
import com.service.impl.TransactionServiceImpl;
import com.data.vo.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TransactionController的单元测试
 * author:kdl
 */
public class TransactionControllerTest {

    @Mock
    private TransactionServiceImpl transactionServiceImpl;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // 测试创建交易记录
    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
        when(transactionServiceImpl.createTransaction(transaction)).thenReturn(transaction);
        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // 测试创建交易记录时缺少必要信息
    @Test
    public void testCreateTransactionWithMissingInfo() {
        Transaction transaction = new Transaction(null, null, new BigDecimal("100"), "", "67890", "Cash");
        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // 测试获取所有交易记录
    @Test
    public void testGetAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        when(transactionServiceImpl.getAllTransactions()).thenReturn(transactions);
        List<Transaction> result = transactionController.getAllTransactions();
        assertEquals(transactions, result);
    }


    // 测试更新交易记录
    @Test
    public void testUpdateTransaction() {
        Long id = 1L;
        Transaction updatedTransaction = new Transaction(id, "Updated Description", new BigDecimal("200"), "12345", "67890", "Card");
        when(transactionServiceImpl.updateTransaction(id, updatedTransaction)).thenReturn(updatedTransaction);
        ResponseEntity<Transaction> response = transactionController.updateTransaction(id, updatedTransaction);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTransaction, response.getBody());
    }

    // 测试更新不存在的交易记录
    @Test
    public void testUpdateNonExistentTransaction() {
        Long id = 1L;
        Transaction updatedTransaction = new Transaction(id, "Updated Description", new BigDecimal("200"), "12345", "67890", "Card");
        when(transactionServiceImpl.updateTransaction(id, updatedTransaction)).thenReturn(null);
        ResponseEntity<Transaction> response = transactionController.updateTransaction(id, updatedTransaction);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // 测试删除交易记录
    @Test
    public void testDeleteTransaction() {
        Long id = 1L;
        when(transactionServiceImpl.deleteTransaction(id)).thenReturn(true);
        ResponseEntity<Void> response = transactionController.deleteTransaction(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // 测试删除不存在的交易记录
    @Test
    public void testDeleteNonExistentTransaction() {
        Long id = 1L;
        when(transactionServiceImpl.deleteTransaction(id)).thenReturn(false);
        ResponseEntity<Void> response = transactionController.deleteTransaction(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}