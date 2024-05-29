import React, {useEffect, useState} from "react";
import {Container, Row, Col} from 'react-bootstrap';
import {Service} from "./Service";
import chunk from "lodash-es/chunk";
import {Link} from "react-router-dom";
import {getAuthToken, getUsername} from "../validateUser";
import axios from "axios";
import {toNumber} from "lodash";

interface Service {
    id: number;
    name: string;
    address: string;
}

export const ServicesController = () => {
    const [services, setServices] = useState<Service[]>([]);

    useEffect(() => {
        const fetchUserIdAndServices = async () => {
            await getId();
            const storedUserId = toNumber(localStorage.getItem('userId'));
            if (storedUserId) {
                await fetchServices(storedUserId);
            }
        };

        fetchUserIdAndServices().then(r => console.log("Fetched services"));
    }, []);

    const getId = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/login/${getUsername()}`);
            if (response.status === 200) {
                localStorage.setItem('userId', response.data);
            }
        } catch (error) {
            console.error(error);
        }
    }

    const fetchServices = async (userId: number) => {
        try {
            const response = await fetch(`http://localhost:8080/services/user/${userId}`, {
                headers: {
                    'Authorization': "Bearer " + getAuthToken()
                }
            });
            if (!response.ok) {
                console.error('Failed to fetch services:', response);
                return;
            }
            const data = await response.json();
            setServices(data);
        } catch (error) {
            console.error('Error fetching services:', error);
        }
    };

    return (
        <Container>
            <Link to={'/add-service'}>
                <button className="btn m-3 mt-5 mb-5 btn-lg">Add service</button>
            </Link>
            {chunk(services, 3).map((rowServices: Service[], rowIndex: number) => (
                <Row key={rowIndex}>
                    {rowServices.map((service: Service, colIndex: number) => (
                        <Col key={colIndex} className="cols">
                            <Service key={colIndex} index={service.id}/>
                        </Col>
                    ))}
                </Row>
            ))}
        </Container>
    );
};