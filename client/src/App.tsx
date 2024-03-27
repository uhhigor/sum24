import React from 'react';
import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Login } from './components/Login';
import { Signup } from './components/Signup';
import { Main } from './components/Main';
import {AddService} from "./components/AddService";

function App() {
  return (
    <BrowserRouter>
        <Routes>
            <Route path='/' element={ <Main /> } />
            <Route path='/login' element={ <Login /> } />
            <Route path='/signup' element={ <Signup /> } />
            <Route path='/add-service' element={ <AddService /> } />
        </Routes>
    </BrowserRouter>
);
}

export default App;
