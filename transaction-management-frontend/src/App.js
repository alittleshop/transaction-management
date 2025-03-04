// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import TransactionList from './component/TransactionList';
import CreateTransaction from './component/CreateTransaction';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<TransactionList />} />
        <Route path="/create" element={<CreateTransaction />} />
      </Routes>
    </Router>
  );
};

export default App;