package controller;

import org.example.controller.TransactionController;
import org.example.service.TransactionService;
import org.example.vo.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * TransactionController压测类
 * author:kdl
 */
public class TransactionControllerStressTest {
    private static final int THREAD_POOL_SIZE = 10; // 线程池大小
    private static final int REQUESTS_PER_THREAD = 100; // 每个线程的请求数

    private static TransactionService transactionService = new TransactionService();
    private static TransactionController transactionController = new TransactionController(transactionService);

    public static void createTransactionStressTest() {
        // 创建固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Callable<Long>> tasks = new ArrayList<>();

        // 为每个线程添加任务
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            tasks.add(() -> {
                long totalTime = 0;
                for (int j = 0; j < REQUESTS_PER_THREAD; j++) {
                    // 记录开始时间
                    Instant start = Instant.now();
                    // 执行创建交易记录操作
                    Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
                    ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);
                    // 简单验证响应状态码
                    if (response.getStatusCode() != HttpStatus.CREATED) {
                        System.out.println("Unexpected status code: " + response.getStatusCode());
                    }
                    // 记录结束时间
                    Instant end = Instant.now();
                    // 计算本次操作的耗时
                    totalTime += Duration.between(start, end).toMillis();
                }
                return totalTime;
            });
        }

        try {
            // 提交所有任务并获取结果
            List<Future<Long>> futures = executorService.invokeAll(tasks);
            long totalTime = 0;
            for (Future<Long> future : futures) {
                totalTime += future.get();
            }

            // 计算总请求数
            int totalRequests = THREAD_POOL_SIZE * REQUESTS_PER_THREAD;
            // 计算平均响应时间
            double averageResponseTime = (double) totalTime / totalRequests;
            // qps
            double throughput = (double) totalRequests / (totalTime / 1000.0);

            // 输出压测结果
            System.out.println("创建交易 - 总请求数: " + totalRequests);
            System.out.println("创建交易 - 总耗时: " + totalTime + " ms");
            System.out.println("创建交易 - 平均响应时间: " + averageResponseTime + " ms");
            System.out.println("创建交易 - qps: " + throughput);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            executorService.shutdown();
        }
    }

    public static void updateTransactionStressTest() {
        // 创建固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Callable<Long>> tasks = new ArrayList<>();

        // 先创建一些交易记录，用于后续的更新操作
        List<Long> transactionIds = new ArrayList<>();
        for (int i = 0; i < THREAD_POOL_SIZE * REQUESTS_PER_THREAD; i++) {
            Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
            ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                transactionIds.add(response.getBody().getId());
            }
        }

        // 为每个线程添加任务
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            tasks.add(() -> {
                long totalTime = 0;
                for (int j = 0; j < REQUESTS_PER_THREAD; j++) {
                    // 记录开始时间
                    Instant start = Instant.now();
                    // 执行更新交易记录操作
                    Long id = transactionIds.get(j % transactionIds.size());
                    Transaction updatedTransaction = new Transaction(id, "Updated Description", new BigDecimal("200"), "12345", "67890", "Card");
                    ResponseEntity<Transaction> response = transactionController.updateTransaction(id, updatedTransaction);
                    // 简单验证响应状态码
                    if (response.getStatusCode() != HttpStatus.OK) {
                        System.out.println("Unexpected status code: " + response.getStatusCode());
                    }
                    // 记录结束时间
                    Instant end = Instant.now();
                    // 计算本次操作的耗时
                    totalTime += Duration.between(start, end).toMillis();
                }
                return totalTime;
            });
        }

        try {
            // 提交所有任务并获取结果
            List<Future<Long>> futures = executorService.invokeAll(tasks);
            long totalTime = 0;
            for (Future<Long> future : futures) {
                totalTime += future.get();
            }

            // 计算总请求数
            int totalRequests = THREAD_POOL_SIZE * REQUESTS_PER_THREAD;
            // 计算平均响应时间
            double averageResponseTime = (double) totalTime / totalRequests;
            // qps
            double throughput = (double) totalRequests / (totalTime / 1000.0);

            // 输出压测结果
            System.out.println("更新交易 - 总请求数: " + totalRequests);
            System.out.println("更新交易 - 总耗时: " + totalTime + " ms");
            System.out.println("更新交易 - 平均响应时间: " + averageResponseTime + " ms");
            System.out.println("更新交易 - qps: " + throughput);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            executorService.shutdown();
        }
    }

    public static void deleteTransactionStressTest() {
        // 创建固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Callable<Long>> tasks = new ArrayList<>();

        // 先创建一些交易记录，用于后续的删除操作
        List<Long> transactionIds = new ArrayList<>();
        for (int i = 0; i < THREAD_POOL_SIZE * REQUESTS_PER_THREAD; i++) {
            Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
            ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                transactionIds.add(response.getBody().getId());
            }
        }

        // 为每个线程添加任务
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            tasks.add(() -> {
                long totalTime = 0;
                for (int j = 0; j < REQUESTS_PER_THREAD; j++) {
                    // 记录开始时间
                    Instant start = Instant.now();
                    // 执行删除交易记录操作
                    Long id = transactionIds.get(j % transactionIds.size());
                    ResponseEntity<Void> response = transactionController.deleteTransaction(id);
                    // 简单验证响应状态码
                    if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
                        System.out.println("Unexpected status code: " + response.getStatusCode());
                    }
                    // 记录结束时间
                    Instant end = Instant.now();
                    // 计算本次操作的耗时
                    totalTime += Duration.between(start, end).toMillis();
                }
                return totalTime;
            });
        }

        try {
            // 提交所有任务并获取结果
            List<Future<Long>> futures = executorService.invokeAll(tasks);
            long totalTime = 0;
            for (Future<Long> future : futures) {
                totalTime += future.get();
            }

            // 计算总请求数
            int totalRequests = THREAD_POOL_SIZE * REQUESTS_PER_THREAD;
            // 计算平均响应时间
            double averageResponseTime = (double) totalTime / totalRequests;
            // qps
            double throughput = (double) totalRequests / (totalTime / 1000.0);

            // 输出压测结果
            System.out.println("删除交易 - 总请求数: " + totalRequests);
            System.out.println("删除交易 - 总耗时: " + totalTime + " ms");
            System.out.println("删除交易 - 平均响应时间: " + averageResponseTime + " ms");
            System.out.println("删除交易 - qps: " + throughput);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            executorService.shutdown();
        }
    }

    public static void getTransactionStressTest() {
        // 创建固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Callable<Long>> tasks = new ArrayList<>();

        // 先创建一些交易记录，用于后续的查询操作
        List<Long> transactionIds = new ArrayList<>();
        for (int i = 0; i < THREAD_POOL_SIZE * REQUESTS_PER_THREAD; i++) {
            Transaction transaction = new Transaction(null, "Test Description", new BigDecimal("100"), "12345", "67890", "Cash");
            ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                transactionIds.add(response.getBody().getId());
            }
        }

        // 为每个线程添加任务
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            tasks.add(() -> {
                long totalTime = 0;
                for (int j = 0; j < REQUESTS_PER_THREAD; j++) {
                    // 记录开始时间
                    Instant start = Instant.now();
                    // 执行查询交易记录操作
                    Long id = transactionIds.get(j % transactionIds.size());
                    List<Transaction> list = transactionController.getAllTransactions();
                    // 简单验证结果
                    if (CollectionUtils.isEmpty(list)) {
                        System.out.println("Unexpected null result for id: " + id);
                    }
                    // 记录结束时间
                    Instant end = Instant.now();
                    // 计算本次操作的耗时
                    totalTime += Duration.between(start, end).toMillis();
                }
                return totalTime;
            });
        }

        try {
            // 提交所有任务并获取结果
            List<Future<Long>> futures = executorService.invokeAll(tasks);
            long totalTime = 0;
            for (Future<Long> future : futures) {
                totalTime += future.get();
            }

            // 计算总请求数
            int totalRequests = THREAD_POOL_SIZE * REQUESTS_PER_THREAD;
            // 计算平均响应时间
            double averageResponseTime = (double) totalTime / totalRequests;
            // qps
            double throughput = (double) totalRequests / (totalTime / 1000.0);

            // 输出压测结果
            System.out.println("查询交易 - 总请求数: " + totalRequests);
            System.out.println("查询交易 - 总耗时: " + totalTime + " ms");
            System.out.println("查询交易 - 平均响应时间: " + averageResponseTime + " ms");
            System.out.println("查询交易 - qps: " + throughput);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            executorService.shutdown();
        }
    }

    public static void main(String[] args) {
        // 压测创建交易
        createTransactionStressTest();
        // 压测更新交易
//        updateTransactionStressTest();
        // 压测删除交易
//        deleteTransactionStressTest();
        // 压测查询交易
//        getTransactionStressTest();
    }
}