
export interface DepositInitiateResponse {
    url: string,
    reference: string,
}

export interface CardDetailsSubmit {
    cardNumber: string,
    cardHolderName: string,
    expirationMonth: number,
    expirationYear: number,
    cvv: number,
    reference: string,
}

export interface AppState {
    name: string,
    value?: string,
}

export interface ErrorProps  {
    message?: string;
    setAppState: (appState: AppState) => void;
}

export interface ReceiptProps {
    t: (key: string) => string
}

