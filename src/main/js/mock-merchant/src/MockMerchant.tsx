import React, { useState } from 'react';
import {
    FormControl,
    FormLabel,
    Input,
    Button,
    Text,
    FormErrorMessage,
    FormHelperText,
    Box,
    useColorModeValue,
  } from "@chakra-ui/react";

interface MoneyFormProps {
  token: string;
}

const MoneyForm: React.FC<MoneyFormProps> = ({token}) => {
    const [amount, setAmount] = useState<string>('');
    const [error, setError] = useState("")
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false);

    const merchantTxRef = crypto.randomUUID()
    
    const bodyData = {
      "amount": parseFloat(amount),
      "product": "example_product",
      "currency": "USD",
      "merchantTxRef": merchantTxRef,
      "orderDescription": "Example deposit order",
      "billingAddress": "123 Main St",
      "address": "456 Secondary St",
      "transactionType": "DEPOSIT",
      "city": "Example City",
      "state": "Example State",
      "postCode": "12345",
      "countryCode": "US",
      "emailAddress": "example@example.com",
      "phoneNumber": "+1234567890",
      "ipAddress": "192.168.1.1",
      "locale": "en_US",
      "dateOfBirth": "1990-01-01",
      "firstName": "John",
      "lastName": "Doe",
      "callbackUrl": "http://localhost:9090/merchant/notification",
      "redirectUrl": "http://localhost:9090/merchant/callback"
  };

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        setLoading(true);

        try {
            const response = await fetch('http://localhost:7777/api/payments/deposit/initiate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(bodyData),
            });

            if (!response.ok) {
                setError('Network response was not ok');
                throw new Error('Network response was not ok');
            }

            const data = await response.json();

            window.open(data.url);
            setLoading(false);

            const pollPaymentStatus = async () => {
                try {
                    const paymentStatusResponse = await fetch(`http://localhost:7777/api/payments/deposit/${merchantTxRef}`);
                    const paymentStatusData = await paymentStatusResponse.json();
    
                    if (paymentStatusData.currentState === 'DEPOSIT_SUCCESSFUL') {
                        setSuccess(true);
                    } else {
                        setTimeout(pollPaymentStatus, 3000);
                    }
                } catch (error) {
                    console.error('Error:', error);
                }
            };
    
            pollPaymentStatus();
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const handleNewDeposit = () => {
      setAmount('');
      setError('');
      setSuccess(false);
    };

    return (
        <Box
          maxW="md"
          mx="auto"
          p={8}
          bg={useColorModeValue("white", "gray.800")}
          shadow="md"
          rounded="lg"
        >
          <Box mb={4} textAlign={"left"}>
            <Text fontSize="xl" fontWeight="bold">
              Customer:
            </Text>
            <Text fontSize="md" mt={2}>
              First Name: {bodyData.firstName}
              <br />
              Last Name: {bodyData.lastName}
              <br />
              Email Address: {bodyData.emailAddress}
            </Text>
          </Box>
          {!success ? (
            <form onSubmit={handleSubmit}>
              <FormControl isInvalid={!!error}>
                <FormLabel htmlFor="amount">Amount (USD):</FormLabel>
                <Input
                  id="amount"
                  type="number"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                />
                {error && <FormErrorMessage>{error}</FormErrorMessage>}
                <FormHelperText>{loading ? 'Submitting...' : 'Please enter a valid amount.'}</FormHelperText>
              </FormControl>
              <Button
                mt={4}
                colorScheme="blue"
                type="submit"
                isLoading={loading} 
                isActive={parseFloat(amount) > 0} 
                isDisabled={loading || parseFloat(amount) <= 0}
              >
                Submit
              </Button>
            </form>) : (
              <Box bg="green.500" color="white" p={4} borderRadius="md">
                <Text fontSize="xl">Deposit successfully submitted!</Text>
                <Text fontSize="xl">Amount: {amount} USD</Text>
                <Button onClick={handleNewDeposit} colorScheme="teal" size="lg" mt={4}>Start Another Deposit</Button>
              </Box>
          )}
        </Box>
      );
};

export default MoneyForm;