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
      payerName: '',
      payeeAccount: '',
      payeeName: '',
      paymentMethod: ''
  });
  const [transactionSerialNo, setTransactionSerialNo] = useState(''); // 新增查询条件


  const fetchTransactions = async () => {
      try {
      // 构建请求参数，包含分页信息和查询条件
      const params = {
        page: currentPage - 1,
        size: pageSize,
        transactionSerialNo: transactionSerialNo
      };
      const response = await axios.get('/transactions', { params })
      if(response.data.data.content !== null){
          setTransactions(response.data.data.content);
      }
      setTotalPages(response.data.data.totalPages);
    } catch (error) {
      console.error('获取交易记录失败:', error);
    }
  };

  useEffect(() => {
    fetchTransactions();
  }, [currentPage, pageSize]);

  const handleCreateTransaction = () => {
      // 跳转到创建交易记录的页面
      navigate('/create');
  };

  // 更新交易记录
    const handleUpdateTransaction = async () => {
      try {
        const response = await axios.put(`/transactions/${updateTransaction.id}`, updateTransaction);
        if(response.data.code!==200){
           alert(response.data.message);
           return;
        }
        if (response.status === 200) {
            // 重新获取交易记录列表
            const params = {
              page: currentPage - 1,
              size: pageSize,
              transactionSerialNo: transactionSerialNo
            };
            const response = await axios.get('/transactions', { params });
            setTransactions(response.data.data.content);
            setTotalPages(response.data.data.totalPages);
          setUpdateTransaction({
            id: null,
            description: '',
            amount: 0,
            payerAccount: '',
            payerName: '',
            payeeAccount: '',
            payeeName: '',
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
        const response = await axios.delete(`/transactions/${id}`);
        if(response.data.code!==200){
          alert(response.data.message);
        }
        // 重新获取交易记录列表
        const params = {
          page: currentPage - 1,
          size: pageSize,
          transactionSerialNo: transactionSerialNo
        };
        const response1 = await axios.get('/transactions', { params });
        setTransactions(response1.data.data.content);
        setTotalPages(response1.data.data.totalPages);
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

  const handleSearch = async(e) => {
    e.preventDefault();
    setCurrentPage(1); // 搜索时重置页码为 1
    await fetchTransactions(); // 重新查询
 };

  return (
    <div>
      <h1>交易记录列表</h1>
      <div style={{paddingBottom: '10px'}}><button onClick={handleCreateTransaction}>创建新交易记录</button></div>
      <div style={{paddingBottom: '10px'}}>
        <form onSubmit={handleSearch}>
          <input
            type="text"
            placeholder="输入交易流水号"
            value={transactionSerialNo}
            onChange={(e) => setTransactionSerialNo(e.target.value)}
          />
          <button type="submit">查询</button>
        </form>
      </div>
      <table border="1px" style={{width:'1000px'}}>
        <thead>
          <tr>
            <th>银行流水号</th>
            <th>描述</th>
            <th>金额</th>
            <th>付款账号</th>
            <th>付款人姓名</th>
            <th>收款账号</th>
            <th>收款人姓名</th>
            <th>支付方式</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((transaction) => (
            <tr key={transaction.id}>
              <td>{transaction.transactionSerialNo}</td>
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
                    value={updateTransaction.payerName}
                    onChange={(e) => setUpdateTransaction({ ...updateTransaction, payerName: e.target.value })}
                    />
                  ) : (
                    transaction.payerName
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
                  value={updateTransaction.payeeName}
                  onChange={(e) => setUpdateTransaction({ ...updateTransaction, payeeName: e.target.value })}
                />
              ) : (
                transaction.payeeName
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
                                      transactionSerialNo: transaction.transactionSerialNo,
                                      amount: transaction.amount,
                                      payerAccount: transaction.payerAccount,
                                      payerName: transaction.payerName,
                                      payeeAccount: transaction.payeeAccount,
                                      payeeName: transaction.payeeName,
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
      <div style={{paddingTop: '20px'}}>
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