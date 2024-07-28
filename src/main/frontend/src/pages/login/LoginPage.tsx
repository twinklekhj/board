import React from 'react';
import {InputGroup} from "@Components/inputs";
import {FaLock, FaUser} from "react-icons/fa6";
import topImage from '@Images/search-left-tablet.svg'
import bottomImage from '@Images/search-right-tablet.svg'
import faviconImage from '@Images/favicon.png'
import {Ajax} from "@Utils/ajax";
import AlertUtil from "@Utils/alert";
import {useDispatch} from "react-redux";
import {updateToken} from "@Store/slice/token";

const LoginPage = () => {
    const dispatch = useDispatch();
    const [params, setParams] = React.useState({
        userId: '',
        userPassword: ''
    })

    const onChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {id, value} = event.currentTarget;
        setParams(prevParams => ({
            ...prevParams,
            [id]: value
        }));
    }
    const onSubmit = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        if (params.userId === '') {
            AlertUtil.warning({
                text: "Please enter your id!!",
            });
            return false;
        }
        if (params.userPassword === '') {
            AlertUtil.warning({
                text: "Please enter your password!!",
            });
            return false;
        }

        Ajax({
            url: '/api/authenticate',
            method: 'POST',
            body: {
                id: params.userId,
                password: params.userPassword,
            },
        })
            .then(res => res.json())
            .then(data => {
                dispatch(updateToken({
                    bearerType: data.grantType,
                    accessToken: data.accessToken,
                }))
            })
            .catch(reason => {
                AlertUtil.warning({
                    text: reason.message
                })
                console.error(reason)
            })
    }
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
                        <h1>Login Page</h1>
                    </div>
                    <InputGroup
                        type="text"
                        placeholder="Enter your id"
                        id={"userId"}
                        icon={<FaUser/>}
                        name={"ID"}
                        onChange={onChange}
                    />
                    <InputGroup
                        type="password"
                        placeholder="Enter your password"
                        id={"userPassword"}
                        icon={<FaLock/>}
                        name={"PW"}
                        onChange={onChange}
                    />
                    <button type={"submit"} onClick={onSubmit} className={"btn btn-login"}>Login</button>
                </form>
            </div>
        </div>

    );
};

export default LoginPage;