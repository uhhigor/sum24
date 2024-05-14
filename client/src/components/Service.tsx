import React, {useEffect, useState} from "react";
import {FcApproval, FcHighPriority} from "react-icons/fc";
import {Col, Row} from 'react-bootstrap';
import '../styles/service.css';
import {Link} from "react-router-dom";
import {getAuthToken, getUserId, getUsername} from "../validateUser";
import axios, {AxiosError} from "axios";


interface Service {
    id: number;
    name: string;
    address: string;
    port: string;
}

interface ServiceProps {
    index: number;
}

export const Service: React.FC<ServiceProps> = ({index}) => {

    const [service, setService] = useState<Service | null>(null);
    const [status, setStatus] = useState(null);

    useEffect(() => {
        fetchServiceData();
        const intervalId = setInterval(fetchServiceStatus, 30 * 1000);
        return () => clearInterval(intervalId);
    }, []);

    const fetchServiceData = async () => {
        try {
            const response = await fetch(`http://localhost:8080/services/${index}`, {
                headers: {
                    'Authorization': "Bearer " + getAuthToken() as string
                }
            });
            if (!response.ok) {
                console.error('Failed to fetch service:', response);
            }
            const data = await response.json();
            setService(data);
        } catch (error) {
            console.error('Error fetching service:', error);
        }
    };


    const fetchServiceStatus = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/services/${index}/status/last`, {
                headers: {
                    'Authorization': "Bearer " + getAuthToken() as string
                }
            });
            if (response.status != 200) {
                console.error('Failed to fetch service status:', response);
                return;
            }
            setStatus(response.data);

        } catch (error: any) {
            if (axios.isCancel(error)) {
                console.error('Request canceled:', error.message);
            } else {
                console.error('Error fetching service status:', error.message);
            }
        }
    };


    function imageRender() {
        if (status === 1) {
            return <FcApproval size={20}/>
        } else if (status === 0) {
            return <FcHighPriority size={20}/>
        } else {
            return <div>Unknown</div>
        }
    }

    const deleteService = () => {
        axios.delete(`http://localhost:8080/services/${index}`, {
            headers: {
                'Authorization': "Bearer " + getAuthToken() as string
            }

        })
            .then(response => {
                if (response.status === 200) {
                    console.log("deleted item")
                    window.location.reload();
                }
            })
            .catch(error => {
                console.error(error);
            })
    }

    const propsToEdit = {
        name: service?.name,
        address: service?.address,
        port: service?.port
    }

    return (
        <div className="m-2 service-container">
            <h5>{service?.name}</h5>
            <p>{service?.address}</p>
            <p>{service?.port}</p>
            <Row className="align-items-center justify-content-center">
                <Col>
                    <div>Status:</div>
                </Col>
                <Col>
                    {imageRender()}
                </Col>
            </Row>
            <div className="mt-4">
                <Link to={'https://' + service?.address as string} target="_blank" rel="noopener noreferrer">
                    <button className="btn">View</button>
                </Link>
                <Link to={`/edit-service/${index}`} state={propsToEdit}>
                    <button className="btn mx-3">Edit</button>
                </Link>
                <button className="btn" onClick={() => deleteService()}>Delete</button>
            </div>


        </div>
    );
};