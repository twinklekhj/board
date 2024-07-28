import {TokenState} from "../store/slice/token"; // Redux 스토어 타입

interface AjaxProps {
    url: string;
    method: 'GET' | 'POST' | 'PUT' | 'DELETE';
    body?: Object | {}
    token?: TokenState | undefined
}
const Ajax = async (props: AjaxProps): Promise<any> => {
    const initParams = {
        method: props.method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': ''
        },
        body: JSON.stringify({})
    };

    if(props.token) {
        initParams.headers.Authorization = `${props.token.bearerType} ${props.token.accessToken}`;
    }

    if(props.body && Object.keys(props.body).length > 0) {
        initParams.body = JSON.stringify(props.body)
    }

    console.log(initParams)

    return new Promise((resolve, reject) => {
        fetch(props.url, initParams)
            .then(response => {
                if(response.ok) {
                    return response;
                }
                return response.text().then(message => {
                    throw new Error(message);
                });
            })
            .then(response=> {
                resolve(response)
            })
            .catch(reason => reject(reason));
    });
};

export {Ajax};