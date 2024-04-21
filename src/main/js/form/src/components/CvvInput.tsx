import React from 'react';
import {
  FormControl,
  FormLabel,
  Input,
  FormErrorMessage,
} from '@chakra-ui/react';
import { InputMask } from '@react-input/mask';
import { FormState } from './utils/FormState';

interface CvvInputProps {
  formState: FormState;
  validateCvv: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

const CvvInput: React.FC<CvvInputProps> = ({ formState, validateCvv }) => {
  return (
    <FormControl isInvalid={formState.cvv.isInvalid} px="4" pb="4" isRequired>
      <FormLabel
        fontSize={'sm'}
        color={formState.cvv.readyForSubmit ? 'default' : 'gray.500'}
      >
        {formState.cardNumber.codeName ? formState.cardNumber.codeName : 'CVV'}
      </FormLabel>
      <Input
        as={InputMask}
        mask={
          formState.cardNumber.codeSize
            ? formState.cardNumber.codeSize === 4
              ? '____'
              : '___'
            : '___'
        }
        replacement={{ _: /\d/ }}
        type="text"
        value={formState.cvv.value}
        placeholder={
          formState.cardNumber.codeSize
            ? formState.cardNumber.codeSize + ' digits'
            : '3 digits'
        }
        variant={'flushed'}
        onChange={validateCvv}
        onBlur={validateCvv}
      />
      <FormErrorMessage>Cvv number is invalid</FormErrorMessage>
    </FormControl>
  );
};

export default CvvInput;

