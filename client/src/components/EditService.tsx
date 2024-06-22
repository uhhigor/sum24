import React, { useEffect, useState, ChangeEvent, useRef } from "react";
import "../styles/login.css";
import "../styles/servicesForms.css";
import axios from "axios";
import { Link, useParams, useLocation } from "react-router-dom";
import { getAuthToken } from "../validateUser";

interface EditServiceProps {
    name: string;
    address: string;
    fields?: string[];
}

function EditService() {
    const { index } = useParams();
    const location = useLocation();
    const { name, address, fields = [] } = location.state as EditServiceProps;
    const [service, setService] = useState<EditServiceProps>({ name: "", address: "", fields: [] });
    const [additionalFields, setAdditionalFields] = useState<string>("");

    const previousFieldsRef = useRef<string[]>(fields);

    useEffect(() => {
        if (JSON.stringify(fields) !== JSON.stringify(previousFieldsRef.current)) {
            setService({ name, address, fields });
            setAdditionalFields(fields.join(','));
            previousFieldsRef.current = fields;
        }
    }, [name, address, fields]);

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

    const editService = () => {
        const serviceData: Partial<EditServiceProps> = { ...service };
        if (service.fields && service.fields.length === 0) {
            delete serviceData.fields;
        }

        axios.put(`http://localhost:8080/service/${index}`, serviceData, {
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
