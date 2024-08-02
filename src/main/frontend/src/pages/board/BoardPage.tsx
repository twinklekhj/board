import BoardList from "@Components/board/BoardList";
import {FaPencil} from "react-icons/fa6";
import Header from "@Layout/header";
import {Button} from "@mui/material";
import {useNavigate} from "react-router-dom";

const BoardPage = () => {
    const navigate = useNavigate();
    const onWriteButtonClick = () => {
        navigate("/board/write");
    }

    return (
        <>
            <Header title={"게시판"} />
            <div className={"content"}>
                <div className={"inner"}>
                    <div className={"content-action"}>
                        <Button variant="contained" startIcon={<FaPencil/>} onClick={onWriteButtonClick}>글쓰기</Button>
                    </div>
                    <div className={"content-list"}>
                        <BoardList />
                    </div>
                </div>
            </div>
        </>
    );
}
export default BoardPage;