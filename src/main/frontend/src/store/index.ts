import {combineReducers, configureStore} from "@reduxjs/toolkit";
import {persistReducer, persistStore} from "redux-persist";
import TokenSlice from "./slice/token";
import sessionStorage from 'redux-persist/lib/storage';
import MemberSlice from "@Store/slice/member";

const persistedConfig = {
    key: "root",
    storage: sessionStorage,
    whitelist: ["token"],
};
const rootReducers = combineReducers({
    token: TokenSlice.reducer,
    member: MemberSlice.reducer
})
const persistedReducer = persistReducer(persistedConfig, rootReducers);
export const store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: false,
        }),
});
export const persistor = persistStore(store);
export type RootState = ReturnType<typeof store.getState>