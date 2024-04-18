import { FaChartLine, FaGaugeHigh, FaPowerOff } from "react-icons/fa6";
import photo from "../resources/avatar.jpg";
import '../styles/sidebar.css'
import {getUsername} from "../validateUser";

export const Sidebar = () => {
    function signOut() {
        localStorage.clear();
        window.location.href = '/login';
    }

    return (
        <div className="sidebar-container">
            <div className="sidebar-header">
                <img src={photo} alt="Avatar" />
                <span className="username">{getUsername()}</span>
            </div>

            <div className="sidebar-nav">
                <a href="/create-table" className="sidebar-item">
                    <div className="sidebar-item-content">
                        <FaChartLine />
                        <span>Services data</span>
                    </div>
                </a>
                <a
                href="#"
                data-bs-toggle="modal" 
                data-bs-target="#template" 
                className="sidebar-item">
                    <div className="sidebar-item-content">
                        <FaGaugeHigh />
                        <span>Option 2</span>
                    </div>
                </a>
                <a className="sidebar-item signout">
                    <div className="sidebar-item-content">
                        <FaPowerOff />
                        <span onClick={signOut}>Sign out</span>
                    </div>
                </a>
            </div>
        </div>
    )
}