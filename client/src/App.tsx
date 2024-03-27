import React from 'react';
import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Login } from './components/Login';
import { Signup } from './components/Signup';
import { Main } from './components/Main';

function App() {
  return (
    <BrowserRouter>
        <Routes>
            <Route path='/' element={ <Main /> } />
            <Route path='/login' element={ <Login /> } />
            <Route path='/signup' element={ <Signup /> } />
        </Routes>
    </BrowserRouter>
);
}

export default App;
