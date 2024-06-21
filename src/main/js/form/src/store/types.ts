
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