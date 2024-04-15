import { Button, VStack } from '@chakra-ui/react';
import valid from 'card-validator';
import { useEffect, useState } from 'react';
import CardHolderNameInput from './components/CardHolderNameInput';
import FormHeader from './components/FormHeader';
import CardNumberInput from './components/CardNumberInput';
import ExpirationDateInput from './components/ExpirationDateInput';
import CvvInput from './components/CvvInput';
import { FormState, initialFormState } from './components/utils/FormState';
import { LoadingSpinner } from './components/LoadingSpinner';

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const PaymentForm = ({setAppState, appState}: any) => {
  const [formState, setFormState] = useState<FormState>(initialFormState);

  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 1500);

    return () => clearTimeout(timer);
  }, []);
  console.error(appState.amount)
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
        codeName: value.card && value.card.code ? value.card.code.name : '',
        codeSize: value.card && value.card.code ? value.card.code.size : 3,
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
    isLoading ? <LoadingSpinner text={'Connecting to provider...'} /> :
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
          onClick={() =>setAppState({
            mockMerchantVisible: false,
            receiptVisible: appState.amount !== 42 ? true : false,
            errorVisible: appState.amount === 42 ? true : false,
            paymentFormVisible: false,
            amount: appState.amount,
            amountAfterFee: appState.amountAfterFee
          })}
          p="4"
          mx="4"
          mt="6"
          w="90%"
          colorScheme="blue"
          variant="solid"
          isDisabled={!isFormReadyForSubmit()}
        >
          Deposit - {appState.amountAfterFee.toFixed(2)}$
        </Button>
      </VStack>
    </form>
  );
};

export default PaymentForm;
