import React, {useState} from "react";
import  "../styles/login.css";
import axios from "axios";
import {getAuthToken} from "../validateUser";
export const AddService = () => {

    const [service, setService] = useState({ name: '', address: ''})

    const handleChange = (e: any) => {
        console.log(e.target.name);
        console.log(e.target.value);
        setService({ ...service, [e.target.name]: e.target.value });
    }

    const addService = () => {
        axios.post('http://localhost:8080/services/', service, {
            headers: {
                Authorization: `Bearer ${getAuthToken()}`,
                "Content-Type": "application/json"
            }
        })
            .then(response => {
                console.log("Login complete");
                if(response.status === 200) {
                    window.location.href = '/dashboard';
                }
                console.log(getAuthToken());
            })
            .catch(error => {
                console.error(error);
            })
    }


    return (
        <div className="login-container">
            <div className="col-4 login-form">
                <h1 className="mt-5 ms-4 align-self-start">ADD SERVICE</h1>
                <input
                    onChange={handleChange}
                    className="form-control username mt-5"
                    placeholder="Enter name"
                    type="text" name="name" id="name" />

                <input
                    onChange={handleChange}
                    className="form-control password mt-3"
                    placeholder="Enter address"
                    type="text" name="address" id="addr" />

                <button
                    onClick={addService}
                    className="btn mt-5">Add service</button>
            </div>
        </div>
    )
}