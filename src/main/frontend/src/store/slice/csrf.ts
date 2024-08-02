import {createSlice, PayloadAction} from "@reduxjs/toolkit";

export interface CsrfTokenState {
    headerName: string;
    token: string;
}
const CsrfTokenSlice = createSlice({
    name: "csrf",
    initialState: {
        headerName: '',
        token: ''
    },
    reducers: {
        updateCsrfToken: (state, action: PayloadAction<CsrfTokenState>) => {
            state.headerName = action.payload.headerName;
            state.token = action.payload.token;
        },
        initCsrfToken: (state) => {
            state.token = '';
            state.headerName = '';
        }
    }
})

export const {updateCsrfToken, initCsrfToken} = CsrfTokenSlice.actions;
export default CsrfTokenSlice;