import React from 'react';

const TokenList = ({ tokens }) => (
  <div className="token-list">
    <table>
      <thead>
        <tr>
          <th>Line</th>
          <th>Category</th>
          <th>Value</th>
        </tr>
      </thead>
      <tbody>
        {tokens.map((token, index) => (
          <tr key={index}>
            <td>{token.lineNumber}</td>
            <td>{token.category}</td>
            <td>{token.value}</td>
          </tr>
        ))}
      </tbody>
    </table>
  </div>
);

export default TokenList;