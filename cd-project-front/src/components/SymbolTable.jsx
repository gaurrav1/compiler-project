import React from 'react';

const SymbolTable = ({ symbolTable }) => (
  <div className="symbol-table">
    {Object.entries(symbolTable).map(([category, symbols]) => (
      <div key={category} className="symbol-category">
        <h3>{category}</h3>
        <ul>
          {symbols.map((symbol, index) => (
            <li key={index}>{symbol}</li>
          ))}
        </ul>
      </div>
    ))}
  </div>
);

export default SymbolTable;