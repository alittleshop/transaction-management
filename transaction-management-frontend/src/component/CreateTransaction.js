// src/CreateTransaction.js
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const CreateTransaction = () => {
  const navigate = useNavigate();
  const [description, setDescription] = useState('');
  const [amount, setAmount] = useState(null);
  const [payerAccount, setPayerAccount] = useState('');
  const [payeeAccount, setPayeeAccount] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('');


  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const transaction = {
        description,
        amount,
        payerAccount,
        payeeAccount,
        paymentMethod
      };
      await axios.post('/transactions', transaction);
      console.log('创建交易记录成功');
      navigate('/');
    } catch (error) {
      console.error('创建交易记录失败:', error);
    }
  };

  return (
    <div>
      <h1>创建交易记录</h1>
      <form onSubmit={handleSubmit}>
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
        <label>收款账号:</label>
        <input
          type="text"
          value={payeeAccount}
          onChange={(e) => setPayeeAccount(e.target.value)}
          required
        />
        <br />
        <label>支付方式:</label>
        <input
          type="text"
          value={paymentMethod}
          onChange={(e) => setPaymentMethod(e.target.value)}
        />
        <br />
        <button type="submit">提交</button>
      </form>
    </div>
  );
};

export default CreateTransaction;