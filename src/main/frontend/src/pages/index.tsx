import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import BoardPage from "@Pages/board/BoardPage";
import SignupPage from "@Pages/member/SignupPage";
import TokenBoundary from "@Components/TokenBoundary";
import WritePage from "@Pages/board/WritePage";
import UpdatePage from "@Pages/board/UpdatePage";
import DetailPage from "@Pages/board/DetailPage";
import MyPage from "@Pages/member/MyPage";
import TestPage from "@Pages/test/TestPage";

const App: React.FC = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/signup" element={<SignupPage />} />
                <Route path="/test" element={<TestPage />} />
                <Route path="/" element={
                    <TokenBoundary>
                        <BoardPage />
                    </TokenBoundary>
                } />
                <Route path="/board/write" element={
                    <TokenBoundary>
                        <WritePage />
                    </TokenBoundary>
                } />
                <Route path="/board/:boardId/update" element={
                    <TokenBoundary>
                        <UpdatePage />
                    </TokenBoundary>
                } />
                <Route path="/board/:boardId" element={
                    <TokenBoundary>
                        <DetailPage />
                    </TokenBoundary>
                } />
                <Route path="/me" element={
                    <TokenBoundary>
                        <MyPage />
                    </TokenBoundary>
                } />

            </Routes>
        </BrowserRouter>
    );
};

export default App;
