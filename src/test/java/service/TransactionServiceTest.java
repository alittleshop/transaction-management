package service;

import com.service.impl.TransactionServiceImpl;
import com.data.vo.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TransactionService的单元测试
 * autho:kdl
 */
public class TransactionServiceTest {

    private TransactionServiceImpl transactionServiceImpl;

    @BeforeEach
    public void setUp() {
        transactionServiceImpl = new TransactionServiceImpl();
    }

    // 测试创建交易记录
    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
        Transaction createdTransaction = transactionServiceImpl.createTransaction(transaction);
        assertNotNull(createdTransaction.getId());
        assertEquals("Test Description", createdTransaction.getDescription());
        assertNotNull(createdTransaction.getTimestamp());
    }

    // 测试获取所有交易记录
    @Test
    public void testGetAllTransactions() {
        Transaction transaction1 = new Transaction(null, "Test Description 1", new BigDecimal("100"), "12345", "67890", "Cash");
        Transaction transaction2 = new Transaction(null, "Test Description 2", new BigDecimal("200"), "12345", "67890", "Card");
        transactionServiceImpl.createTransaction(transaction1);
        transactionServiceImpl.createTransaction(transaction2);
        List<Transaction> allTransactions = transactionServiceImpl.getAllTransactions();
        assertEquals(2, allTransactions.size());
    }

    // 测试更新交易记录
    @Test
    public void testUpdateTransaction() {
        Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
        Transaction createdTransaction = transactionServiceImpl.createTransaction(transaction);
        Transaction updatedTransaction = new Transaction(null, "Updated Description", new BigDecimal("200"), "12345", "67890", "Card");
        Transaction result = transactionServiceImpl.updateTransaction(createdTransaction.getId(), updatedTransaction);
        assertNotNull(result);
        assertEquals("Updated Description", result.getDescription());
        assertEquals(new BigDecimal("200"), result.getAmount());
    }

    // 测试更新不存在的交易记录
    @Test
    public void testUpdateNonExistentTransaction() {
        Transaction updatedTransaction = new Transaction(null, "Updated Description", new BigDecimal("200"), "12345", "67890", "Card");
        Transaction result = transactionServiceImpl.updateTransaction(999L, updatedTransaction);
        assertNull(result);
    }

    // 测试删除交易记录
    @Test
    public void testDeleteTransaction() {
        Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
        Transaction createdTransaction = transactionServiceImpl.createTransaction(transaction);
        boolean deleted = transactionServiceImpl.deleteTransaction(createdTransaction.getId());
        assertTrue(deleted);
    }

    // 测试删除不存在的交易记录
    @Test
    public void testDeleteNonExistentTransaction() {
        boolean deleted = transactionServiceImpl.deleteTransaction(999L);
        assertFalse(deleted);
    }
}