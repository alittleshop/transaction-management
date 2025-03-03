import React, { useState, useEffect } from 'react';
import axios from 'axios';

const App = () => {
  const [transactions, setTransactions] = useState([]);
  const [newTransaction, setNewTransaction] = useState({
    description: '',
    amount: 0,
    payerAccount: '',
    payeeAccount: '',
    paymentMethod: ''
  });
  const [updateTransaction, setUpdateTransaction] = useState({
    id: null,
    description: '',
    amount: 0,
    payerAccount: '',
    payeeAccount: '',
    paymentMethod: ''
  });

  // 格式化日期函数
    const formatDate = (timestamp) => {
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    };

  // 获取所有交易记录
  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const response = await axios.get('/transactions');
        setTransactions(response.data);
      } catch (error) {
        console.error('Error fetching transactions:', error);
      }
    };

    fetchTransactions();
  }, []);

  // 创建新的交易记录
  const handleCreateTransaction = async () => {
    try {
      const response = await axios.post('/transactions', newTransaction);
      if (response.status === 201) {
        setTransactions([...transactions, response.data]);
        setNewTransaction({
          description: '',
          amount: 0,
          payerAccount: '',
          payeeAccount: '',
          paymentMethod: ''
        });
      }
    } catch (error) {
       if(error.status===405){
         alert('创建失败，请稍后重试！');
       }
       if(error.status===400){
         alert('创建交易记录失败，请检查金额/账号是否已经输入，稍后重新重试！');
       }
      console.error('Error creating transaction:', error);
    }
  };

  // 更新交易记录
  const handleUpdateTransaction = async () => {
    try {
      const response = await axios.put(`/transactions/${updateTransaction.id}`, updateTransaction);
      if (response.status === 200) {
        const updatedTransactions = transactions.map(transaction =>
          transaction.id === updateTransaction.id ? response.data : transaction
        );
        setTransactions(updatedTransactions);
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
        if(error.status===400){
          alert('更新交易记录失败，请检查金额/账号是否已经输入，稍后重新重试！');
        }
        if(error.status===404){
          alert('更新交易记录失败，交易记录不存在，请稍后重试！');
        }
        if(error.status===405){
           alert('更新失败，请稍后重试！');
        }
      console.error('Error updating transaction:', error);
    }
  };

  // 删除交易记录
  const handleDeleteTransaction = async (id) => {
    try {
      const response = await axios.delete(`/transactions/${id}`);
      if (response.status === 204) {
        const filteredTransactions = transactions.filter(transaction => transaction.id !== id);
        setTransactions(filteredTransactions);
      }
    } catch (error) {
       if(error.status===404){
         alert('删除交易记录失败，交易记录不存在！');
       }
       if(error.status===405){
         alert('删除失败，请稍后重试！');
       }
      console.error('Error deleting transaction:', error);
    }
  };

  return (
    <div>
      <h1>交易管理</h1>

      {/* 创建交易记录表单 */}
      <h2>创建交易</h2>
      <input
        type="text"
        placeholder="交易描述"
        value={newTransaction.description}
        onChange={(e) => setNewTransaction({ ...newTransaction, description: e.target.value })}
      />
      <input
        type="number"
        placeholder="金额"
        value={newTransaction.amount}
        onChange={(e) => setNewTransaction({ ...newTransaction, amount: parseFloat(e.target.value) })}
      />
      <input
        type="text"
        placeholder="付款账号"
        value={newTransaction.payerAccount}
        onChange={(e) => setNewTransaction({ ...newTransaction, payerAccount: e.target.value })}
      />
      <input
        type="text"
        placeholder="收款账号"
        value={newTransaction.payeeAccount}
        onChange={(e) => setNewTransaction({ ...newTransaction, payeeAccount: e.target.value })}
      />
      <input
        type="text"
        placeholder="交易方式"
        value={newTransaction.paymentMethod}
        onChange={(e) => setNewTransaction({ ...newTransaction, paymentMethod: e.target.value })}
      />
      <button onClick={handleCreateTransaction}>创建</button>

      {/* 交易记录列表 */}
      <h2>交易列表</h2>
      <table border="1px" style={{width:'1000px'}} >
        <thead>
          <tr>
            <th>交易流水号</th>
            <th>交易描述</th>
            <th>金额</th>
            <th>付款账号</th>
            <th>收款账号</th>
            <th>交易方式</th>
            <th>交易更新时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map(transaction => (
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
              <td>{formatDate(transaction.timestamp)}</td>
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
                    <button onClick={() => handleDeleteTransaction(transaction.id)}>删除</button>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default App;