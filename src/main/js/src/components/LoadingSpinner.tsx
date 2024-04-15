import {
    Spinner,
    StackDivider,
    VStack,
  } from "@chakra-ui/react";

  export interface LoadingSpinnerProps {
    text: string;
  }

  export const LoadingSpinner = ({text}: LoadingSpinnerProps) => {

    return (
        <VStack 
        divider={<StackDivider borderColor='gray.200' />}
        spacing={24}
        align='stretch'>
          <h1>{text}</h1>
          <h1>
            <span>
              <Spinner
                thickness="10px"
                speed="0.65s"
                emptyColor="gray.200"
                color="blue.500"
                size="xl"
              />
            </span>
          </h1>
        </VStack>
    );
  };
  