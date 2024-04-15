// ExpirationDateInput.tsx
import React from 'react';
import {
  FormControl,
  FormLabel,
  Input,
  FormErrorMessage,
} from '@chakra-ui/react';
import { InputMask } from '@react-input/mask';
import { FormState } from './utils/FormState';

interface ExpirationDateInputProps {
  formState: FormState;
  validateExpirationDate: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const ExpirationDateInput: React.FC<ExpirationDateInputProps> = ({
  formState,
  validateExpirationDate,
}) => {
  return (
    <FormControl
      isInvalid={formState.expirationDate.isInvalid}
      px="4"
      pb="4"
      isRequired
    >
      <FormLabel
        fontSize={'sm'}
        color={
          formState.expirationDate.readyForSubmit ? 'default' : 'gray.500'
        }
      >
        Expiration Date
      </FormLabel>
      <Input
        as={InputMask}
        mask={'__/__'}
        replacement={{ _: /\d/ }}
        type="text"
        placeholder={'MM/YY'}
        variant={'flushed'}
        onChange={validateExpirationDate}
        onBlur={validateExpirationDate}
      />
      <FormErrorMessage>Expiration date is invalid</FormErrorMessage>
    </FormControl>
  );
};

export default ExpirationDateInput;