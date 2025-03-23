import React, { useState } from 'react';
import CodeEditor from './CodeEditor';
import TokenList from './TokenList';
import SymbolList from './SymbolList';
import SymbolTable from './SymbolTable';
import Loader from './Loader';
import { analyzeCode } from '../services/api';
import './styles/App.css';

export function App() {
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleAnalysis = async (code) => {
    try {
      setLoading(true);
      setError('');
      const response = await analyzeCode(code);
      setResults(response.data);
    } catch (err) {
      setError('Error analyzing code. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app">
      <h1>Lexical Analyzer</h1>
      <CodeEditor onAnalyze={handleAnalysis} />
      
      {loading && <Loader />}
      {error && <div className="error">{error}</div>}
      
      {results && (
        <div className="results-container">
          <div className="results-section">
            <h2>Tokens</h2>
            <TokenList tokens={results.tokens} />
          </div>
          
          <div className="results-section">
            <h2>Symbol Table</h2>
            <SymbolTable symbolTable={results.symbolTable} />
          </div>
          
          <div className="results-section">
            <h2>Symbol Entries</h2>
            <SymbolList symbols={results.symbolEntries} />
          </div>
        </div>
      )}
    </div>
  );
}
