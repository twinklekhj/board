import React from 'react';
import {InputGroup} from "@Components/inputs";
import {FaLock, FaUser} from "react-icons/fa6";
import topImage from '@Images/search-left-tablet.svg'
import bottomImage from '@Images/search-right-tablet.svg'
import faviconImage from '@Images/favicon.png'
import {useLogin} from "@Hooks/member";
import {Button} from "@mui/material";

const LoginPage = () => {
    const {onInputChange, onSubmitClick, onSignupClick} = useLogin();
    return (
        <div className={"login-container"}>
            <div className={"login-illustration"}>
                <img src={topImage} alt={"로그인 일러스트 top"}/>
                <img src={bottomImage} alt={"로그인 일러스트 bottom"}/>
            </div>
            <div className={"login-form"}>
                <form>
                    <div className={"login-title"}>
                        <img src={faviconImage} alt={"로고 이미지"}/>
                        <h1>Login</h1>
                    </div>
                    <InputGroup
                        type="text"
                        placeholder="아이디를 입력해주세요"
                        id={"userId"}
                        icon={<FaUser/>}
                        name={"ID"}
                        onChange={onInputChange}
                    />
                    <InputGroup
                        type="password"
                        placeholder="비밀번호를 입력해주세요"
                        id={"userPassword"}
                        icon={<FaLock/>}
                        name={"PW"}
                        onChange={onInputChange}
                    />
                    <button type={"submit"} onClick={onSubmitClick} className={"btn btn-login"}>Login</button>
                </form>
                <div>
                    아직 회원이 아니신가요? <Button onClick={onSignupClick}>회원가입 하러가기</Button>
                </div>
            </div>

        </div>

    );
};

export default LoginPage;