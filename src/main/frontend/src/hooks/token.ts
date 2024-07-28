import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../store";
import {useCallback, useEffect, useState} from "react";
import {Ajax} from "@Utils/ajax";
import {initToken} from "@Store/slice/token";

export function useToken(): [boolean, boolean] {
    const [loading, setLoading] = useState(false);
    const [authorized, setAuthorized] = useState(true);

    const dispatch = useDispatch();
    const accessToken = useSelector((state: RootState) => state.token).accessToken;
    const bearerType = useSelector((state: RootState) => state.token).bearerType;
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
                }
            }).then(res => {
                setLoading(false);
                setAuthorized(true);
            }).catch(err => {
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