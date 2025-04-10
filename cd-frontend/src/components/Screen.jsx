import { useState } from "react";
import axios from "axios";
import { SyntaxResult } from "./SyntaxResult";
import { LexicalResult } from "./LexicalResult";

export function Screen() {
    const [code, setCode] = useState('');
    const [lexicalResult, setLexicalResult] = useState(null);
    const [syntaxResult, setSyntaxResult] = useState(null);

    const handleLexical = async () => {
        const res = await axios.post("http://localhost:8080/api/compiler/lexical", code, {
            headers: { "Content-Type": "text/plain" }
        });
        setLexicalResult(res.data);
    };

    const handleSyntax = async () => {
        const res = await axios.post("http://localhost:8080/api/compiler/syntax", code, {
            headers: { "Content-Type": "text/plain" }
        });
        setSyntaxResult(res.data);
    };

    return (
        <div className="p-4 max-w-screen-lg mx-auto font-mono">
            <h1 className="text-2xl font-bold mb-4">🧠 Compiler Playground</h1>

            <textarea
                rows={12}
                className="w-full border rounded p-2 mb-4"
                value={code}
                onChange={(e) => setCode(e.target.value)}
            />

            <div className="space-x-2 mb-4">
                <button className="bg-blue-500 text-white px-4 py-1 rounded" onClick={handleLexical}>Run Lexical</button>
                <button className="bg-green-600 text-white px-4 py-1 rounded" onClick={handleSyntax}>Run Syntax</button>
            </div>

            <LexicalResult lexicalResult={lexicalResult} />
            <SyntaxResult syntaxResult={syntaxResult} />

        </div>
    );
}