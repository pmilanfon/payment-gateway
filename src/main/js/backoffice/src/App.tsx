import {
  Admin,
  Resource,

} from "react-admin";
import { dataProvider } from "./dataProvider";
import { TransactionList } from "./components/TransactionList";
import { TransactionShow } from "./components/TransactionShow";

export const App = () => (
  <Admin dataProvider={dataProvider}>
    <Resource
      name="transactions"
      list={TransactionList}

      show={TransactionShow}
    />
  </Admin>
);
