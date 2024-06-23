import React, { useState, ChangeEvent } from "react";
import "../styles/login.css";
import "../styles/servicesForms.css";
import axios from "axios";
import { Link } from "react-router-dom";
import { getAuthToken, getUserId } from "../validateUser";

interface Service {
    name: string;
    address: string;
    fields: string[];
}

export const AddService = () => {

    const [service, setService] = useState<Service>({ name: '', address: '', fields: [] });
    const [additionalFields, setAdditionalFields] = useState<string>('');

    const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;

        if (name === 'additionalFields') {
            const filteredValue = value.replace(/[^a-zA-Z0-9,]/g, '');
            setAdditionalFields(filteredValue);

            const fieldsArray = filteredValue.split(',').filter((field: string) => field.trim() !== '');
            setService({ ...service, fields: fieldsArray });
        } else {
            setService({ ...service, [name]: value });
        }
        console.log("service" + service);
    }

    const addService = () => {
        const serviceData: Partial<Service> = { ...service };
        if (service.fields.length === 0) {
            delete serviceData.fields;
        }

        axios.post(`http://localhost:8080/service/user/${getUserId()}`, serviceData, {
            headers: {
                "Content-Type": "application/json",
                'Authorization': "Bearer " + getAuthToken()
            }
        })
            .then(response => {
                console.log("Service added");
                console.log(serviceData);
                if (response.status === 200) {
                    window.location.href = '/dashboard';
                }
            })
            .catch(error => {
                console.error(error);
            });
    }

    return (
        <div className="login-container">
            <div className="col-4 login-form addService-form">
                <h1 className="mt-5 ms-5 align-self-start">ADD SERVICE</h1>
                <input
                    onChange={handleChange}
                    className="form-control username mt-5"
                    placeholder="Enter name"
                    type="text" name="name" id="name" />

                <input
                    onChange={handleChange}
                    className="form-control password mt-4"
                    placeholder="Enter address"
                    type="text" name="address" id="addr" />
                <textarea
                    id="additionalFields"
                    className="form-control mt-4 additionalFields"
                    placeholder="Additional fields"
                    name="additionalFields"
                    value={additionalFields}
                    onChange={handleChange}
                />
                <div className="row mt-5">
                    <div className="col">
                        <button
                            onClick={addService}
                            className="btn px-5 me-4">Add
                        </button>
                    </div>
                    <div className="col">
                        <Link to="/dashboard">
                            <button
                                className="btn px-5">Cancel
                            </button>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}
