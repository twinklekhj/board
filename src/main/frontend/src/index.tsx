import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './pages/index';
import reportWebVitals from './reportWebVitals';
import {persistor, store} from "./store";
import {PersistGate} from 'redux-persist/integration/react';
import {Provider} from "react-redux";

import '@Fonts/notokr/notokr.css'
import '@Fonts/default.css'
import '@Style/default.css'
import '@Style/mobile.css'
const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
      <Provider store={store}>
          <PersistGate loading={null} persistor={persistor}>
              <App />
          </PersistGate>
      </Provider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
