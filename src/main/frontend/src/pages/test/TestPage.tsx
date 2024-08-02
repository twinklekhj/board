import React, {useMemo} from 'react';
import {Ajax} from "@Utils/ajax";

interface ApiInfo {

}
const TestPage = () => {
    const fetchData = useMemo(() => {
        Ajax({
            url: '/api/list',
            method: 'GET',
        })
        .then(res => res.json())
        .then(res => {
            console.log(res)
        });
    }, []);
    return (
        <div className={"test-container"}>
            Test 페이지 입니다
        </div>
    );
};

export default TestPage;