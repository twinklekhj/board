import Swal, {SweetAlertResult} from 'sweetalert2';

interface AlertProps {
    title?: string
    text: string
    icon?: 'success' | 'error' | 'warning' | 'info' | 'question'
    showCancelButton?: boolean
    confirmButtonText?: string
    cancelButtonText?: string
    onHide?: (result: SweetAlertResult<any>) => {}
}

const AlertUtil = {
    alert: (props: AlertProps) => {
        Swal.fire({
            title: props.title || 'Alert',
            html: props.text || 'Enter your message!',
            icon: props.icon || 'success',
            confirmButtonText: props.confirmButtonText || 'Confirm',
            cancelButtonText: props.cancelButtonText || 'Cancel',
            showCancelButton: props.showCancelButton,
        })
        .then(result => {
            props.onHide && props.onHide(result);
        })
    },
    warning: (props: AlertProps) => {
        AlertUtil.alert(Object.assign({}, props, {
            title: 'Warning',
            icon: props.icon || 'warning',
        }))
    }
}
export default AlertUtil