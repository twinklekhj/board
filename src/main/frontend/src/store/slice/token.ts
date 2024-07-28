import {createSlice, PayloadAction} from "@reduxjs/toolkit";

export interface TokenState {
    bearerType: string;
    accessToken: string;
}

const TokenSlice = createSlice({
    name: "token",
    initialState: {
        bearerType: '',
        accessToken: '',
    },
    reducers: {
        initToken: (state) => {
            state.bearerType = '';
            state.accessToken = '';
        },
        updateToken: (state, action: PayloadAction<TokenState>) => {
            state.bearerType = action.payload.bearerType;
            state.accessToken = action.payload.accessToken;
        },

    }
});

export const {initToken, updateToken} = TokenSlice.actions;
export default TokenSlice;