import {TokenState} from "@Store/slice/token";
import {CsrfTokenState} from "@Store/slice/csrf"; // Redux 스토어 타입

interface AjaxProps {
    url: string;
    method: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
    body?: Object | {}
    token?: TokenState | undefined
    csrf?: CsrfTokenState | undefined
    file?: File | undefined
}
const Ajax = async (props: AjaxProps): Promise<any> => {
    const initParams = {
        method: props.method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `${props.token?.bearerType} ${props.token?.accessToken}`
        }
    }

    if(props.csrf) {
        // @ts-ignore
        initParams.headers[props.csrf.headerName] = props.csrf.token;
    }

    if(props.body && Object.keys(props.body).length > 0) {
        // @ts-ignore
        initParams.body = JSON.stringify(props.body)
    }

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

const AjaxFile = async (props: AjaxProps): Promise<any> => {
    const initParams = {
        method: props.method,
        headers: {
            'Authorization': `${props.token?.bearerType} ${props.token?.accessToken}`
        }
    };

    if(props.csrf) {
        // @ts-ignore
        initParams.headers[props.csrf.headerName] = props.csrf.token;
    }

    const formData = new FormData();
    props.file && formData.append('file', props.file);
    // @ts-ignore
    initParams.body = formData

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

export {Ajax, AjaxFile};