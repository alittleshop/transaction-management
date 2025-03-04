// src/TransactionList.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const TransactionList = () => {
  const navigate = useNavigate();
  const [transactions, setTransactions] = useState([]);
  const [currentPage, setCurrentPage] = useState(1); // 当前页码
  const [totalPages, setTotalPages] = useState(1); // 总页数
  const [pageSize] = useState(10); // 每页显示的记录数
  const [updateTransaction, setUpdateTransaction] = useState({
      id: null,
      description: '',
      amount: 0,
      payerAccount: '',
      payeeAccount: '',
      paymentMethod: ''
  });

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const response = await axios.get(`/transactions?page=${currentPage - 1}&size=${pageSize}`);
        if(response.data.data.content !== null){
            setTransactions(response.data.data.content);
        }
        setTotalPages(response.data.data.totalPages);
      } catch (error) {
        console.error('获取交易记录失败:', error);
      }
    };

    fetchTransactions();
  }, [currentPage, pageSize]);

  const handleCreateTransaction = () => {
      // 跳转到创建交易记录的页面，假设路径为 '/create-transaction'
      navigate('/create');
  };

  // 更新交易记录
    const handleUpdateTransaction = async () => {
      try {
        const response = await axios.put(`/transactions/${updateTransaction.id}`, updateTransaction);
        if (response.status === 200) {
            const response = await axios.get(`/transactions?page=${currentPage - 1}&size=${pageSize}`);
            setTransactions(response.data.data.content);
            setTotalPages(response.data.data.totalPages);
          setUpdateTransaction({
            id: null,
            description: '',
            amount: 0,
            payerAccount: '',
            payeeAccount: '',
            paymentMethod: ''
          });
        }
      } catch (error) {
        console.error('修改交易记录失败:', error);
      }
    };

  const handleDelete = async (id) => {
    if (window.confirm('确定要删除该交易记录吗？')) {
      try {
        await axios.delete(`/transactions/${id}`);
        // 重新获取交易记录列表
        const response = await axios.get(`/transactions?page=${currentPage - 1}&size=${pageSize}`);
        setTransactions(response.data.data.content);
        setTotalPages(response.data.data.totalPages);
      } catch (error) {
        console.error('删除交易记录失败:', error);
      }
    }
  };

  const handlePageChange = (page) => {
    if (page >= 1 && page <= totalPages) {
      setCurrentPage(page);
    }
  };

  return (
    <div>
      <h1>交易记录列表</h1>
      <div><button onClick={handleCreateTransaction}>创建新交易记录</button></div>
      <table border="1px" style={{width:'1000px'}}>
        <thead>
          <tr>
            <th>ID</th>
            <th>描述</th>
            <th>金额</th>
            <th>付款账号</th>
            <th>收款账号</th>
            <th>支付方式</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((transaction) => (
            <tr key={transaction.id}>
              <td>{transaction.id}</td>
              <td>
                {updateTransaction.id === transaction.id ? (
                                  <input
                                    type="text"
                                    value={updateTransaction.description}
                                    onChange={(e) => setUpdateTransaction({ ...updateTransaction, description: e.target.value })}
                                  />
                                ) : (
                                  transaction.description
                                )}
              </td>
              <td>
                {updateTransaction.id === transaction.id ? (
                                  <input
                                    type="number"
                                    value={updateTransaction.amount}
                                    onChange={(e) => setUpdateTransaction({ ...updateTransaction, amount: parseFloat(e.target.value) })}
                                  />
                                ) : (
                                  transaction.amount
                                )}
              </td>
              <td>
                {updateTransaction.id === transaction.id ? (
                                <input
                                  type="text"
                                  value={updateTransaction.payerAccount}
                                  onChange={(e) => setUpdateTransaction({ ...updateTransaction, payerAccount: e.target.value })}
                                />
                              ) : (
                                transaction.payerAccount
                              )}
              </td>
              <td>
                {updateTransaction.id === transaction.id ? (
                                <input
                                  type="text"
                                  value={updateTransaction.payeeAccount}
                                  onChange={(e) => setUpdateTransaction({ ...updateTransaction, payeeAccount: e.target.value })}
                                />
                              ) : (
                                transaction.payeeAccount
                              )}
              </td>
              <td>
                {updateTransaction.id === transaction.id ? (
                                  <input
                                    type="text"
                                    value={updateTransaction.paymentMethod}
                                    onChange={(e) => setUpdateTransaction({ ...updateTransaction, paymentMethod: e.target.value })}
                                  />
                                ) : (
                                  transaction.paymentMethod
                                )}
              </td>
              <td>
                {updateTransaction.id === transaction.id ? (
                                  <button onClick={handleUpdateTransaction}>保存</button>
                                ) : (
                                  <>
                                    <button onClick={() => setUpdateTransaction({
                                      id: transaction.id,
                                      description: transaction.description,
                                      amount: transaction.amount,
                                      payerAccount: transaction.payerAccount,
                                      payeeAccount: transaction.payeeAccount,
                                      paymentMethod: transaction.paymentMethod
                                    })}>更新</button>
                                    <button onClick={() => handleDelete(transaction.id)}>删除</button>
                                  </>
                                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div>
        <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1}>
          上一页
        </button>
        <span>{currentPage} / {totalPages}</span>
        <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages}>
          下一页
        </button>
      </div>
    </div>
  );
};

export default TransactionList;