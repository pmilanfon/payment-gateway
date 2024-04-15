import { useState, useEffect } from "react";
import { LoadingSpinner } from "./LoadingSpinner";
import { Stack, Heading, Divider, Button,  Center } from "@chakra-ui/react";
import { WarningIcon } from "@chakra-ui/icons";

export const ErrorView = () => {
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
      const timer = setTimeout(() => {
        setIsLoading(false);
      }, 3000);
  
      return () => clearTimeout(timer);
    }, []);
    return (
        isLoading ? <LoadingSpinner text="Processing..." /> :
    <Center>
    <Stack align='center' mt='12' spacing='8'>
    
    <WarningIcon boxSize={32} color={'red.800'}/>
      <Heading fontSize='5xl' color='red.700'>Deposit failed</Heading>
        <Divider />
      <Button 
        onClick={() => window.location.reload()}
        size='lg' 
        variant='ghost' 
        colorScheme='blue' 
        fontSize={'2xl'}>
        Go back and try again
      </Button>
    </Stack>
    </Center>
    )
}