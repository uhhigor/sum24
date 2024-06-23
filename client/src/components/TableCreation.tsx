import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {getUserId} from "../validateUser";
import '../styles/tableCreation.css';

export const TableCreation = () => {
    const [tableData, setTableData]
        = useState({columns: [], rows: []});

    const [rowsToShow, setRowsToShow] = useState(10);

    const [services, setServices] = useState([]);
    const [service, setService] = useState<any>(services[0]);
    const [serviceIndex, setServiceIndex] = useState<number>(0);
    const [selectedColumns, setSelectedColumns] = useState<any[]>(tableData.columns);

    useEffect(() => {
        getServices();
    }, []);

    function handleServiceChange(event: any) {
        setService(services[event.target.value]);
        setServiceIndex(event.target.value);
        console.log(services[event.target.value]);
    }

    const fetchTableData = () => {
        if (service?.fields) {

            axios.get(`http://localhost:8080/tables/table/${service.id}?numberOfRows=${rowsToShow}`)
                .then(response => {
                    const rowsWithDate = response.data.rows.map((row: any) => {
                        if (row["timeunix"] !== undefined) {
                            const date = new Date(row["timeunix"] * 1000);
                            return { ...row, timeunixDate: date.toLocaleString() };
                        } else {
                            return { ...row, timeunixDate: "" };
                        }
                    });
    
                    const sortedRows = rowsWithDate.sort((a: any, b: any) => b["timeunix"] - a["timeunix"]);
    
                    const transformedRows = sortedRows.map((row: any) => {
                        return response.data.columns.map((column: any) => {
                            if (column !== "timeunix") {
                                return row[column] !== undefined ? row[column] : "";
                            } else {
                                return row["timeunixDate"];
                            }
                        });
                    });
                    response.data.rows = transformedRows;
                    setTableData(response.data);
    
                    console.log(response.data);
                })
                .catch(error => {
                    console.error(error);
                });
        } else {
            window.alert("Service do not have saved metrics!");
        }
    };

    const getServices = () => {
        axios.get(`http://localhost:8080/service/user/${getUserId()}`)
            .then(response => {
                setServices(response.data.services);
                setService(response.data.services[0]);
                setServiceIndex(0);
                console.log(response.data);
            })
            .catch(error => {
                console.error(error);
            });
    }

    const selectRowsNumber = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setRowsToShow(Number(event.target.value));
    };

    const handleCheckboxChange = (column: any) => {

        setSelectedColumns((prevSelected: any) => {
            if (prevSelected.includes(column)) {
                return prevSelected.filter((col: any) => col !== column);
            } else {
                return [...prevSelected, column];
            }
        });
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
                                {services?.map((service: any, index: number) => (
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
                    <div className='columns-check ms-5'>
                        {service?.fields?.map((field: any) => (
                            <label key={field}>
                                <input
                                    type="checkbox"
                                    checked={selectedColumns.includes(field)}
                                    onChange={() => handleCheckboxChange(field)}
                                />
                                {field}
                            </label>
                        ))}
                    </div>
                    <button className="btn mt-5 buttonBack" onClick={() => window.location.href = '/dashboard'}>Back to dashboard</button>
                </div>
                
                <div className="ms-5">
                    <table className='table table-dark table-striped'>
                        <thead>
                        <tr>
                        {selectedColumns.map((column: any) => (
                            <th key={column}>{column}</th>
                        ))}
                        </tr>
                        </thead>
                        <tbody>
                        {tableData.rows.map((row: any, rowIndex: number) => (
                            <tr key={rowIndex}>
                                {selectedColumns.map((column: any, index: any) => {
                                    const it = tableData.columns.findIndex((el: any) => {
                                        return el === column
                                    })
                                    return (
                                        <td key={column}>
                                            {row[it]}
                                        </td>
                                    )
                                })}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};