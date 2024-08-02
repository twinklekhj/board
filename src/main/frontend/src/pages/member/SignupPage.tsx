import React from 'react';
import {InputGroup} from "@Components/inputs";
import {FaLock, FaUser} from "react-icons/fa6";
import topImage from '@Images/search-left-tablet.svg'
import bottomImage from '@Images/search-right-tablet.svg'
import faviconImage from '@Images/favicon.png'
import {useLogin} from "@Hooks/member";
import {Button} from "@mui/material";

const SignupPage = () => {
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
                        <h1>Sign Up</h1>
                    </div>
                    <InputGroup
                        type="text"
                        placeholder="사용하실 아이디를 입력해주세요"
                        id={"userId"}
                        icon={<FaUser/>}
                        name={"ID"}
                        onChange={onInputChange}
                    />
                    <InputGroup
                        type="password"
                        placeholder="사용하실 비밀번호를 입력해주세요"
                        id={"userPassword"}
                        icon={<FaLock/>}
                        name={"PW"}
                        onChange={onInputChange}
                    />

                    <InputGroup
                        type="password"
                        placeholder="비밀번호 확인을 입력해주세요"
                        id={"userPasswordConfirm"}
                        icon={<FaLock/>}
                        name={"PW"}
                        onChange={onInputChange}
                    />
                    <button type={"submit"} onClick={onSubmitClick} className={"btn btn-login"}>Sign Up</button>
                </form>
            </div>

        </div>

    );
};

export default SignupPage;