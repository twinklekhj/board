import {configureStore} from "@reduxjs/toolkit";
import TokenSlice from "./slice/token";

export const store = configureStore({
    reducer: {
        token: TokenSlice.reducer
    }
})
export type RootState = ReturnType<typeof store.getState>