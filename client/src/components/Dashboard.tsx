import React from "react";
import { Sidebar } from "./Sidebar";
import {ServicesController} from './ServicesController';
import '../styles/dashboard.css';

export const Dashboard = () => {
    return (
        <div>
            <div className="row">
                <div className="col-2 position-fixed">
                    <Sidebar />
                </div>
                <div className="col-10 ms-auto">
                    <div className="row">
                        <div className="col-6">
                            <h1 className="ms-3 mt-3">DASHBOARD</h1>
                        </div>
                    </div>
                    <ServicesController />
                </div>
            </div>
        </div>
    )
}