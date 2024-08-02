import React from 'react';
import {useParams} from "react-router-dom";
import {Avatar, Button} from "@mui/material";
import {FaEye, FaPencil, FaTrash} from "react-icons/fa6";
import Header from "@Layout/header";
import MDEditor from "@uiw/react-md-editor";
import {TimeUtil} from "@Utils/time";
import {useBoardDetail} from "@Hooks/board";

const DetailPage = () => {
    const {boardId} = useParams();
    const {board, memberId, onUpdateClick, onDeleteClick} = useBoardDetail(boardId);

    return (
        <>
            <Header title={"게시글 상세보기"} usePreviousButton={true}/>
            <div className={"content"}>
                <div className={"inner post-container"}>
                    <h1 className={"post-header"}>{board?.title}</h1>
                    <div className={"post-meta"}>
                        <div className={"post-flex"}>
                            <Avatar sx={{width: "30px", height: "30px"}}/>
                            {board?.writer + ' ㆍ ' + TimeUtil.getLastAgo(board?.createDate)}
                        </div>
                        <div className={"post-flex"}>
                            {memberId === board?.writerId ? (
                                <>
                                    <Button
                                        startIcon={<FaPencil/>}
                                        onClick={onUpdateClick}>수정</Button>
                                    <Button
                                        color={"error"}
                                        startIcon={<FaTrash/>}
                                        onClick={onDeleteClick}>삭제</Button>
                                </>
                            ) : ""}
                            <div><FaEye/> 조회수 {board?.hits}</div>
                        </div>
                    </div>
                    <div className={"post-divider"}></div>
                    <div className={"post-content"}>
                        <MDEditor.Markdown source={board?.content}/>
                    </div>
                    <div className={"post-divider"}></div>
                    <div>Comments</div>
                </div>
            </div>
        </>
    );
};

export default DetailPage;
