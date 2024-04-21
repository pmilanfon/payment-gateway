import {
  Datagrid,
  DateField,
  EmailField,
  List,
  NumberField,
  SearchInput,
  TextField,
} from "react-admin";

// const choices = [
//   { id: 123, first_name: "Leo", last_name: "Tolstoi" },
//   { id: 456, first_name: "Jane", last_name: "Austen" },
// ];

const transactionFilters = [
  <SearchInput source="q" alwaysOn key={"q"} />,
  // <TextInput label="Title" source="title" defaultValue="Hello, World!" />,
//   <SelectInput source="state" key={"state"} />,

];

export const TransactionList = () => (
  <List sort={{ field: 'updated', order: 'DESC' }} filters={transactionFilters}>
    <Datagrid rowClick="show">
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
    </Datagrid>
  </List>
);
