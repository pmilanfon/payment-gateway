export interface FormState {
  cardHolderName: {
    isInvalid: boolean;
    readyForSubmit: boolean;
    value: string;
  };
  cardNumber: {
    isInvalid: boolean;
    cardNiceType: string;
    cardType: string;
    codeName: string;
    codeSize: number;
    readyForSubmit: boolean;
    value: string;
  };
  expirationDate: {
    isInvalid: boolean;
    readyForSubmit: boolean;
    value: string;
  };
  cvv: {
    isInvalid: boolean;
    readyForSubmit: boolean;
    value: string;
  };
}

export const initialFormState: FormState = {
  cardHolderName: {
    isInvalid: false,
    readyForSubmit: false,
    value: '',
  },
  cardNumber: {
    isInvalid: false,
    cardNiceType: '',
    cardType: '',
    codeName: '',
    codeSize: 3,
    readyForSubmit: false,
    value: '',
  },
  expirationDate: {
    isInvalid: false,
    readyForSubmit: false,
    value: '',
  },
  cvv: {
    isInvalid: false,
    readyForSubmit: false,
    value: '',
  },
};