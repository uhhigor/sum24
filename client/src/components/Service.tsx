import React, {useEffect, useState} from "react";
import {FcApproval, FcHighPriority} from "react-icons/fc";
import {Col, Row} from "react-bootstrap";
import '../styles/service.css';
import {Link} from "react-router-dom";
import {getAuthToken} from "../validateUser";


interface Service {
    id: number;
    name: string;
    address: string;
}

interface ServiceProps {
    index: number;
}

export const Service: React.FC<ServiceProps> = ({index}) => {

    const [service, setService] = useState<Service | null>(null);
    const [status, setStatus] = useState<boolean | null >(false);

    useEffect(() => {
        fetchServiceData();
    }, []);

    useEffect(() => {
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
            const response = await fetch(service?.address as string);
            setStatus(response.status === 200);
            console.log(response.status);
        } catch (error) {
            console.error('Error fetching service status:', error);
        }
    };


    function imageRender() {
        console.log(status);
        if (status === true) {
            return <FcApproval size={20}/>
        } else if (status === false) {
            return <FcHighPriority size={20}/>
        } else {
            return <div>Unknown</div>
        }
    }

    async function deleteService() {
        try {
            const response = await fetch(`http://localhost:8080/services/${index}`, {
                method: 'DELETE',
                headers: {
                    Authorization: 'Bearer ' + getAuthToken() as string
                }
            });
            if (!response.ok) {
                console.error('Failed to delete service:', response);
            }
        } catch (error) {
            console.error('Error deleting service:', error);
            throw error;
        }
    }


    return (
        <div className="m-2 service-container">
            <h5>{service?.name}</h5>
            <p>{service?.address}</p>
            <Row className="align-items-center justify-content-center">
                <Col>
                    <div>Status:</div>
                </Col>
                <Col>
                    {imageRender()}
                </Col>
            </Row>
            <div className="mt-4">
                <Link to={service?.address as string} target="_blank" rel="noopener noreferrer">
                    <button className="btn">View</button>
                </Link>
                <Link to={'/edit-service'}>
                    <button className="btn mx-3">Edit</button>
                </Link>
                <button className="btn" onClick={() => deleteService()}>Delete</button>
            </div>

        </div>
    );
};