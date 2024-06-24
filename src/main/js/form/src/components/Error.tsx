import React from 'react';
import { Box, Text, Button, HStack } from '@chakra-ui/react';
import { WarningIcon } from '@chakra-ui/icons'
import { ErrorProps } from '../store/types';
import { useTranslation } from 'react-i18next';

const Error: React.FC<ErrorProps> = ({ message, setAppState }) => {
  const { t } = useTranslation();
  const onRetry = () => {
      setAppState({ name: 'FORM', value: '' });
  }
  const onCancel = () => {
    window.close()
  }
  return (
    <Box textAlign="center" p={8} bg="white" borderRadius="md">
      <WarningIcon boxSize={16} color="red.600" />
      <Text fontSize="3xl" fontWeight="bold" mb={4} color="red.600">
        {t('elements.error.title')}
      </Text>
      <Text fontSize="lg" >{message}</Text>
      <HStack mt={6} justifyContent="center">
        <Button onClick={onRetry} colorScheme="green">
          {t('elements.error.button.retry')}
        </Button>
        <Button onClick={onCancel} colorScheme="red">
          {t('elements.error.button.close')}
        </Button>
      </HStack>
    </Box>
  );
};

export default Error;