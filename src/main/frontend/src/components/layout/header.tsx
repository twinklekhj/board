import React from 'react';
import {IconButton} from "@mui/material";
import {FaArrowLeft} from "react-icons/fa6";
import MemberComponent from "@Layout/member";

interface HeaderProps {
    title: string;
    usePreviousButton?: boolean;
}
const Header = (props: HeaderProps) => {
    return (
        <header className={"header"}>
            <div className={"inner"}>
                {props.usePreviousButton ? (
                    <IconButton onClick={(e) => {window.history.back()}}><FaArrowLeft /></IconButton>
                ): null}
                <div className={"header-title"}>{props.title}</div>

                <MemberComponent />
            </div>
        </header>
    );
};

export default Header;