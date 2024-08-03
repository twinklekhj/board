import React from 'react';
import {useToken} from "@Hooks/token";
import LoadingPage from "@Pages/common/loading";
import LoginPage from "@Pages/member/login";

export interface TokenBoundaryProps {
    children: React.ReactNode;
}

const TokenBoundary = (props: TokenBoundaryProps) => {
    const [loading, authorized] = useToken();

    // 로딩 상태 및 인증 상태에 따른 렌더링
    if (loading) {
        return <LoadingPage />;
    }

    if (!authorized) {
        return <LoginPage />;
    }

    // 자식 컴포넌트 렌더링
    return (
        <>
            {props.children}
        </>
    );
};

export default TokenBoundary;