import React, {useEffect} from 'react';

const MainApp = () => {
    const [time, setTime] = React.useState("");
    useEffect(() => {
        fetch("/api/time", {
            method: "GET",
        })
            .then(res => {
                if (!res.ok) {
                    throw new Error('Network response was not ok');
                }
                return res.text();
            })
            .then(res => {
                setTime(res)
            })
            .catch(err => console.error(err));
    }, [])
    return (
        <div>
            Hi, Server time is {time}
        </div>
    );
};

export default MainApp;