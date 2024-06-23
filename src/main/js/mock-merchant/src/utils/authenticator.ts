export const authenticate = async () => {
    const merchantName = 'merchant1'
    let accessToken = ''
    let merchantId = ''

    const authenticateBody = {
        "email": "test",
        "password": "test"
    }

    const createMerchantBody = {
        "name": merchantName
    }

    const registerBody = {
        "firstName": "firstName",
        "lastName": "lastName",
        "email": "email",
        "password": "password",
        "role": "MERCHANT_ADMIN",
        "merchant": merchantName
    }
    try {
        const authResponse = await fetch('http://localhost:7777/authenticate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(authenticateBody),
        });

        if (!authResponse.ok) {
            throw new Error('Auth response was not ok');
        }

        const data = await authResponse.json();
        accessToken = data.accessToken;
    } catch (error) {
        console.error('Error:', error);
    }

    try {
        const createMerchantResponse = await fetch('http://localhost:7777/create-merchant', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(createMerchantBody),
        });

        if (!createMerchantResponse.ok) {
            throw new Error('Create merchant response was not ok');
        }

        const data = await createMerchantResponse.json();
        merchantId = data.merchantId;
    } catch (error) {
        console.error('Error:', error);
    }

    try {
        const registerResponse = await fetch('http://localhost:7777/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(registerBody),
        });

        if (!registerResponse.ok) {
            throw new Error('Register response was not ok');
        }
    } catch (error) {
        console.error('Error:', error);
    }


    try {
        const tokenBody = `${encodeURIComponent('client_id')}=${encodeURIComponent(merchantId)}&${encodeURIComponent('client_secret')}=${encodeURIComponent(12345)}&${encodeURIComponent('grant_type')}=${encodeURIComponent('clients_credentials')}`;

        const tokenResponse = await fetch('http://localhost:7777/oauth2/token', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
            },
            body: tokenBody,
        });

        if (!tokenResponse.ok) {
            throw new Error('Token response was not ok');
        }

        const data = await tokenResponse.json();
        accessToken = data.accessToken;
    } catch (error) {
        console.error('Error:', error);
    }
    return accessToken;
    
  }