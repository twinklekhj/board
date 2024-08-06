import React, {useState} from 'react';
import Header from "@Layout/header";
import {Avatar, Button, TextField} from "@mui/material";
import {FaCloud} from "react-icons/fa6";
import {Ajax, AjaxFile} from "@Utils/ajax";
import {useSelector} from "react-redux";
import {RootState} from "@Store/index";
import AlertUtil from "@Utils/alert";
import {IoIosSend} from "react-icons/io";

const reader = new FileReader();

const MyPage = () => {
    const token = useSelector((state: RootState) => state.token);
    const csrfToken = useSelector((state: RootState) => state.csrf);

    const id = useSelector((state: RootState) => state.member).userId;
    const username = useSelector((state: RootState) => state.member).name;
    const imageUrl = useSelector((state: RootState) => state.member).imageUrl;

    const [name, setName] = useState<string>(username);
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

    const onNameChangeClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        console.log(name)
        if(name === ''){
            AlertUtil.warning({
                text: '변경하실 이름을 입력해주세요!'
            });
            return;
        } else if (name === username) {
            AlertUtil.warning({
                text: '변경된 내용이 없습니다.<br>변경하실 이름을 입력해주세요!'
            });
            return;
        }

        Ajax({
            url: '/api/user/info',
            method: 'PATCH',
            body: {
                name: name
            },
            token: token,
            csrf: csrfToken
        }).then(res => {
            if(res.ok) {
                AlertUtil.success({
                    text: '성공적으로 수정되었습니다!',
                    onHide: result => {
                        window.location.reload();
                    }
                })
            }
        }).catch(reason => {
            AlertUtil.error({
                text: reason
            })
        })
    }

    const onImageChangeClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        if (!file) {
            AlertUtil.warning({
                text: '변경하실 사진을 업로드해주세요!'
            });
            return;
        }
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
                        text: '프로필 이미지가 변경되었습니다!',
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
            <div className={"content member-page"}>
                <div className={"inner"}>
                    <form className={"member-profile"}>
                        <Avatar sx={{width: "200px", height: "200px"}} src={src}/>
                        <div className={"member-action"}>
                            <div>
                                <TextField
                                    id="outlined-controlled"
                                    label="사용자 아이디"
                                    defaultValue={id}
                                    disabled={true}
                                    variant="standard"
                                />
                            </div>
                            <div>
                                <TextField
                                    id="outlined-controlled"
                                    label="사용자 이름"
                                    value={name}
                                    onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                                        setName(event.target.value);
                                    }}
                                    variant="standard"
                                />

                                <Button
                                    type={"submit"}
                                    variant={"contained"}
                                    onClick={onNameChangeClick}
                                    className={"btn-submit"}
                                    startIcon={<IoIosSend/>}
                                >변경</Button>
                            </div>
                            <div>
                                <Button
                                    component="label"
                                    startIcon={<FaCloud/>}
                                    className={"btn-file-select"}
                                >
                                    프로필 이미지 선택
                                    <input type="file" name={"file"} onChange={onFileChange}/>
                                </Button>
                                <Button
                                    type={"submit"}
                                    variant={"contained"}
                                    onClick={onImageChangeClick}
                                    className={"btn-submit"}
                                    startIcon={<IoIosSend/>}
                                >변경</Button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
};

export default MyPage;