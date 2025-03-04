// src/TransactionTable.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';

const TransactionTable = () => {
  // 存储交易记录列表
  const [transactions, setTransactions] = useState([]);
  // 当前页码
  const [currentPage, setCurrentPage] = useState(1);
  // 每页显示的记录数
  const [pageSize, setPageSize] = useState(10);
  // 交易流水号，用于搜索
  const [transactionSerialNo, setTransactionSerialNo] = useState('');

  // 分页查询交易记录
  const fetchTransactions = async () => {
    try {
      const response = await axios.get('/transactions', {
        params: {
          transactionSerialNo,
          page: currentPage - 1, // 后端分页从 0 开始
          size: pageSize
        }
      });
      setTransactions(response.data.content);
    } catch (error) {
      console.error('Error fetching transactions:', error);
    }
  };

  // 处理页码变化
  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  // 处理每页显示记录数变化
  const handlePageSizeChange = (size) => {
    setPageSize(size);
    setCurrentPage(1); // 重置页码为 1
  };

  // 处理搜索框输入变化
  const handleSearchChange = (event) => {
    setTransactionSerialNo(event.target.value);
    setCurrentPage(1); // 重置页码为 1
  };

  // 组件挂载时和依赖项变化时调用
  useEffect(() => {
    fetchTransactions();
  }, [currentPage, pageSize, transactionSerialNo]);

  return (
    <div>
      <input
        type="text"
        placeholder="输入交易流水号"
        value={transactionSerialNo}
        onChange={handleSearchChange}
      />
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>金额</th>
            <th>付款账号</th>
            <th>收款账号</th>
            {/* 可根据需要添加更多表头 */}
          </tr>
        </thead>
        <tbody>
          {transactions.map((transaction) => (
            <tr key={transaction.id}>
              <td>{transaction.id}</td>
              <td>{transaction.amount}</td>
              <td>{transaction.payerAccount}</td>
              <td>{transaction.payeeAccount}</td>
              {/* 可根据需要添加更多单元格 */}
            </tr>
          ))}
        </tbody>
      </table>
      <div>
        <button
          onClick={() => handlePageChange(currentPage - 1)}
          disabled={currentPage === 1}
        >
          上一页
        </button>
        <span>{currentPage}</span>
        <button onClick={() => handlePageChange(currentPage + 1)}>下一页</button>
        <select value={pageSize} onChange={(e) => handlePageSizeChange(Number(e.target.value))}>
          <option value="10">10 条/页</option>
          <option value="20">20 条/页</option>
          <option value="50">50 条/页</option>
        </select>
      </div>
    </div>
  );
};

export default TransactionTable;