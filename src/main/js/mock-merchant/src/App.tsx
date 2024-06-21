// import { useState } from 'react'
import React from 'react'
import './App.css'
import MockMerchant from './MockMerchant'
import { ChakraProvider } from '@chakra-ui/react'

function App() {
  //const [count, setCount] = useState(0)

  return (
    <ChakraProvider>
      <MockMerchant />
    </ChakraProvider>
  )
}

export default App
