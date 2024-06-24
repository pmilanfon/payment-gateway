import { FormState } from "../components/utils/FormState"
import { getCardDetailsSubmit } from "./utils";

export const submitCardDetails = async (formState: FormState, reference: React.MutableRefObject<string>): Promise<Response> => {
    return await fetch('http://localhost:7777/api/payments/deposit/cardDetails', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(getCardDetailsSubmit(formState, reference.current)),
    });
}