import React from 'react';

const SymbolList = ({ symbols }) => (
  <div className="symbol-list">
    <table>
      <thead>
        <tr>
          <th>Line</th>
          <th>Name</th>
          <th>Type</th>
          <th>Category</th>
        </tr>
      </thead>
      <tbody>
        {symbols.map((symbol, index) => (
          <tr key={index}>
            <td>{symbol.lineNumber}</td>
            <td>{symbol.name}</td>
            <td>{symbol.type}</td>
            <td>{symbol.category}</td>
          </tr>
        ))}
      </tbody>
    </table>
  </div>
);

export default SymbolList;