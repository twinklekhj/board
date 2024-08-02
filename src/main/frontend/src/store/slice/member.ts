import {createSlice, PayloadAction} from "@reduxjs/toolkit";

interface MemberState {
    id: number;
    name: string;
    email: string;
}
const MemberSlice = createSlice({
    name: "member",
    initialState: {
        id: 0,
        name: '',
        email: '',
    },
    reducers: {
        updateMember(state, action: PayloadAction<MemberState>) {
            state.id = action.payload.id;
            state.name = action.payload.name;
            state.email = action.payload.email;
        }
    }
})

export const {updateMember} = MemberSlice.actions;
export default MemberSlice;