import React from "react";
import { Sidebar } from "./Sidebar";
import {ServicesController} from './ServicesController';
import '../styles/dashboard.css';

export const Dashboard = () => {
    return (
        <div className="main-container">
            <div className="row">
                <div className="col-2">
                    <Sidebar />
                </div>

                {/* Workhours */}
                <div className="col-10">
                    <div className="row">
                        <div className="col-6">
                            <h1 className="ms-3 mt-3">DASHBOARD</h1>
                        </div>
                    </div>
                    <ServicesController />
                </div>
                {/* Workhours */}
            </div>
            {/* row */}
        </div>
    )
}