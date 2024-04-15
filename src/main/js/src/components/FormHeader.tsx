import { HStack, Image, VStack } from '@chakra-ui/react';
import payceLogo from '../assets/payce-logo-transparent.svg';
import paymentIcons from './utils/paymentIcons';

const FormHeader = () => {
  return (
    <VStack>
      <Image src={payceLogo} />
      <HStack>
        {paymentIcons.map((icon: string, index: number) => (
          <Image key={index} src={icon} boxSize="25px" />
        ))}
      </HStack>
    </VStack>
  );
};

export default FormHeader;