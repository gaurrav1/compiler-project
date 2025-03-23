import React, { useState } from 'react';
import './styles/CodeEditor.css';

const CodeEditor = ({ onAnalyze }) => {
  const [code, setCode] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onAnalyze(code);
  };

  return (
    <form onSubmit={handleSubmit} className="code-editor">
      <textarea
        value={code}
        onChange={(e) => setCode(e.target.value)}
        placeholder="Enter your code here..."
        rows={15}
      />
      <button type="submit" className="analyze-button">
        Analyze Code
      </button>
    </form>
  );
};

export default CodeEditor;