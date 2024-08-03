import React, {useEffect} from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import BoardPage from "@Pages/board/BoardPage";
import Signup from "@Pages/member/signup";
import TokenBoundary from "@Components/TokenBoundary";
import {Ajax} from "@Utils/ajax";
import {useDispatch} from "react-redux";
import {updateCsrfToken} from "@Store/slice/csrf";
import TestPage from "@Pages/test/TestPage";
import WritePage from "@Pages/board/WritePage";
import UpdatePage from "@Pages/board/UpdatePage";
import DetailPage from "@Pages/board/DetailPage";
import MyPage from "@Pages/member/mypage";

const App: React.FC = () => {
    const dispatch = useDispatch();
    useEffect(() => {
        Ajax({
            url: '/api/csrf-token',
            method: 'GET',
        }).then(res => res.json())
        .then(result => {
            dispatch(updateCsrfToken(result));
        }).catch(err => {
            console.error(err)
        })
    }, []);

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/signup" element={<Signup/>}/>
                <Route path="/test" element={<TestPage/>}/>
                <Route path="/" element={
                    <TokenBoundary>
                        <BoardPage/>
                    </TokenBoundary>
                }/>
                <Route path="/board/write" element={
                    <TokenBoundary>
                        <WritePage/>
                    </TokenBoundary>
                }/>
                <Route path="/board/:boardId/update" element={
                    <TokenBoundary>
                        <UpdatePage/>
                    </TokenBoundary>
                }/>
                <Route path="/board/:boardId" element={
                    <TokenBoundary>
                        <DetailPage/>
                    </TokenBoundary>
                }/>
                <Route path="/me" element={
                    <TokenBoundary>
                        <MyPage/>
                    </TokenBoundary>
                }/>

            </Routes>
        </BrowserRouter>
    );
};

export default App;
