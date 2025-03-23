import React from 'react';

const TokenList = ({ tokens, errors }) => (
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
      {tokens.map((token, index) => {
          const error = errors.find(e => 
            e.lineNumber === token.lineNumber && 
            e.message.includes(token.value)
          );
          
          return (
            <tr key={index} className={error ? 'error-row' : ''}>
              <td>{token.lineNumber}</td>
              <td>{token.category}</td>
              <td>
                {token.value}
                {error && <span className="error-marker">!</span>}
              </td>
            </tr>
          );
        })}
      </tbody>
    </table>
  </div>
);

export default TokenList;