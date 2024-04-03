import React, { useState } from 'react';
import axios from 'axios';

export const TableCreation = () => {
    const [tableData, setTableData]
        = useState({ columns: [], rows: [] });

    const [rowsToShow, setRowsToShow] = useState(10);

    const [service, setService] = useState('table');

    const fetchTableData = () => {
        axios.get(`http://localhost:8080/tables/table?numberOfRows=${rowsToShow}`)
            .then(response => {
                setTableData(response.data);
            })
            .catch(error => {
                console.error(error);
            });
    };

    const handleRowsChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setRowsToShow(Number(event.target.value));
    };

    const selectService = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setService(event.target.value);
    }

    return (
        <div>
            <h1>Table</h1>
            <label htmlFor="rows">Show rows:</label>
            <select id="rows" value={rowsToShow} onChange={handleRowsChange}>
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