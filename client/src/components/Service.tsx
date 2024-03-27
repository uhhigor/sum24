import React, {useEffect, useState} from "react";
import {FcApproval, FcHighPriority} from "react-icons/fc";
import {Col, Row} from "react-bootstrap";
import '../styles/service.css';


interface Service {
    id: number;
    name: string;
    address: string;
}

export const Service : React.FC = () => {

    const [services, setServices] = useState<Service[]>([]);

    // useEffect(() => {
    //     fetchServices().then(r => console.log('Services fetched'));
    // }, []);
    //
    // const fetchServices = async () => {
    //     try {
    //         const response = await fetch('http://localhost:8080/services');
    //         if (!response.ok) {
    //             console.error('Failed to fetch services:', response);
    //         }
    //         const data: Service[] = await response.json();
    //         setServices(data);
    //     } catch (error) {
    //         console.error('Error fetching services:', error);
    //     }
    // };

    function imageRender() {
        if (true) {
            return <FcApproval size={20}/>
        } else {
            return <FcHighPriority  size={20}/>
        }
    }

    return (
        <div className="m-2 service-container">
            {/*<h2>Services</h2>*/}
            {/*<ul>*/}
            {/*    {services.map(service => (*/}
            {/*        <li key={service.id}>*/}
            {/*            <p>ID: {service.id}</p>*/}
            {/*            <p>Name: {service.name}</p>*/}
            {/*            <p>Address: {service.address}</p>*/}
            {/*        </li>*/}
            {/*    ))}*/}
            {/*</ul>*/}

            <h5>Service name</h5>
            <p>Service address</p>
            <Row className="align-items-center justify-content-center">
                <Col>
                    <div>Status:</div>
                </Col>
                <Col>
                    {imageRender()}
                </Col>
            </Row>
            <div className="mt-4">
                <button className="btn">View</button>
                <button className="m-lg-3 btn">Edit</button>
                <button className="btn">Delete</button>
            </div>

        </div>
    );
};