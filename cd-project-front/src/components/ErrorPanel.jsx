export const ErrorPanel = ({ errors }) => (
    <div className="error-panel">
      <h3>Errors</h3>
      <ul>
        {errors.map((error, index) => (
          <li key={index}>
            Line {error.lineNumber}: {error.message}
          </li>
        ))}
      </ul>
    </div>
  );