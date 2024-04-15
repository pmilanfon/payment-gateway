import { useState } from 'react'
import './App.css'
import PaymentForm from './PaymentForm'
import { MockMerchant } from './components/MockMerchant';
import { Receipt } from './components/Receipt';
import { ErrorView } from './components/ErrorView';

export interface AppState {
  mockMerchantVisible: boolean,
  receiptVisible: boolean,
  errorVisible: boolean,
  paymentFormVisible: boolean,
  amount: number,
  amountAfterFee: number
}
function App() {
  const [appState, setAppState] = useState<AppState>({
    mockMerchantVisible: true,
    receiptVisible: false,
    errorVisible: false,
    paymentFormVisible: false,
    amount: 0,
    amountAfterFee: 0,
  });

  return (
    <>
    {appState.mockMerchantVisible && <MockMerchant setAppState={setAppState}/>}
    {appState.receiptVisible && appState.amount !== 42 && <Receipt appState={appState}/>}
    {appState.errorVisible && appState.amount == 42 && <ErrorView />}
    {appState.paymentFormVisible && <PaymentForm setAppState={setAppState} appState={appState} />}
    </>
  )
}

export default App
