import React from "react";
import { Container, Row, Col } from 'react-bootstrap';
import { Service } from "./Service";

export const Dashboard = () => {
    return (
        <Container>
            <Row>
                <Col md={4}>
                    <Service/>
                </Col>
                <Col md={4}>
                    <Service/>
                </Col>
                <Col md={4}>
                    <Service/>
                </Col>
            </Row>
            <Row>
                <Col md={4}>
                    <Service/>
                </Col>
                <Col md={4}>
                    <Service/>
                </Col>
                <Col md={4}>
                    <Service/>
                </Col>
            </Row>
            <Row>
                <Col md={4}>
                    <Service/>
                </Col>
                <Col md={4}>
                    <Service/>
                </Col>
                <Col md={4}>
                    <Service/>
                </Col>
            </Row>
        </Container>
    );
};