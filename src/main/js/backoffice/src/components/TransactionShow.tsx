import {
  DateField,
  NumberField,
  EmailField,
  Show,
  SimpleShowLayout,
  TextField,
} from "react-admin";

export const TransactionShow = () => (
  <Show>
    <SimpleShowLayout>
      {/* <ReferenceField source="merchantId" reference="merchants" />
            <ReferenceField source="transactionId" reference="transactions" /> */}
      <TextField source="merchantId" />
      <TextField source="transactionId" />
      <DateField source="created" />
      <TextField source="state" />
      <NumberField source="status" />
      <NumberField source="amount" />
      <NumberField source="fee" />
      <DateField source="updated" />
      <TextField source="userIp" />
      <TextField source="channel" />
      <EmailField source="userEmail" />
      <TextField source="userCountry" />
      <TextField source="userAccount" />
    </SimpleShowLayout>
  </Show>
);
