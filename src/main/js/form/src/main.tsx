import React from 'react';
import ReactDOM from 'react-dom/client';
import { ChakraProvider } from '@chakra-ui/react';
import App from './App.tsx';
import './index.css';
import i18n from 'i18next';
import { initReactI18next, I18nextProvider } from 'react-i18next';

import enTranslation from './translations/en.json';

i18n.use(initReactI18next).init({
  resources: {
    en: {
      translation: enTranslation,
    },
  },
  lng: 'en',
  fallbackLng: 'en',
  interpolation: {
    escapeValue: false,
    formatSeparator: ',',
  },
}).catch((err) => console.log(err));

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <ChakraProvider>
      <I18nextProvider i18n={i18n}>
        <App />
      </I18nextProvider>
    </ChakraProvider>
  </React.StrictMode>
);
