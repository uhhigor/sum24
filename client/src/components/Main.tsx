import React, {useState} from "react";
import "../styles/main.css";
import {useNavigate} from "react-router-dom";

export const Main : React.FC = () => {
    const [isButtonHovered, setIsButtonHovered] = useState(false);
    let navigate = useNavigate();

    const handleButtonHover = () => {
        setIsButtonHovered(true);
    };

    const handleButtonLeave = () => {
        setIsButtonHovered(false);
    };

    const goToLoginPage = () => {
        const path = `/login`;

        navigate(path);
    };
    const goToRegisterPage = () => {
        const path = `/signup`;
        navigate(path)
    };

    return (
        <div className="main-container">
            <div className="d-flex justify-content-center align-items-center min-vh-100">
                <div className="container-fluid">
                    <div className="row">
                        <div className="col text-center">
                            <div className={`welcomeText ${isButtonHovered ? 'paused' : ''}`}>Welcome!
                            </div>
                            <button
                                className="btn btn-lg m-5"
                                onMouseEnter={handleButtonHover}
                                onMouseLeave={handleButtonLeave}
                                onClick={goToRegisterPage}
                            >
                                Register here!
                            </button>
                            <button
                                className="btn btn-lg m-5"
                                onMouseEnter={handleButtonHover}
                                onMouseLeave={handleButtonLeave}
                                onClick={goToLoginPage}
                            >
                                Log in here!
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    )
}