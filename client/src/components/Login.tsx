import React, { useState } from "react";
import '../styles/login.css';
import { Link } from "react-router-dom";
import axios from "axios";
import {getAuthToken} from "../validateUser";
import { jwtDecode } from "jwt-decode";
interface DecodedToken {
    sub: string;
    iat: number;
    exp: number;
}

function isDecodedToken(token: any): token is DecodedToken {
    return typeof token === 'object' && token !== null && 'sub' in token;
}

export const Login: React.FC = () =>  {

    const [user, setUser] = useState({ username: '', password: ''})
    const [message, setMessage] = useState("");
    const handleChange = (e: any) => {
        setUser({ ...user, [e.target.name]: e.target.value });
    }

    const login = () => {
        axios.post('http://localhost:8080/login', user)
            .then(response => {
                console.log("Login complete");
                if(response.status === 200) {
                    localStorage.setItem('authToken', response.data.token);

                    const decodedToken : DecodedToken = jwtDecode(response.data.token);
                    if (isDecodedToken(decodedToken)) {
                        const extractedUsername = decodedToken.sub;

                        localStorage.setItem('username', extractedUsername);

                    }
                    window.location.href = '/dashboard';
                }
                console.log(getAuthToken());
            })
            .catch((error:any) => {
                setMessage(error.response.data.message);
                console.error(error.response.data.message);
            })
    }
    
    return (
        <div className="login-container">
            <div className="col-4 login-form">
                <h1 className="mt-5 ms-4 align-self-start">LOGIN</h1>
                <input 
                onChange={handleChange}
                className="form-control username mt-5" 
                placeholder="Enter username" 
                type="text" name="username" id="username" />
                
                <input
                onChange={handleChange}
                className="form-control password mt-3" 
                placeholder="Enter password" 
                type="password" name="password" id="pass" />

                <button
                onClick={login}
                className="btn mt-5">Log in</button>

                <span className="mt-5">Do not have account? <Link to={'/signup'}>Sign up</Link></span>
                {
                    message !== "" &&
                    <span className="mt-4 text-danger">{message}</span>
                }
            </div>
        </div>
    )
}