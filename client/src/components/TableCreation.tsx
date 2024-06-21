import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {getUserId} from "../validateUser";
import '../styles/tableCreation.css';

export const TableCreation = () => {
    const [tableData, setTableData]
        = useState({columns: [], rows: []});

    const [rowsToShow, setRowsToShow] = useState(10);

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
        axios.get(`http://localhost:8080/tables/table/${service.id}?numberOfRows=${rowsToShow}`)
            .then(response => {
                setTableData(response.data);
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
                console.log(response.data);
            })
            .catch(error => {
                console.error(error);
            });
    }

    const selectRowsNumber = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setRowsToShow(Number(event.target.value));
    };

    return (
        <div>
            <h1 className="p-5">Fetch service data</h1>
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
                        <div className="d-flex align-items-center mb-3">
                            <label className="pe-4" htmlFor="rows">Show rows:</label>
                            <select id="rows" className="rowsSelect" value={rowsToShow} onChange={selectRowsNumber}>
                                <option value={10}>10</option>
                                <option value={50}>50</option>
                                <option value={100}>100</option>
                            </select>
                        </div>
                        <div className="d-flex align-items-center">
                            <button onClick={fetchTableData} className="btn btn-sm">Fetch Table Data</button>
                        </div>
                    </div>
                    <button className="btn mt-5 buttonBack" onClick={() => window.location.href = '/dashboard'}>Back to dashboard</button>
                </div>
                <div className="ms-5">
                    <table>
                        <thead>
                        <tr>
                            {tableData.columns.map((columnName, index) => (
                                <th className="px-5" key={index}>{columnName}</th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {tableData.rows.map((row: string[], rowIndex: number) => (
                            <tr key={rowIndex}>
                                {row.map((cell: string, cellIndex: number) => (
                                    <td className="px-5 py-2" key={cellIndex}>{cell}</td>
                                ))}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};