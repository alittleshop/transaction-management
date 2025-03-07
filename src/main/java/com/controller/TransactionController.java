package com.controller;


import com.entity.BaseResponse;
import com.service.ITransactionService;
import com.entity.vo.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 交易记录api类
 * author:kdl
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    /**
     * 创建交易记录
     * @param transaction
     * @return
     */
    @PostMapping
    public BaseResponse<Transaction> createTransaction(@RequestBody @Validated Transaction transaction) {
        try {
            Transaction createdTransaction = transactionService.createTransaction(transaction);
            return BaseResponse.successData(createdTransaction);
        }catch (RuntimeException e){
            // 异常捕获
            return BaseResponse.systemError(e.getMessage());
        }catch (Exception e){
            // 异常捕获
            e.printStackTrace();
            return BaseResponse.systemError("系统繁忙，请稍后再试");
        }
    }

    /**
     * 获取所有交易记录
     * @return
     */
    @GetMapping
    public BaseResponse<Page<Transaction>> getTransactionsPage(
            @RequestParam(required = false) String transactionSerialNo,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
            ) {
        try{
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<Transaction> result =  transactionService.getTransactionsPage(transactionSerialNo, pageable);
            return BaseResponse.successData(result);
        }catch (Exception e){
            // 异常捕获
            e.printStackTrace();
            return BaseResponse.systemError("系统繁忙，请稍后再试");
        }
    }


    /**
     * 更新单条记录
     * @param id
     * @param updatedTransaction
     * @return
     */
    @PutMapping("/{id}")
    public BaseResponse<Transaction> updateTransaction(@PathVariable Long id, @RequestBody @Validated Transaction updatedTransaction) {
        try {
            Transaction transaction = transactionService.updateTransaction(id, updatedTransaction);
            if (transaction == null) {
                return BaseResponse.bizError("记录不存在，请刷新页面重试");
            }
            return BaseResponse.successData(transaction);
        }catch (Exception e){
            // 异常捕获
            e.printStackTrace();
            return BaseResponse.systemError("系统繁忙，请稍后再试");
        }
    }

    /**
     * 删除单条记录
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResponse deleteTransaction(@PathVariable Long id) {
        try{
            boolean deleted = transactionService.deleteTransaction(id);
            if (!deleted) {
                return BaseResponse.bizError("记录不存在，请刷新页面重试");
            }
            return BaseResponse.success();
        }catch (Exception e){
            // 异常捕获
            e.printStackTrace();
            return BaseResponse.systemError("系统繁忙，请稍后再试");
        }
    }
}
