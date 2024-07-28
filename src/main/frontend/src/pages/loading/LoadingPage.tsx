import React from 'react';
import {FadeLoader} from "react-spinners";

const LoadingPage = () => {
    return (
        <div className={"loading-container"}>
            <div className={"loading-spinner"}>
                <FadeLoader color="#317fe0" radius={10}/>
            </div>
            <div>
                Loading...
            </div>
        </div>
    );
};

export default LoadingPage;