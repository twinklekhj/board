import React from 'react';
import Header from "@Layout/header";
import {Button, FormControlLabel, Switch} from "@mui/material";
import AlertUtil from "@Utils/alert";
import MDEditor from '@uiw/react-md-editor';
import rehypeSanitize from "rehype-sanitize";
import {IoIosSend} from "react-icons/io";
import {FaArrowLeft} from "react-icons/fa6";
import {useNavigate} from "react-router-dom";
import {useAddBoard} from "@Hooks/board";

const WritePage = () => {
    const navigate = useNavigate();
    const {
        title,
        content,
        isShow,
        onTitleChange,
        onContentChange,
        onShowChange,
        onPublishClick
    } = useAddBoard();

    const goBack = () => {
        AlertUtil.confirm({
            title: '나가기 알림',
            text: '정말로 나가시겠습니까?<br>현재 페이지의 내용이 초기화됩니다.',
            onHide: (result) => {
                if (result.isConfirmed) {
                    navigate('/');
                }
            }
        });
    };

    return (
        <>
            <Header title={"게시글 작성"}/>
            <div className={"content"}>
                <form className={"inner post-container"}>
                    <div className={"post-title"}>
                        <input type={"text"} maxLength={100} placeholder={"제목을 입력하세요"} value={title}
                               onChange={onTitleChange}/>
                    </div>
                    <div className={"post-content"}>
                        <MDEditor
                            value={content}
                            height={"100%"}
                            previewOptions={{
                                rehypePlugins: [[rehypeSanitize]],
                            }}
                            onChange={onContentChange}
                        />
                    </div>
                    <div className={"post-footer"}>
                        <Button
                            startIcon={<FaArrowLeft/>}
                            size="large"
                            onClick={goBack}>목록으로</Button>
                        <div>
                            <FormControlLabel control={<Switch checked={isShow} onChange={onShowChange}/>}
                                              label="공개유무"/>
                        </div>
                        <Button
                            startIcon={<IoIosSend/>}
                            variant={"contained"}
                            size="large"
                            onClick={onPublishClick}>출간하기</Button>
                    </div>
                </form>
            </div>
        </>
    );
};

export default WritePage;
