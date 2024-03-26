import React from "react";
import { Sidebar } from "./Sidebar";
import '../styles/main.css'

export const Main = () => {
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
                            <h1 className="ms-5 mt-3">DASHBOARD</h1>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-6">
                            <div className="row justify-content-center">
                                
                            </div>
                        </div>
                        <div className="col-6">
                            <div className="row justify-content-center">
                                <div className="col-10 m-3 widget">
                                    
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                {/* Workhours */}
            </div>
            {/* row */}
        </div>
    )
}