import React from 'react';
import TokenBoundary from "@Components/TokenBoundary";
import BoardPage from "./board/BoardPage";

const App = () => {
    return (
        <TokenBoundary>
            <BoardPage />
        </TokenBoundary>
    );
};

export default App;