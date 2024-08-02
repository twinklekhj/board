import React from 'react';
import Header from "@Layout/header";

const MyPage = () => {
    return (
        <>
            <Header title={"마이페이지"} usePreviousButton={true} />
            <div className={"content"}>
                <div className={"inner"}>
                    마이페이지입니다.
                </div>
            </div>
        </>
    );
};

export default MyPage;