import React, { useState } from 'react';

const MoneyForm: React.FC = () => {
    const [amount, setAmount] = useState<string>('');
    // const [merchantId, setMerchantId] = useState<string>('');
    // const [product, setProduct] = useState<string>('');
    // const [currency, setCurrency] = useState<string>('');
    // const [merchantTxRef, setMerchantTxRef] = useState<string>('');
    // const [orderDescription, setOrderDescription] = useState<string>('');
    // const [billingAddress, setBillingAddress] = useState<string>('');
    // const [address, setAddress] = useState<string>('');
    // const [transactionType, setTransactionType] = useState<string>('');
    // const [city, setCity] = useState<string>('');
    // const [state, setState] = useState<string>('');
    // const [postCode, setPostCode] = useState<string>('');
    // const [countryCode, setCountryCode] = useState<string>('');
    // const [emailAddress, setEmailAddress] = useState<string>('');
    // const [phoneNumber, setPhoneNumber] = useState<string>('');
    // const [ipAddress, setIpAddress] = useState<string>('');
    // const [locale, setLocale] = useState<string>('');
    // const [dateOfBirth, setDateOfBirth] = useState<string>('');
    // const [firstName, setFirstName] = useState<string>('');
    // const [lastName, setLastName] = useState<string>('');
    // const [callbackUrl, setCallbackUrl] = useState<string>('');
    // const [redirectUrl, setRedirectUrl] = useState<string>('');

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        const merchantTxRef = crypto.randomUUID()

        const bodyData = {
            amount: parseFloat(amount),
            "merchantId": "example_merchant",
            "product": "example_product",
            "currency": "USD",
            "merchantTxRef": merchantTxRef,
            "orderDescription": "Example deposit order",
            "billingAddress": "123 Main St",
            "address": "456 Secondary St",
            "transactionType": "DEPOSIT",
            "city": "Example City",
            "state": "Example State",
            "postCode": "12345",
            "countryCode": "US",
            "emailAddress": "example@example.com",
            "phoneNumber": "+1234567890",
            "ipAddress": "192.168.1.1",
            "locale": "en_US",
            "dateOfBirth": "1990-01-01",
            "firstName": "John",
            "lastName": "Doe",
            "callbackUrl": "http://localhost:9090/merchant/notification",
            "redirectUrl": "http://localhost:9090/merchant/callback"
        };

        try {
            const response = await fetch('http://localhost:7777/api/payments/deposit/initiate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(bodyData),
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const data = await response.json();
            //window.open(data.url);
            window.location.href = data.url

            const pollPaymentStatus = async () => {
                try {
                    const paymentStatusResponse = await fetch(`http://localhost:7777/api/payments/deposit/${merchantTxRef}`);
                    const paymentStatusData = await paymentStatusResponse.json();
    
                    if (paymentStatusData.currentState === 'DEPOSIT_SUCCESSFUL') {
                        console.log('SUCCESS')
                    } else {
                        console.log('POLL')
                        setTimeout(pollPaymentStatus, 3000);
                    }
                } catch (error) {
                    console.error('Error:', error);
                }
            };
    
            pollPaymentStatus();
        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Amount:
                <input 
                    type="number" 
                    value={amount} 
                    onChange={(e) => setAmount(e.target.value)} 
                />
            </label>
            <button type="submit">Submit</button>
        </form>
    );
};

export default MoneyForm;