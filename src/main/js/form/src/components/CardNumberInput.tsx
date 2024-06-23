import React from 'react';
import {
  FormControl,
  FormLabel,
  Input,
  InputGroup,
  InputRightElement,
  Image,
  FormErrorMessage,
} from '@chakra-ui/react';
import { InputMask } from '@react-input/mask';
import { FormState } from './utils/FormState';
import paymentIcons from './utils/paymentIcons';

interface CardNumberInputProps {
  formState: FormState;
  validateCardNumber: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const imgSrcByType = {
  visa: paymentIcons[0],
  mastercard: paymentIcons[1],
  'american-express': paymentIcons[2],
  'diners-club': paymentIcons[3],
  discover: paymentIcons[4],
  jcb: paymentIcons[5],
  maestro: paymentIcons[6],
};

const CardNumberInput: React.FC<CardNumberInputProps> = ({
  formState,
  validateCardNumber,
}) => {
  return (
    <FormControl
      isInvalid={formState.cardNumber.isInvalid}
      px="4"
      pb="4"
      isRequired
    >
      <FormLabel
        fontSize={'sm'}
        color={formState.cardNumber.readyForSubmit ? 'default' : 'gray.500'}
      >
        Card Number
      </FormLabel>
      <InputGroup>
        <Input
          as={InputMask}
          mask={
            formState.cardNumber.cardType &&
            formState.cardNumber.cardType === 'american-express'
              ? '____ ______ _____'
              : '____ ____ ____ ____'
          }
          replacement={{ _: /\d/ }}
          type="text"
          placeholder="1234 5678 9012 3456"
          variant={'flushed'}
          onChange={validateCardNumber}
          onBlur={validateCardNumber}
        />
        <InputRightElement>
          <Image src={(imgSrcByType as never)[formState.cardNumber.cardType]} />
        </InputRightElement>
      </InputGroup>
      <FormErrorMessage>Card number is invalid</FormErrorMessage>
    </FormControl>
  );
};

export default CardNumberInput;
