import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import dayjs from "dayjs";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import {
  Datagrid,
  DateField,
  DateInput,
  EmailField,
  List,
  NumberField,
  SearchInput,
  SelectInput,
  TextField,
} from "react-admin";

// const choices = [
//   { id: 123, first_name: "Leo", last_name: "Tolstoi" },
//   { id: 456, first_name: "Jane", last_name: "Austen" },
// ];

const transactionFilters = [
  <SearchInput source="q" alwaysOn key={"q"} />,
  // <TextInput label="Title" source="title" defaultValue="Hello, World!" />,
  <DateInput label="From Date" source="fromDate" alwaysOn parse={(date) => (date ? date.toISOString() : null)} />,
  <DateInput label="To Date" source="toDate" alwaysOn parse={(date) => (date ? date.toISOString() : null)} />
  //<DatePicker label="fromDate" />

];

export const TransactionList = () => (
  <List 
    sort={{ field: 'fromDate', order: 'DESC' }} 
    filters={transactionFilters} 
    filterDefaultValues={{ fromDate: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000), toDate: new Date() }}>
    <Datagrid rowClick="show">
      {/* <ReferenceField source="merchantId" reference="merchants" />
            <ReferenceField source="transactionId" reference="transactions" /> */}
      <TextField source="merchantTxRef" />
      <TextField source="reference" />
      <TextField source="cardHolderName" />
      <TextField source="state" />
      <DateField source="fromDate" />
      <DateField source="toDate" />
    </Datagrid>
  </List>
);
