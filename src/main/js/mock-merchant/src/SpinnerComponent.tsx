import { Spinner, Text } from "@chakra-ui/react";

interface SpinnerComponentProps {
  text: string;
}

const SpinnerComponent = ({ text }: SpinnerComponentProps) => {
  return (
    <div>
      <Spinner size="xl" />
      <Text mt={4}>{text}</Text>
    </div>
  );
};

export default SpinnerComponent;