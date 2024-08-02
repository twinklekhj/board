import {TokenState} from "@Store/slice/token"; // Redux 스토어 타입

interface AjaxProps {
    url: string;
    method: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
    body?: Object | {}
    token?: TokenState | undefined
}
const Ajax = async (props: AjaxProps): Promise<any> => {
    const initParams = props.method === 'GET' ? {
        method: props.method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': ''
        }
    }: {
        method: props.method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': ''
        },
        body: JSON.stringify(props.body)
    };

    if(props.token) {
        initParams.headers.Authorization = `${props.token.bearerType} ${props.token.accessToken}`;
    }

    if(props.body && Object.keys(props.body).length > 0) {
        initParams.body = JSON.stringify(props.body)
    }

    console.debug('param', initParams)

    return new Promise((resolve, reject) => {
        fetch(props.url, initParams)
            .then(response => {
                if(response.ok) {
                    return response;
                }
                if (response.status === 401) {
                    throw new Error('로그인이 필요합니다');
                }
                if(response.status === 504) {
                    throw new Error('API 연결에 실패했습니다.<br>관리자에게 문의바랍니다.')
                }
                return response.text().then(message => {
                    throw new Error(message);
                });
            })
            .then(response=> {
                resolve(response)
            })
            .catch(reason => {
                reject(reason)
            });
    });
};

export {Ajax};