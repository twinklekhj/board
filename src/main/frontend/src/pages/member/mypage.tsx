import React, {useState} from 'react';
import Header from "@Layout/header";
import {Avatar, Button} from "@mui/material";
import {FaCloud} from "react-icons/fa6";
import {AjaxFile} from "@Utils/ajax";
import {useSelector} from "react-redux";
import {RootState} from "@Store/index";
import AlertUtil from "@Utils/alert";

const reader = new FileReader();

const MyPage = () => {
    const token = useSelector((state: RootState) => state.token);
    const csrfToken = useSelector((state: RootState) => state.csrf);
    const imageUrl = useSelector((state: RootState) => state.member).imageUrl;
    const [file, setFile] = useState<File>();
    const [src, setSrc] = useState<string>(imageUrl);
    const onFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            const file = e.target.files[0];
            setFile(file);
            reader.onload = function (e) {
                const result = e.target?.result;
                if (typeof result === 'string') {
                    setSrc(result);
                }
            }
            reader.readAsDataURL(file);
        }
    }

    const onSubmitClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        AjaxFile({
            url: '/api/user/image',
            method: 'PATCH',
            file: file,
            token: token,
            csrf: csrfToken,
        })
            .then(res => {
                if (res.ok) {
                    AlertUtil.success({
                        text: '업로드에 성공하였습니다!',
                        onHide: result => {
                            window.location.reload();
                        }
                    })
                }
            })
            .catch(err => {
                AlertUtil.warning({
                    text: err
                })
            });
    }
    return (
        <>
            <Header title={"마이페이지"} usePreviousButton={true}/>
            <div className={"content"}>
                <div className={"inner"}>
                    <form>
                        <Avatar sx={{width: "200px", height: "200px"}} src={src}/>
                        <Button
                            component="label"
                            role={undefined}
                            variant="contained"
                            tabIndex={-1}
                            startIcon={<FaCloud/>}
                        >
                            Upload file
                            <input type="file" name={"file"} onChange={onFileChange}/>
                        </Button>
                        <Button type={"submit"} onClick={onSubmitClick}>변경하기</Button>
                    </form>
                </div>
            </div>
        </>
    );
};

export default MyPage;