import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';




//import Navbar from "./components/navbar/navbar";
import Home from './components/home/home';
import Login from './components/login/login'
import Register from './components/register/register';
import Navbar from './components/navbar/navbar';
import Dashboard from './components/dashboard/dashboard';
import Profile from './components/profile/profile'
import ProtectedRoute from './components/ProtectedRoute';


function App() {
  return (
    //  <><Navbar />
    //  <Home /></>
    //  <Home />



    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} /> {/* Rotta Home */}
        <Route path="/login" element={<Login />} /> {/* Rotta Login */}
        <Route path="/register" element={<Register />} /> {/* Rotta Login */}


        {/* Rotte protette */}
        <Route path="/dashboard"element={<ProtectedRoute><Dashboard /></ProtectedRoute>
          }
        />

        



        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <Profile />
            </ProtectedRoute>
          }
        />



        {/* <Route
          path="/gallery"
          element={
            <ProtectedRoute>
              <Gallery />
            </ProtectedRoute>
          }
        /> */}




        {/* <Route path="/dashboard" element={<Dashboard />} /> Rotta Login */}
      </Routes>
    </Router>
  );
}

export default App;
