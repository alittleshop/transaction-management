// src/CreateTransaction.js
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const CreateTransaction = () => {
  const navigate = useNavigate();
  const [description, setDescription] = useState('');
  const [amount, setAmount] = useState(null);
  const [payerAccount, setPayerAccount] = useState('');
  const [payerName, setPayerName] = useState('');
  const [payeeAccount, setPayeeAccount] = useState('');
  const [payeeName, setPayeeName] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('');
  const [transactionSerialNo, setTransactionSerialNo] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const transaction = {
        transactionSerialNo,
        description,
        amount,
        payerAccount,
        payerName,
        payeeAccount,
        payeeName,
        paymentMethod
      };
      const response = await axios.post('/transactions', transaction);
      if(response.data.code!==200){
        alert('创建交易记录失败，原因：'+response.data.message);
      }else{
        navigate('/');
      }
      console.log('创建交易记录成功');
    } catch (error) {
      console.error('创建交易记录失败:', error);
    }
  };

  const handleReturnHome = () => {
        // 回到首页
        navigate('/');
    };

  return (
    <div>
      <h1>创建交易记录</h1>
      <form onSubmit={handleSubmit}>
        <label>银行交易流水号（唯一）:</label>
        <input
          type="text"
          value={transactionSerialNo}
          onChange={(e) => setTransactionSerialNo(e.target.value)}
          required
        />
        <br />
        <label>描述:</label>
        <input
          type="text"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        <br />
        <label>金额:</label>
        <input
          type="number"
          value={amount}
          onChange={(e) => setAmount(Number(e.target.value))}
          required
        />
        <br />
        <label>付款账号:</label>
        <input
          type="text"
          value={payerAccount}
          onChange={(e) => setPayerAccount(e.target.value)}
          required
        />
        <br />
        <label>付款人姓名:</label>
        <input
          type="text"
          value={payerName}
          onChange={(e) => setPayerName(e.target.value)}
          required
        />
        <br />
        <label>收款账号:</label>
        <input
          type="text"
          value={payeeAccount}
          onChange={(e) => setPayeeAccount(e.target.value)}
          required
        />
        <br />
        <label>收款人姓名:</label>
        <input
          type="text"
          value={payeeName}
          onChange={(e) => setPayeeName(e.target.value)}
        />
        <br />
        <label>支付方式:</label>
        <input
          type="text"
          value={paymentMethod}
          onChange={(e) => setPaymentMethod(e.target.value)}
        />
        <br />
        <br />
        <button type="submit">提交</button>
        <button onClick={handleReturnHome}>返回</button>
      </form>
    </div>
  );
};

export default CreateTransaction;