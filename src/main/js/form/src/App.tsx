import React from 'react';
import './App.css';
import PaymentForm from './PaymentForm';
import Receipt from './components/Receipt';

export interface AppState {
  mockMerchantVisible: boolean;
  receiptVisible: boolean;
  errorVisible: boolean;
  paymentFormVisible: boolean;
  amount: number;
  amountAfterFee: number;
}

function App() {
  const [showReceipt, setShowReceipt] = React.useState(false);
  const handleShowReceipt = () => {
    setShowReceipt(true);
  };
  return (
    <>
    {showReceipt ? <Receipt /> : <PaymentForm handleShowReceipt={handleShowReceipt}/>}
    </>
  );
}

export default App;
