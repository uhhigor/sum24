import React, {useEffect, useState} from "react";
import "../styles/login.css";
import "../styles/servicesForms.css";
import axios from "axios";
import {Link, useParams, useLocation} from "react-router-dom";
import {getAuthToken} from "../validateUser";

interface EditServiceProps {
    name: string;
    address: string;
}

function EditService() {
    const {index} = useParams();
    const location = useLocation();
    const { name, address } = location.state as EditServiceProps;
    const [service, setService] = useState<EditServiceProps>({name: "", address: ""});

    useEffect(() => {
        setService({ name, address });
    }, [name, address]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setService(prevService => ({
            ...prevService,
            [name]: value
        }));
    };

    const editService = () => {
        axios.put(`http://localhost:8080/service/${index}`, service, {
            headers: {
                "Content-Type": "application/json",
                'Authorization': "Bearer " + getAuthToken()
            }
        })
            .then(response => {
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
            <div className="col-4 login-form editService-form">
                <h1 className="mt-5 ms-5 align-self-start">EDIT SERVICE</h1>
                <input
                    onChange={handleChange}
                    value={service.name}
                    className="form-control username mt-5"
                    placeholder="New name"
                    type="text"
                    name="name"
                    id="name"
                />
                <input
                    onChange={handleChange}
                    value={service.address}
                    className="form-control password mt-4"
                    placeholder="New address"
                    type="text"
                    name="address"
                    id="addr"
                />
                <div className="row mt-5">
                    <div className="col">
                        <button
                            onClick={editService}
                            className="btn px-5 me-4">Edit
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
    );
}

export default EditService;
