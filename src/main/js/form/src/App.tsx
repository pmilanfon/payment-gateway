import './App.css';
import PaymentForm from './PaymentForm';

export interface AppState {
  mockMerchantVisible: boolean;
  receiptVisible: boolean;
  errorVisible: boolean;
  paymentFormVisible: boolean;
  amount: number;
  amountAfterFee: number;
}
function App() {
  return (
    <>
      <PaymentForm />
    </>
  );
}

export default App;
