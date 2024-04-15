import { Button, VStack } from '@chakra-ui/react';
import valid from 'card-validator';
import { useState } from 'react';
import CardHolderNameInput from './components/CardHolderNameInput';
import FormHeader from './components/FormHeader';
import CardNumberInput from './components/CardNumberInput';
import ExpirationDateInput from './components/ExpirationDateInput';
import CvvInput from './components/CvvInput';
import { FormState, initialFormState } from './components/utils/FormState';

export const PaymentForm = () => {
  const [formState, setFormState] = useState<FormState>(initialFormState);

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

  return (
    <form style={{ width: 350 }}>
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
          // onClick={}
          p="4"
          mx="4"
          mt="6"
          w="90%"
          colorScheme="blue"
          variant="solid"
          isDisabled={!isFormReadyForSubmit()}
        >
          Deposit
        </Button>
      </VStack>
    </form>
  );
};

export default PaymentForm;
