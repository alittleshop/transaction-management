package service;

import org.example.service.TransactionService;
import org.example.vo.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceTest {

    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionService();
    }

    // 测试创建交易记录
    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        assertNotNull(createdTransaction.getId());
        assertEquals("Test Description", createdTransaction.getDescription());
        assertNotNull(createdTransaction.getTimestamp());
    }

    // 测试通过ID获取交易记录
    @Test
    public void testGetTransaction() {
        Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        Transaction retrievedTransaction = transactionService.getTransaction(createdTransaction.getId());
        assertEquals(createdTransaction.getId(), retrievedTransaction.getId());
    }

    // 测试获取所有交易记录
    @Test
    public void testGetAllTransactions() {
        Transaction transaction1 = new Transaction(null, "Test Description 1", new BigDecimal("100"), "12345", "67890", "Cash");
        Transaction transaction2 = new Transaction(null, "Test Description 2", new BigDecimal("200"), "12345", "67890", "Card");
        transactionService.createTransaction(transaction1);
        transactionService.createTransaction(transaction2);
        List<Transaction> allTransactions = transactionService.getAllTransactions();
        assertEquals(2, allTransactions.size());
    }

    // 测试更新交易记录
    @Test
    public void testUpdateTransaction() {
        Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        Transaction updatedTransaction = new Transaction(null, "Updated Description", new BigDecimal("200"), "12345", "67890", "Card");
        Transaction result = transactionService.updateTransaction(createdTransaction.getId(), updatedTransaction);
        assertNotNull(result);
        assertEquals("Updated Description", result.getDescription());
        assertEquals(new BigDecimal("200"), result.getAmount());
    }

    // 测试更新不存在的交易记录
    @Test
    public void testUpdateNonExistentTransaction() {
        Transaction updatedTransaction = new Transaction(null, "Updated Description", new BigDecimal("200"), "12345", "67890", "Card");
        Transaction result = transactionService.updateTransaction(999L, updatedTransaction);
        assertNull(result);
    }

    // 测试删除交易记录
    @Test
    public void testDeleteTransaction() {
        Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        boolean deleted = transactionService.deleteTransaction(createdTransaction.getId());
        assertTrue(deleted);
    }

    // 测试删除不存在的交易记录
    @Test
    public void testDeleteNonExistentTransaction() {
        boolean deleted = transactionService.deleteTransaction(999L);
        assertFalse(deleted);
    }
}