import React, {useEffect, useState} from "react";
import {Container, Row, Col} from 'react-bootstrap';
import {Service} from "./Service";
import chunk from 'lodash-es/chunk';
import {Link} from "react-router-dom";

interface Service {
    id: number;
    name: string;
    address: string;
}

export const Dashboard = () => {
    // const [services, setServices] = useState<Service[]>([]);
    //
    //
    // useEffect(() => {
    //     fetchServices().then(r => console.log('Services fetched'));
    // }, []);
    //
    // const fetchServices = async () => {
    //     try {
    //         const response = await fetch('http://localhost:8080/services/');
    //         if (!response.ok) {
    //             console.error('Failed to fetch services:', response);
    //         }
    //         const data = await response.json();
    //         setServices(data);
    //     } catch (error) {
    //         console.error('Error fetching services:', error);
    //     }
    // };

    return (
        // <Container>
        //     {chunk(services, 3).map((rowServices: Service[], rowIndex: number) => (
        //         <Row key={rowIndex}>
        //             {rowServices.map((service: Service, colIndex: number) => (
        //                 <Col key={colIndex}>
        //                     <Service key={colIndex} index={rowIndex * 3 + colIndex} />
        //                 </Col>
        //             ))}
        //         </Row>
        //     ))}
        // </Container>
        <Container>
            <Link to={'/add-service'}>
                <button className="btn m-3 mt-5 mb-5 btn-lg">Add service</button>
            </Link>
            <Row>
                <Col md={4}>
                    <Service index={0}/>
                </Col>
            </Row>
        </Container>
    );
};