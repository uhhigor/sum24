import React, {useEffect, useState} from 'react';
import axios from 'axios';

export const TableCreation = () => {
    const [tableData, setTableData]
        = useState({ columns: [], rows: [] });

    const [rowsToShow, setRowsToShow] = useState(10);

    const [services, setServices] = useState([]);

    useEffect(() => {
        getServices();
    }, []);

    const fetchTableData = () => {
        axios.get(`http://localhost:8080/tables/table?numberOfRows=${rowsToShow}`)
            .then(response => {
                setTableData(response.data);
            })
            .catch(error => {
                console.error(error);
            });
    };

    const getServices = () => {
        axios.get(`http://localhost:8080/services/user/802`)
            .then(response => {
                const serviceData = response.data;
                const service = serviceData.map((service: any) => service.name);
                setServices(service);
            })
            .catch(error => {
                console.error(error);
            });
    }



    const selectRowsNumber = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setRowsToShow(Number(event.target.value));
    };

    // const selectService = (event: React.ChangeEvent<HTMLSelectElement>) => {
    //     setServices((event.target.value));
    // }

    return (
        <div>
            <h1>Table</h1>
            <label htmlFor="services">Select Service:</label>
            <select id="services" value={services}>
                {services.map((service: any, index: number) => (
                    <option key={index} value={service}>{service}</option>
                ))}
            </select>
            <label htmlFor="rows">Show rows:</label>
            <select id="rows" value={rowsToShow} onChange={selectRowsNumber}>
                <option value={10}>10</option>
                <option value={50}>50</option>
                <option value={100}>100</option>
            </select>
            <button onClick={fetchTableData}>Fetch Table Data</button>
            <table>
                <thead>
                <tr>
                    {tableData.columns.map((columnName, index) => (
                        <th key={index}>{columnName}</th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {tableData.rows.map((row: string[], rowIndex: number) => (
                    <tr key={rowIndex}>
                        {row.map((cell: string, cellIndex: number) => (
                            <td key={cellIndex}>{cell}</td>
                        ))}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};