import React from 'react';
import './styles/App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Login } from './components/Login';
import { Signup } from './components/Signup';
import { Dashboard } from './components/Dashboard';
import {AddService} from "./components/AddService";
import {TableCreation} from "./components/TableCreation";
import {Main} from "./components/Main";
import EditService from "./components/EditService";
import { Charts } from './components/Charts';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path='/dashboard' element={<Dashboard/>}/>
                <Route path='/login' element={<Login/>}/>
                <Route path='/signup' element={<Signup/>}/>
                <Route path='/add-service' element={<AddService/>}/>
                <Route path='/' element={<Main/>}/>
                <Route path='/edit-service/:index' element={<EditService/>}/>
                <Route path='/create-table' element={ <TableCreation /> } />
                <Route path='/charts' element={ <Charts /> } />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
