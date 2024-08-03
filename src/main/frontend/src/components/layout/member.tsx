import React from 'react';
import {Avatar, Box, Button, Fade} from "@mui/material";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "@Store/index";
import Popper from "@mui/material/Popper/BasePopper";
import {Ajax} from "@Utils/ajax";
import {initToken} from "@Store/slice/token";
import AlertUtil from "@Utils/alert";
import {useNavigate} from "react-router-dom";
import {FaCircleUser} from "react-icons/fa6";
import {LuLogOut} from "react-icons/lu";

const MemberComponent = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const name = useSelector((state: RootState) => state.member).name;
    const imageUrl = useSelector((state: RootState) => state.member).imageUrl;
    const accessToken = useSelector((state: RootState) => state.token).accessToken;
    const bearerType = useSelector((state: RootState) => state.token).bearerType;

    const [tooltipOpen, setTooltipOpen] = React.useState(false);
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);

    const canBeOpen = tooltipOpen && Boolean(anchorEl);
    const id = canBeOpen ? 'transition-popper' : undefined;

    const handleClick = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
        setTooltipOpen((previousOpen) => !previousOpen);
    };

    const goMyPage = () => {
        navigate('/me');
    };
    const logout = () => {
        AlertUtil.confirm({
            title: '로그아웃',
            text: '로그아웃 하시겠습니까?',
            onHide: result => {
                if(result.isConfirmed) {
                    Ajax({
                        url: '/api/logout',
                        method: 'GET',
                        token: {
                            accessToken: accessToken,
                            bearerType: bearerType
                        }
                    }).then(res => {
                        if(res.ok) {
                            dispatch(initToken());
                        }
                    })
                }
            }
        })
    }


    return (
        <>
            <div className="member-container" aria-describedby={id} onClick={handleClick}>
                <Avatar sx={{width: 30, height: 30}} src={imageUrl}/>
                <div>
                    <b>{name}</b>님
                </div>
            </div>
            <Popper id={id} open={tooltipOpen} anchorEl={anchorEl} transition>
                {({TransitionProps}) => (
                    <Fade {...TransitionProps} timeout={350}>
                        <div className="member-tooltip">
                            <div><Button onClick={goMyPage} startIcon={<FaCircleUser />}>마이페이지</Button></div>
                            <div><Button onClick={logout} startIcon={<LuLogOut />}>로그아웃</Button></div>
                        </div>
                    </Fade>
                )}
            </Popper>
        </>
    );
};

export default MemberComponent;