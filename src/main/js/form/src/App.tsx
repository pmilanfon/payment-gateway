import React, { useRef } from 'react';
import './App.css';
import CardForm from './CardForm';
import Receipt from './components/Receipt';
import { AppState } from './store/types';
import Error from './components/Error';

function App() {
  const reference = useRef<string>(window.location.pathname.split('/').pop() ?? '')
  const [appState, setAppState] = React.useState<AppState>({name: 'FORM', value: ''});

  switch (appState.name) {
    case 'ERROR':
      return <Error message={appState.value} setAppState={setAppState}/>;
    case  'FORM':
      return <CardForm setAppState={setAppState} reference={reference}/>;
    case 'RECEIPT':
      return <Receipt />
    default:
      return <Error message={appState.value} setAppState={setAppState} />;
  }
}

export default App;
