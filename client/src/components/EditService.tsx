import React, {useEffect, useState} from "react";
import  "../styles/login.css";
import {getAuthToken} from "../validateUser";
import axios from "axios";
import {useParams} from "react-router-dom";

interface EditServiceProps {
    name: string;
    address: string;
    port: string;
}

 function EditService(props: EditServiceProps) {

     const { index } = useParams();
     const [service, setService] = useState<EditServiceProps>({ name: "", address: "", port: "" });
    const handleChange = (e: any) => {
        console.log(e.target.name);
        console.log(e.target.value);
        setService({ ...service, [e.target.name]: e.target.value });
    }

     useEffect(() => {
         console.log(props.name);
         console.log(props.address);
         console.log(props.port);

             setService({ ...props });
     }, [props]);

    const editService = () => {
        axios.put(`http://localhost:8080/services/${index}`, service, {
            headers: {
                "Content-Type": "application/json",
                'Authorization': "Bearer " + getAuthToken() as string
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
                <h1 className="mt-5 ms-4 align-self-start">EDIT SERVICE</h1>
                <input
                    onChange={handleChange}
                    className="form-control username mt-5"
                    placeholder="New name"
                    type="text" name="name" id="name" />

                <input
                    onChange={handleChange}
                    className="form-control password mt-3"
                    placeholder="New address"
                    type="text" name="address" id="addr" />

                <input
                    onChange={handleChange}
                    className="form-control password mt-3"
                    placeholder="New port"
                    type="text" name="port" id="por" />
                <button
                    onClick={editService}
                    className="btn mt-5">Edit service</button>
            </div>
        </div>
    );
}

export default EditService;