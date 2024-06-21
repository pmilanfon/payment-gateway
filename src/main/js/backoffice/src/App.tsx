import {
  Admin,
  Resource,

} from "react-admin";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { sv } from "date-fns/locale";
import { dataProvider } from "./dataProvider";
import { TransactionList } from "./components/TransactionList";
import { TransactionShow } from "./components/TransactionShow";

export const App = () => (
  <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={sv}>
    <Admin dataProvider={dataProvider}>
      <Resource
        name="transactions"
        list={TransactionList}

        show={TransactionShow}
      />
    </Admin>
  </LocalizationProvider>
);
