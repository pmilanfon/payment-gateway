import React from 'react';
import {
  FormControl,
  FormLabel,
  Input,
  FormErrorMessage,
} from '@chakra-ui/react';
import valid from 'card-validator';
import { useTranslation } from 'react-i18next';
import { FormState } from './utils/FormState';

interface CardHolderNameInputProps {
  formState: FormState;
  setFormState: React.Dispatch<React.SetStateAction<FormState>>;
}

const CardHolderNameInput: React.FC<CardHolderNameInputProps> = ({
  formState,
  setFormState,
}) => {
  const { t } = useTranslation();
  const { cardHolderName } = formState;

  const handleCardholderNameChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ): void => {
    e.preventDefault();
    const isBlurEvent = e.type === 'blur';
    const inputValue = e.target.value;
    const validation = valid.cardholderName(inputValue);
    setFormState((prevState) => ({
      ...prevState,
      cardHolderName: {
        ...prevState.cardHolderName,
        isInvalid: isBlurEvent
          ? !validation.isValid
          : !validation.isPotentiallyValid,
        readyForSubmit: validation.isValid,
      },
    }));
  };

  return (
    <FormControl isInvalid={cardHolderName?.isInvalid} p="4" isRequired>
      <FormLabel
        fontSize="sm"
        color={cardHolderName?.readyForSubmit ? 'default' : 'gray.500'}
      >
        {t('cardHolderNameInput.label')}
      </FormLabel>

      <Input
        type="text"
        placeholder={t('cardHolderNameInput.placeholder')}
        variant="flushed"
        onChange={handleCardholderNameChange}
        onBlur={handleCardholderNameChange}
      />
      <FormErrorMessage>{t('cardHolderNameInput.errorMessage')}</FormErrorMessage>
    </FormControl>
  );
};

export default CardHolderNameInput;
