import React from 'react';
import { FaSpinner } from 'react-icons/fa';

const Loader = () => (
  <div className="loader">
    <FaSpinner className="spinner" />
    <p>Analyzing Code...</p>
  </div>
);

export default Loader;