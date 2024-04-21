export interface FormState {
  cardHolderName: {
    isInvalid: boolean;
    readyForSubmit: boolean;
  };
  cardNumber: {
    isInvalid: boolean;
    cardNiceType: string;
    cardType: string;
    codeName: string;
    codeSize: number;
    readyForSubmit: boolean;
  };
  expirationDate: {
    isInvalid: boolean;
    readyForSubmit: boolean;
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
  },
  cardNumber: {
    isInvalid: false,
    cardNiceType: '',
    cardType: '',
    codeName: '',
    codeSize: 3,
    readyForSubmit: false,
  },
  expirationDate: {
    isInvalid: false,
    readyForSubmit: false,
  },
  cvv: {
    isInvalid: false,
    readyForSubmit: false,
    value: '',
  },
};