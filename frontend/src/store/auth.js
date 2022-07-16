import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    tokens: {
        authToken: "",
        refreshToken: ""
    },

    loggedIn: false
};

const authSlice = createSlice({
    name: 'auth',
    initialState,

    reducers: {
        login(state, action) {
            const { authToken, refreshToken } = action.payload;

            state.tokens.authToken = authToken;
            state.tokens.refreshToken = refreshToken;
            state.loggedIn = true;
        },

        logout(state) {
            state.tokens.authToken = "";
            state.tokens.refreshToken = "";
            state.loggedIn = false;
        }
    }
});

export const authActions = authSlice.actions;
export default authSlice;