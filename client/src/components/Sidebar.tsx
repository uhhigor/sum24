import { FaChartLine, FaGaugeHigh, FaPowerOff } from "react-icons/fa6";
import photo from "../assets/avatar.jpg";
import '../styles/sidebar.css'

export const Sidebar = () => {
    return (
        <div className="sidebar-container">
            <div className="sidebar-header">
                <img src={photo} alt="Avatar" />
                <span className="username">username</span>
                <span className="email">email</span>
            </div>

            <div className="sidebar-nav">
                <a href="" className="sidebar-item">
                    <div className="sidebar-item-content">
                        <FaChartLine />
                        <span>Option 1</span>
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
                        <span>Sign out</span>
                    </div>
                </a>
            </div>
        </div>
    )
}