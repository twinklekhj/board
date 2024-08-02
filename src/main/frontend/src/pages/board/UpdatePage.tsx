import React from 'react';
import Header from "@Layout/header";
import {Button, FormControlLabel, Switch} from "@mui/material";
import MDEditor from '@uiw/react-md-editor';
import rehypeSanitize from "rehype-sanitize";
import {IoIosSend} from "react-icons/io";
import {FaArrowLeft} from "react-icons/fa6";
import {useBoardUpdate} from "@Hooks/board";

const UpdatePage = () => {
    const {
        title,
        content,
        isShow,
        onTitleChange,
        onContentChange,
        onVisibleChange,
        goBack,
        publish,
    } = useBoardUpdate();

    return (
        <>
            <Header title={"게시글 수정"} usePreviousButton={true}/>
            <div className={"content"}>
                <form className={"inner post-container"}>
                    <div className={"post-title"}>
                        <input type={"text"} maxLength={100} value={title} placeholder={"제목을 입력하세요"}
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
                        <Button startIcon={<FaArrowLeft/>} size="large" onClick={goBack}>목록으로</Button>
                        <div>
                            <FormControlLabel control={<Switch checked={isShow} onChange={onVisibleChange}/>}
                                              label="공개유무"/>
                        </div>
                        <Button startIcon={<IoIosSend/>} variant={"contained"} size="large"
                                onClick={publish}>수정하기</Button>
                    </div>
                </form>
            </div>
        </>
    );
};

export default UpdatePage;