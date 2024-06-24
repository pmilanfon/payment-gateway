import { useEffect, useState } from "react";
import { Box, Text, Button, useColorModeValue } from "@chakra-ui/react";
import { SunIcon } from '@chakra-ui/icons'
import { useTranslation } from "react-i18next";

const Receipt = () => {
  const { t } = useTranslation();
  const [countdown, setCountdown] = useState(10);

  useEffect(() => {
    let timer: string | number | NodeJS.Timeout | undefined;
    if (countdown > 0) {
      timer = setTimeout(() => {
        setCountdown(countdown - 1);
      }, 1000);
    } else {
      window.close();
    }
    return () => clearTimeout(timer);
  }, [countdown]);

  return (
  <Box 
    p={8} 
    fontSize="2xl" 
    bg={useColorModeValue("white", "gray.800")}
    shadow="md"
    rounded="lg">
    <SunIcon color="green.500" boxSize={16} mb={8}/>
    <Text fontSize="3xl" color="green.500" fontWeight="bold" mb={4}>
        {t('elements.receipt.text.submitted')}
    </Text>
    <Text fontSize="lg" color="green.500" mb={8}>
      {t('elements.receipt.text.countdown', { countdown })}
    </Text>
    <Button
        colorScheme="white"
        variant="outline"
        mt={4}
        onClick={() => window.close()}
    >
        {t('elements.receipt.button.close')}
    </Button>
</Box>
  );
};

export default Receipt;