import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../store";
import {useCallback, useEffect, useState} from "react";
import {Ajax} from "@Utils/ajax";
import {initToken} from "@Store/slice/token";
import {updateMember} from "@Store/slice/member";

export function useToken(): [boolean, boolean] {
    const [loading, setLoading] = useState(true);
    const [authorized, setAuthorized] = useState(false);

    const dispatch = useDispatch();
    const accessToken = useSelector((state: RootState) => state.token).accessToken;
    const bearerType = useSelector((state: RootState) => state.token).bearerType;
    const csrfToken = useSelector((state: RootState) => state.csrf)

    const validToken = useCallback(() => {
        setLoading(true);

        if (accessToken === '') {
            setLoading(false);
            setAuthorized(false);
        } else {
            Ajax({
                url: "/api/validate",
                method: "POST",
                token: {
                    accessToken: accessToken,
                    bearerType: bearerType
                },
                csrf: csrfToken
            }).then(res => {
                return res.json();
            }).then(result => {
                console.log(result)
                dispatch(updateMember({
                    id: result.id,
                    name: result.name,
                    email: result.email,
                    imageUrl: result.imageUrl,
                    userId: result.userId
                }))
                setLoading(false);
                setAuthorized(true);
            })
                .catch(err => {
                setLoading(false);
                setAuthorized(false);
                dispatch(initToken())
            });
        }
    }, [accessToken, bearerType, dispatch])

    useEffect(() => {
        validToken();
    }, [validToken]);

    return [loading, authorized];
}