import { useEffect, useState } from "react";
import { Box, Text, Button } from "@chakra-ui/react";
import { SunIcon } from '@chakra-ui/icons'


const Receipt = () => {
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
  <Box bg="white" p={8} rounded="lg" fontSize="2xl">
    <SunIcon color="green.500" boxSize={12} />
    <Text color="green.500" fontWeight="bold" mb={4}>
        Card Submitted
    </Text>
    <Text color="green.500" fontSize="lg" mb={4}>
        Form will close in {countdown} seconds
    </Text>
    <Button
        colorScheme="white"
        variant="outline"
        mt={4}
        onClick={() => window.close()}
    >
        Close this page
    </Button>
</Box>
  );
};

export default Receipt;