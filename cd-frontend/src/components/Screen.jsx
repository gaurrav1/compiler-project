import { useState } from "react";
import axios from "axios";
import { SyntaxResult } from "./SyntaxResult";
import { LexicalResult } from "./LexicalResult";
import { ChevronDown, X } from "lucide-react";

// Derive API base URL from runtime env first, then build-time env, then sensible fallback
const RUNTIME_ENV = (typeof window !== 'undefined' && window.__ENV) ? window.__ENV : {};
const API_BASE_URL =
  RUNTIME_ENV.API_BASE_URL ||
  import.meta.env.VITE_API_BASE_URL ||
  `${window.location.protocol}//${window.location.hostname}:${RUNTIME_ENV.BACKEND_PORT || import.meta.env.VITE_BACKEND_PORT || 8080}`;

export function Screen() {
    const [code, setCode] = useState('');
    const [lexicalResult, setLexicalResult] = useState(null);
    const [syntaxResult, setSyntaxResult] = useState(null);
    const [expanded, setExpanded] = useState(null);

    const handleLexical = async () => {
        const res = await axios.post(`${API_BASE_URL}/api/compiler/lexical`, code, {
            headers: { "Content-Type": "text/plain" }
        });
        setLexicalResult(res.data);
    };

    const handleSyntax = async () => {
        const res = await axios.post(`${API_BASE_URL}/api/compiler/syntax`, code, {
            headers: { "Content-Type": "text/plain" }
        });
        setSyntaxResult(res.data);
    };

    const handleExpand = (section) => {
        setExpanded(section);
    };

    const handleClose = () => {
        setExpanded(null);
    };

    return (
        <div className="flex h-screen font-mono">
            {/* Left Side */}
            <div className={`transition-all duration-300 ${expanded ? 'w-[30%]' : 'w-1/2'} border-r p-4`}>
                <h1 className="text-xl font-bold mb-4">🧠 Compiler Playground</h1>

                <textarea
                    rows={12}
                    className="w-full border rounded p-2 mb-4"
                    value={code}
                    onChange={(e) => setCode(e.target.value)}
                />

                <div className="space-x-2">
                    <button className="bg-blue-500 text-white px-4 py-1 rounded" onClick={handleLexical}>Run Lexical</button>
                    <button className="bg-green-600 text-white px-4 py-1 rounded" onClick={handleSyntax}>Run Syntax</button>
                </div>
            </div>

            {/* Right Side */}
            <div className={`transition-all duration-300 ${expanded ? 'w-[70%]' : 'w-1/2'} overflow-y-auto p-4`}>
                {expanded === null && (
                    <div className="space-y-4">
                        {/* Accordions */}
                        <div className="border rounded">
                            <button onClick={() => handleExpand("lexical")} className="w-full flex justify-between items-center p-3 bg-gray-100 hover:bg-gray-200">
                                <span className="font-semibold">Lexical Result</span>
                                <ChevronDown />
                            </button>
                        </div>
                        <div className="border rounded">
                            <button onClick={() => handleExpand("syntax")} className="w-full flex justify-between items-center p-3 bg-gray-100 hover:bg-gray-200">
                                <span className="font-semibold">Syntax Result</span>
                                <ChevronDown />
                            </button>
                        </div>
                    </div>
                )}

                {expanded === "lexical" && (
                    <div>
                        <div className="flex justify-between items-center mb-2">
                            <h2 className="text-xl font-bold">Lexical Analysis</h2>
                            <button onClick={handleClose} className="text-red-600 hover:text-red-800"><X /></button>
                        </div>
                        <LexicalResult lexicalResult={lexicalResult} />
                    </div>
                )}

                {expanded === "syntax" && (
                    <div>
                        <div className="flex justify-between items-center mb-2">
                            <h2 className="text-xl font-bold">Syntax Analysis</h2>
                            <button onClick={handleClose} className="text-red-600 hover:text-red-800"><X /></button>
                        </div>
                        <SyntaxResult syntaxResult={syntaxResult} />
                    </div>
                )}
            </div>
        </div>
    );
}
