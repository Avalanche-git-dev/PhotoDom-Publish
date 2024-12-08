import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';




//import Navbar from "./components/navbar/navbar";
import Home from './components/home/home';
import Login from  './components/login/login'
import Register from './components/register/register';

function App() {
  return (
  //  <><Navbar />
  //  <Home /></>
  //  <Home />



  <Router>
  <Routes>
    <Route path="/" element={<Home />} /> {/* Rotta Home */}
    <Route path="/login" element={<Login />} /> {/* Rotta Login */}
    <Route path="/register" element={<Register />} /> {/* Rotta Login */}
  </Routes>
</Router>
  );
}

export default App;
