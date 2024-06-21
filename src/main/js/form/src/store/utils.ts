import { FormState } from "../components/utils/FormState";
import { CardDetailsSubmit } from "./types";

export const getCardDetailsSubmit = (formState: FormState): CardDetailsSubmit => {
    const expirationDate = formState.expirationDate.value;
    const [month, year] = expirationDate.split('/').map(Number);
    return {
        cardHolderName: formState.cardHolderName.value,
        cardNumber: formState.cardNumber.value,
        expirationMonth: month,
        expirationYear: year,
        cvv: parseInt(formState.cvv.value ?? '0'),
        reference: window.location.pathname.split('/').pop() ?? '',
    };
}