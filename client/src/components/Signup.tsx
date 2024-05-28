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

    const [user, setUser] = useState({ username: '', password: '', role: 'USER'})
    const [message, setMessage] = useState("");
    const [emailValid, setEmailValid] = useState(true);

    const handleChange = (e: any) => {
        const { name, value } = e.target;
        setUser({ ...user, [e.target.name]: e.target.value });

        if(name === 'username') {
            if(value.match(/^([\w.%+-]+)@([\w-]+\.)+([\w]{2,})$/i)) {
                setEmailValid(true);
            } else {
                setEmailValid(false);
            }
        }
    }

    const register = () => {
        if(!emailValid) {
            setMessage("Please enter a valid email address");
            return;
        }
        if (user.password.length < 8) {
            setMessage("Password must be at least 8 characters long");
            return;
        }

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
                    className={`form-control username mt-5 ${emailValid ? '' : 'is-invalid'}`}
                    placeholder="Enter email"
                    type="email" name="username" id="username"/>

                <input
                    onChange={handleChange}
                    className="form-control password mt-3"
                    placeholder="Enter password"
                    type="password" name="password" id="pass"/>

                <button
                    onClick={register}
                    className="btn mt-5">Sign up
                </button>
                <span className="mt-5">Already have account? <Link to={'/login'}>Log in</Link></span>
                {
                    message !== "" &&
                    <span className="mt-4 text-danger">{message}</span>
                }
            </div>
        </div>
    );
}