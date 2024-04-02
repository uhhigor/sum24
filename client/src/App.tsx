import React from 'react';
import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Login } from './components/Login';
import { Signup } from './components/Signup';
import { Dashboard } from './components/Dashboard';
import {AddService} from "./components/AddService";
import {EditService} from './components/EditService';
import {TableCreation} from "./components/TableCreation";
import {EditService} from "./components/EditService";
import {Main} from "./components/Main";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path='/dashboard' element={<Dashboard/>}/>
                <Route path='/login' element={<Login/>}/>
                <Route path='/signup' element={<Signup/>}/>
                <Route path='/add-service' element={<AddService/>}/>
                <Route path='/' element={<Main/>}/>
                <Route path='/edit-service' element={<EditService/>}/>
                <Route path='/create-table' element={ <TableCreation /> } />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
