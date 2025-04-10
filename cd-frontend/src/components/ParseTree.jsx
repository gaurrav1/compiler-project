import { useState } from "react";

export function ParseTree({ tree }) {
  const [expanded, setExpanded] = useState(true);

  const hasChildren = tree.children && tree.children.length > 0;

  return (
    <div className="ml-4">
      <div
        className={`cursor-pointer flex items-center gap-2 ${hasChildren ? "hover:text-blue-600" : ""}`}
        onClick={() => hasChildren && setExpanded(prev => !prev)}
      >
        {hasChildren && (
          <span className="text-sm text-gray-500">
            {expanded ? "▼" : "▶"}
          </span>
        )}
        <span>
          <span className="font-semibold text-purple-700">{tree.name}</span>
          {tree.value !== null && (
            <span className="text-gray-700"> : <span className="text-green-600 font-mono">{tree.value}</span></span>
          )}
        </span>
      </div>

      {hasChildren && expanded && (
        <div className="ml-6 border-l pl-4 border-gray-300">
          {tree.children.map((child, i) => (
            <ParseTree key={i} tree={child} />
          ))}
        </div>
      )}
    </div>
  );
}
