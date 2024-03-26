import React, { useState } from "react";
import '../styles/login.css';
import { Link } from "react-router-dom";

export const Login = () => {

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    
    return (
        <div className="login-container">
            <div className="col-4 login-form">
                <h1 className="mt-5 ms-4 align-self-start">LOGIN</h1>
                <input 
                onChange={(e) => setEmail(e.target.value)}
                className="form-control email mt-5" 
                placeholder="Enter email" 
                type="email" name="email" id="email" />
                
                <input
                onChange={(e) => setPassword(e.target.value)} 
                className="form-control password mt-3" 
                placeholder="Enter password" 
                type="password" name="password" id="pass" />

                <button
                className="btn mt-5">Log in</button>

                <span className="mt-5">Do not have account? <Link to={'/signup'}>Sign up</Link></span>
            </div>
        </div>
    )
}