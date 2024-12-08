import './home.css';
import photodom from '../../images/photo-dom.webp'
import secure from '../../images/secure-fast.webp'
import whatis from '../../images/what-is-photodom.webp'



import React from "react";

function Home() {
  return (
    <div className="home">
      <div id="carousel" className="carousel slide carousel-fade" data-bs-ride="carousel">
        <div className="carousel-inner">
          <div className="carousel-item active">
            <img
              src={photodom}
              className="d-block w-100"
              alt="PhotoDom"
            />
          </div>
          <div className="carousel-item">
            <img
              src={whatis}
              className="d-block w-100"
              alt="WhatIs"
            />
          </div>
          <div className="carousel-item">
            <img
              src={secure}
              className="d-block w-100"
              alt="Upload"
            />
          </div>
        </div>
        <button
          className="carousel-control-prev"
          type="button"
          data-bs-target="#carousel"
          data-bs-slide="prev"
        >
          <span className="carousel-control-prev-icon" aria-hidden="true" />
          {/* <span className="visually-hidden">Previous</span> */}
        </button>
        <button
          className="carousel-control-next"
          type="button"
          data-bs-target="#carousel"
          data-bs-slide="next"
        >
          <span className="carousel-control-next-icon" aria-hidden="true" />
          {/* <span className="visually-hidden">Next</span> */}
        </button>
      </div>

      {/* Contenitore Pulsanti */}
      <div className="home-buttons container-card">
        <button
          className="btn btn-btn btn-primary"
          onClick={() => (window.location.href = "/login")}
        >
          Login
        </button>
        <button
          className="btn btn-secondary"
          onClick={() => (window.location.href = "/register")}
        >
          Registrati
        </button>
      </div>
    </div>
  );
}

export default Home;
