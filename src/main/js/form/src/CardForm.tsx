import { Button, VStack } from '@chakra-ui/react';
import valid from 'card-validator';
import React, { useState } from 'react';
import CardHolderNameInput from './components/CardHolderNameInput';
import FormHeader from './components/FormHeader';
import CardNumberInput from './components/CardNumberInput';
import ExpirationDateInput from './components/ExpirationDateInput';
import CvvInput from './components/CvvInput';
import { FormState, initialFormState } from './components/utils/FormState';
import { AppState } from './store/types';
import { submitCardDetails } from './store/requests';
import { useTranslation } from 'react-i18next';

interface CardFormProps {
  setAppState: (appState: AppState) => void;
  reference: React.MutableRefObject<string>;
}

export const CardForm = ({ setAppState, reference }: CardFormProps ) => {
  const [formState, setFormState] = useState<FormState>(initialFormState);
  const { t } = useTranslation();

  const validateCardNumber = (e: React.ChangeEvent<HTMLInputElement>): void => {
    e.preventDefault();
    const value = valid.number(e.target.value);
    const isBlurEvent = e.type === 'blur';

    setFormState((prevState) => ({
      ...prevState,
      cardNumber: {
        ...prevState.cardNumber,
        isInvalid: !value[isBlurEvent ? 'isValid' : 'isPotentiallyValid'],
        cardNiceType: value.card ? value.card.niceType : '',
        cardType: value.card ? value.card.type : '',
        codeName: value?.card?.code ? value.card.code.name : '',
        codeSize: value?.card?.code ? value.card.code.size : 3,
        readyForSubmit: value.isValid,
        value: e.target.value,
      },
      ...(isBlurEvent
        ? {}
        : {
            cvv: {
              ...prevState.cvv,
              value: '',
              isInvalid: false,
              readyForSubmit: false,
            },
          }),
    }));
  };

  const validateExpirationDate = (
    e: React.ChangeEvent<HTMLInputElement>
  ): void => {
    e.preventDefault();
    const value = valid.expirationDate(e.target.value);
    const isBlurEvent = e.type === 'blur';

    setFormState((prevState) => ({
      ...prevState,
      expirationDate: {
        ...prevState.expirationDate,
        isInvalid: isBlurEvent ? !value.isValid : !value.isPotentiallyValid,
        readyForSubmit: value.isValid,
        value: e.target.value,
      },
    }));
  };

  const validateCvv = (e: React.ChangeEvent<HTMLInputElement>): void => {
    e.preventDefault();
    const isBlurEvent = e.type === 'blur';

    setFormState((prevState) => ({
      ...prevState,
      cvv: {
        ...prevState.cvv,
        isInvalid:
          (isBlurEvent &&
            e.target.value.length !== formState.cardNumber.codeSize) ||
          (!isBlurEvent &&
            e.target.value.length > formState.cardNumber.codeSize),
        readyForSubmit: e.target.value.length === formState.cardNumber.codeSize,
        value: e.target.value,
      },
    }));
  };

  const isFormReadyForSubmit = () => {
    return (
      formState.cardHolderName.readyForSubmit &&
      formState.cardNumber.readyForSubmit &&
      formState.expirationDate.readyForSubmit &&
      formState.cvv.readyForSubmit
    );
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    setAppState({name: 'LOADING', value: t("common.spinner.submitting")});

    try {
      const response = await submitCardDetails(formState, reference)

      if (!response.ok) {     
        throw new Error(response.statusText);
      }

      setAppState({name: 'RECEIPT'});
    } catch (error) {
      setAppState({name: 'ERROR', value: t("common.error.message")});
    }
  }

  const handleSubmitWrapper = (event: React.FormEvent) => {
    handleSubmit(event).catch((error) => {
      setAppState({name: 'ERROR', value: JSON.stringify(error)});
    });
  };

  return (
    <form onSubmit={handleSubmitWrapper} style={{ width: 350 }}>
      <VStack>
        <FormHeader />
        <CardHolderNameInput
          formState={formState}
          setFormState={setFormState}
        />
        <CardNumberInput
          formState={formState}
          validateCardNumber={validateCardNumber}
        />
        <ExpirationDateInput
          formState={formState}
          validateExpirationDate={validateExpirationDate}
        />
        <CvvInput formState={formState} validateCvv={validateCvv} />
        <Button
          type="submit"
          p="4"
          mx="4"
          mt="6"
          w="90%"
          colorScheme="blue"
          variant="solid"
          isDisabled={!isFormReadyForSubmit()}
        >
          {t("elements.cardForm.button.submit")}
        </Button>
      </VStack>
    </form>
  );
};

export default CardForm;
