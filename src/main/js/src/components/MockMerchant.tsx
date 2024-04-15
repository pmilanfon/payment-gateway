import {
  Button,
  Divider,
  FormControl,
  FormLabel,
  Heading,
  NumberInput,
  NumberInputField,
  VStack,
  TableContainer,
  Table,
  Tbody,
  Td,
  Tfoot,
  Th,
  Tr,
  Box,
} from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import { LoadingSpinner } from './LoadingSpinner';

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const MockMerchant = ({ setAppState }: any) => {
  const [amount, setAmount] = useState<number>();
  const feeAmount = amount ? amount * 0.01 : 0;
  const amountAfterFee = amount ? amount - feeAmount : 0;
  const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
      const timer = setTimeout(() => {
        setIsLoading(false);
      }, 1000);
  
      return () => clearTimeout(timer);
    }, []);
  return (
    isLoading ? <LoadingSpinner text="Loading" /> :
    <>
      <Box>
      <Heading fontFamily="monospace" color="orange.400" fontSize={'7xl'}>
        MockBet
      </Heading>
      <Divider my="2" />
      <Heading fontWeight={'bold'} color="blue.700" fontSize={'4xl'}>
       PayCE
      </Heading>
      <Divider my="5" />
       
        <VStack>
        <Box bg={'gray.30'} maxW='250px' borderRadius='lg'>
          <FormControl p="4" isRequired>
            <FormLabel fontWeight="bold" color="black.400" fontSize="lg">Amount ($)</FormLabel>

            <NumberInput
              onChange={(valueString) => setAmount(+valueString)}
              variant="flushed"
              value={amount}
            >
              <NumberInputField fontSize={'md'} placeholder={amount ? amount.toString() : 'Enter amount'}/>
            </NumberInput>
          </FormControl>
          <Divider my="4" />
          <TableContainer>
          <Table fontSize={'xl'} size={'sm'} variant='unstyled'>
            <Tbody>
              <Tr>
                <Td>Amount</Td>
                <Td isNumeric>{amount ? amount : 0} $</Td>
              </Tr>
              <Tr>
                <Td >Fee (1%)</Td>
                <Td isNumeric>{feeAmount.toFixed(2)} $</Td>
              </Tr>
            </Tbody>
            <Tfoot>
              <Tr>
                <Th>Total Amount</Th>
                <Th isNumeric>{amountAfterFee.toFixed(2)} $</Th>
              </Tr>
            </Tfoot>
          </Table>
        </TableContainer>
        </Box>
          <Divider my="4" />
          <Button
            p="4"
            mx="4"
            mt="6"
            w="100%"
            colorScheme="blue"
            size={'lg'}
            fontSize={'lg'}
            variant="solid"
            isDisabled={!(amount && amount > 0)}
            onClick={() =>
              setAppState({
                mockMerchantVisible: false,
                receiptVisible: false,
                errorVisible: false,
                paymentFormVisible: true,
                amount: amount,
                amountAfterFee: amountAfterFee,
              })
            }
          >
            Deposit
          </Button>
        </VStack>
        </Box>
    </>
  );
};
