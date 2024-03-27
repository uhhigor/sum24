import React, { useState } from "react";
import '../styles/login.css';
import { Link } from "react-router-dom";
import axios from "axios";

export const Login = () => {

    const [user, setUser] = useState({ username: '', password: ''})
    
    const handleChange = (e: any) => {
        console.log(e.target.name);
        console.log(e.target.value);
        setUser({ ...user, [e.target.name]: e.target.value });
    }

    const login = () => {
        axios.post('http://localhost:8080/login', user)
            .then(response => {
                console.log("Login complete");
            })
            .catch(error => {
                console.error(error);
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
            </div>
        </div>
    )
}