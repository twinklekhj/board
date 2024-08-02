import Swal, {SweetAlertResult} from 'sweetalert2';

interface AlertProps {
    title?: string
    text: string
    icon?: 'success' | 'error' | 'warning' | 'info' | 'question'
    showCancelButton?: boolean
    confirmButtonText?: string
    cancelButtonText?: string
    onHide?: (result: SweetAlertResult) => void
}

const AlertUtil = {
    alert: (props: AlertProps) => {
        Swal.fire({
            title: props.title || '알림',
            html: props.text || 'Enter your message!',
            icon: props.icon || 'success',
            confirmButtonText: props.confirmButtonText || '확인',
            cancelButtonText: props.cancelButtonText || '취소',
            showCancelButton: props.showCancelButton,
        })
        .then(result => {
            props.onHide && props.onHide(result);
        })
    },
    info: (props: AlertProps) => {
        AlertUtil.alert(Object.assign({}, props, {
            icon: props.icon || 'info',
        }))
    },
    warning: (props: AlertProps) => {
        AlertUtil.alert(Object.assign({}, props, {
            title: '주의',
            icon: props.icon || 'warning',
        }))
    },
    success: (props: AlertProps) => {
        AlertUtil.alert(Object.assign({}, props, {
            icon: props.icon || 'success',
        }))
    },
    error: (props: AlertProps) => {
        AlertUtil.alert(Object.assign({}, props, {
            title: '에러!',
            icon: props.icon || 'error',
        }))
    },
    confirm: (props: AlertProps) => {
        AlertUtil.alert(Object.assign({}, props, {
            icon: props.icon || 'question',
            showCancelButton: true,
        }))
    }
}
export default AlertUtil