import axios from 'axios';

const API_URL = 'http://localhost:8080/api/lexical-analysis';

export const analyzeCode = async (code) => {
  return axios.post(API_URL, code, {
    headers: {
      'Content-Type': 'text/plain'
    }
  });
};