import React, {useEffect, useState} from "react";
import "../styles/login.css";
import "../styles/servicesForms.css";
import axios from "axios";
import {Link, useParams} from "react-router-dom";
import {getAuthToken} from "../validateUser";

interface EditServiceProps {
    name: string;
    address: string;
    port: string;
}

function EditService(props: EditServiceProps) {

    const {index} = useParams();
    const [service, setService] = useState<EditServiceProps>({name: "", address: "", port: ""});
    const handleChange = (e: any) => {
        console.log(e.target.name);
        console.log(e.target.value);
        setService({...service, [e.target.name]: e.target.value});
    }

    useEffect(() => {
        console.log(props.name);
        console.log(props.address);
        console.log(props.port);

        setService({...props});
    }, [props]);

    const editService = () => {
        axios.put(`http://localhost:8080/service/${index}`, service, {
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
            <div className="col-4 login-form editService-form">
                <h1 className="mt-5 ms-5 align-self-start">EDIT SERVICE</h1>
                <input
                    onChange={handleChange}
                    className="form-control username mt-5"
                    placeholder="New name"
                    type="text" name="name" id="name"/>

                <input
                    onChange={handleChange}
                    className="form-control password mt-4"
                    placeholder="New address"
                    type="text" name="address" id="addr"/>

                <input
                    onChange={handleChange}
                    className="form-control password mt-4"
                    placeholder="New port"
                    type="text" name="port" id="por"/>
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