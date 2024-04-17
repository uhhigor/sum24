import React, {useEffect, useState} from "react";
import {Container, Row, Col} from 'react-bootstrap';
import {Service} from "./Service";
import chunk from 'lodash-es/chunk';
import {Link} from "react-router-dom";
import {getAuthToken} from "../validateUser";

interface Service {
    id: number;
    name: string;
    address: string;
}

export const ServicesController = () => {
    const [services, setServices] = useState<Service[]>([]);


    useEffect(() => {
        fetchServices().then(r => console.log('ServicesControler fetched'));
    }, []);

    const fetchServices = async () => {
        try {
            const response = await fetch('http://localhost:8080/services/', {
                headers: {
                    'Authorization': "Bearer " + getAuthToken() as string
                }
            });
            if (!response.ok) {
                console.error('Failed to fetch services:', response);
            }
            const data = await response.json();
            setServices(data);
            console.log(getAuthToken())
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