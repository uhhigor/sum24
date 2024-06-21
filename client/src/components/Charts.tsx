import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {getUserId} from "../validateUser";


export const Charts = () => {

    const [chartData, setChartData] = useState();
    const [services, setServices] = useState([]);
    const [service, setService] = useState<any>();
    const [serviceIndex, setServiceIndex] = useState<number>(0);

    useEffect(() => {
        getServices();
    }, []);

    function handleServiceChange(event: any) {
        setService(services[event.target.value]);
        setServiceIndex(event.target.value);
        console.log(services[event.target.value]);
    }

    const fetchTableData = () => {
        axios.get(`http://localhost:8080/service/${service.id}`)
            .then(response => {
                console.log(response.data);
            })
            .catch(error => {
                console.error(error);
            });
    };

    const getServices = () => {
        axios.get(`http://localhost:8080/service/user/${getUserId()}`)
            .then(response => {
                setServices(response.data);
                setService(response.data[0]);
            })
            .catch(error => {
                console.error(error);
            });
    }

    return (
        <>
            <h1 className="p-5">Charts of data</h1>
            <div className="d-flex">
                <div>
                    <div className="ps-5 fetchTableDataForm ms-5 me-5">
                        <div className="d-flex align-items-center mb-3 pt-5">
                            <label htmlFor="services" className="pe-4">Select Service:</label>
                            <select onChange={handleServiceChange} id="services" value={serviceIndex}>
                                {services.map((service: any, index: number) => (
                                    <option key={index} value={index}>{service?.name}</option>
                                ))}
                            </select>
                        </div>
                        <div className="d-flex align-items-center">
                            <button onClick={fetchTableData} className="btn btn-sm">Fetch Table Data</button>
                        </div>
                    </div>
                    <button className="btn mt-5 buttonBack" onClick={() => window.location.href = '/dashboard'}>Back to dashboard</button>
                </div>
                <div className="ms-5">
                    
                </div>
            </div>
        </>
    );
}