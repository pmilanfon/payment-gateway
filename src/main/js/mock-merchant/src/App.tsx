// import { useState } from 'react'
import { useEffect, useState } from 'react'
import './App.css'
import MockMerchant from './MockMerchant'
import { ChakraProvider } from '@chakra-ui/react'
import { authenticate } from './utils/authenticator';
import SpinnerComponent from './SpinnerComponent';

function App() {
  const [token, setToken] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchToken = async () => {
      setToken(await authenticate());
      setLoading(false);
    };

    fetchToken();
  }, []);

  return (
    <ChakraProvider>
     {loading ? <SpinnerComponent text='Loading deposit form...'/> : token && <MockMerchant token={token} />}
    </ChakraProvider>
  )
}

export default App
