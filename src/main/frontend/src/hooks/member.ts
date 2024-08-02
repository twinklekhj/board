import {useDispatch, useSelector} from "react-redux";
import React from "react";
import AlertUtil from "@Utils/alert";
import {Ajax} from "@Utils/ajax";
import {updateToken} from "@Store/slice/token";
import {useNavigate} from "react-router-dom";
import {RootState} from "@Store/index";

export const useLogin = function () {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const csrfToken = useSelector((state: RootState) => state.csrf)
    const [params, setParams] = React.useState({
        userId: '',
        userPassword: ''
    })

    // 아이디 비밀번호 입력
    const onInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {id, value} = event.currentTarget;
        setParams(prevParams => ({
            ...prevParams,
            [id]: value
        }));
    }

    // 로그인 버튼 클릭
    const onSubmitClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        if (params.userId === '') {
            AlertUtil.warning({
                text: "아이디를 입력해주세요!",
            });
            return false;
        }
        if (params.userPassword === '') {
            AlertUtil.warning({
                text: "비밀번호를 입력해주세요!",
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
            csrf: csrfToken,
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

    const onSignupClick = () => {
        navigate('/signup');
    }
    return {onInputChange, onSubmitClick, onSignupClick}
}