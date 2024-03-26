import React, { useEffect, useState } from "react";
import "../styles/login.css";
import { Link } from "react-router-dom";

export const Signup = () => {

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [username, setUsername] = useState('')
    
    return (
        <div className="login-container">
            <div className="col-4 login-form">
                <h1 className="mt-5 ms-4 align-self-start">SIGN UP</h1>
                <input 
                onChange={(e) => setUsername(e.target.value)}
                className="form-control email mt-5" 
                placeholder="Enter username" 
                type="text" name="username" id="username" />
                
                <input 
                onChange={(e) => setEmail(e.target.value)}
                className="form-control email mt-3" 
                placeholder="Enter email" 
                type="email" name="email" id="email" />
                
                <input
                onChange={(e) => setPassword(e.target.value)} 
                className="form-control password mt-3" 
                placeholder="Enter password" 
                type="password" name="password" id="pass" />

                <button
                className="btn btn-success mt-5">Sign up</button>

                <span className="mt-5">Already have account? <Link to={'/login'}>Log in</Link></span>
            </div>
        </div>
    );
}