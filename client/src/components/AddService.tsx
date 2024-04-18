import React, {useState} from "react";
import "../styles/login.css";
import "../styles/servicesForms.css";
import axios from "axios";
import {Link} from "react-router-dom";
import {getAuthToken, getUserId, getUsername} from "../validateUser";

export const AddService = () => {

    const [service, setService] = useState({name: '', address: '', port: ''})

    const handleChange = (e: any) => {
        console.log(e.target.name);
        console.log(e.target.value);
        setService({...service, [e.target.name]: e.target.value});
    }

    const addService = () => {
        axios.post(`http://localhost:8080/services/user/${getUserId()}`, service, {
            headers: {
                "Content-Type": "application/json",
                'Authorization': "Bearer " + getAuthToken() as string
            }
        })
            .then(response => {
                console.log("Login complete");
                if (response.status === 200) {
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
            <div className="col-4 login-form addService-form">
                <h1 className="mt-5 ms-5 align-self-start">ADD SERVICE</h1>
                <input
                    onChange={handleChange}
                    className="form-control username mt-5"
                    placeholder="Enter name"
                    type="text" name="name" id="name"/>

                <input
                    onChange={handleChange}
                    className="form-control password mt-4"
                    placeholder="Enter address"
                    type="text" name="address" id="addr"/>

                <input
                    onChange={handleChange}
                    className="form-control password mt-4"
                    placeholder="Enter port"
                    type="text" name="port" id="por"/>
                <div className="row mt-5">
                    <div className="col">
                        <button
                            onClick={addService}
                            className="btn px-5 me-4">Add
                        </button>
                    </div>
                    <div className="col">
                        <Link to={"/dashboard"}>
                            <button
                                className="btn px-5">Cancel
                            </button>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    )
}