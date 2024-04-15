import { Text, Stack, Heading, Divider, Button, Center } from "@chakra-ui/react"
import {  CheckCircleIcon } from '@chakra-ui/icons'
import { useState, useEffect } from "react";
import { LoadingSpinner } from "./LoadingSpinner";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const Receipt = ({appState}: any) => {
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
      const timer = setTimeout(() => {
        setIsLoading(false);
      }, 2000);
  
      return () => clearTimeout(timer);
    }, []);
    return (
    isLoading ? <LoadingSpinner text="Processing Payment" /> :
    <Center>
    <Stack align='center' mt='12' spacing='8'>
    
    <CheckCircleIcon boxSize={32} color={'green.500'}/>
      <Heading fontSize='3xl' color='green.600'>Deposit Successful!</Heading>
      <Text fontWeight='bold' fontSize='5xl' color={'green.700'}>
        {appState.amountAfterFee?.toFixed(2)} $
      </Text>
        <Divider />
      <Button 
        onClick={() => window.location.reload()}
        size='lg' 
        variant='ghost' 
        colorScheme='blue' 
        fontSize={'2xl'}>
        Back to MockMerchant
      </Button>
    </Stack>
    </Center>
    )
}