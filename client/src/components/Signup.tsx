import React, { useEffect, useState } from "react";
import "../styles/login.css";
import { Link } from "react-router-dom";
import axios from "axios";
import {jwtDecode} from "jwt-decode";

interface DecodedToken {
    sub: string;
    iat: number;
    exp: number;
}

function isDecodedToken(token: any): token is DecodedToken {
    return typeof token === 'object' && token !== null && 'sub' in token;
}


export const Signup = () => {

    const [user, setUser] = useState({ username: '', password: ''})
    const [message, setMessage] = useState("");

    const handleChange = (e: any) => {
        setUser({ ...user, [e.target.name]: e.target.value });
    }

    const register = () => {
        axios.post('http://localhost:8080/register', user)
            .then(response => {
                console.log("Register complete");
                if(response.status === 200) {
                    localStorage.setItem('authToken', response.data.token);
                    const decodedToken : DecodedToken = jwtDecode(response.data.token);
                    if (isDecodedToken(decodedToken)) {
                        const extractedUsername = decodedToken.sub;

                        localStorage.setItem('username', extractedUsername);

                    }
                    window.location.href = '/dashboard';
                }
            })
            .catch(error => {
                setMessage(error.response.data.message);
                console.error(error);
            })
    }
    
    return (
        <div className="login-container">
            <div className="col-4 login-form">
                <h1 className="mt-5 ms-4 align-self-start">SIGN UP</h1>
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
                onClick={register}
                className="btn mt-5">Sign up</button>
                <span className="mt-5">Already have account? <Link to={'/login'}>Log in</Link></span>
                {
                    message !== "" &&
                    <span className="mt-4 text-danger">{message}</span>
                }
            </div>
        </div>
    );
}