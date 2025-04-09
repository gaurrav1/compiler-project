import { useState } from "react";
import axios from "axios";

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

            {lexicalResult && (
                <div className="mb-6">
                    <h2 className="text-xl font-semibold mb-2">🔤 Lexical Tokens</h2>
                    <table className="w-full table-auto border">
                        <thead>
                            <tr>
                                <th className="border p-1">Line</th>
                                <th className="border p-1">Category</th>
                                <th className="border p-1">Value</th>
                            </tr>
                        </thead>
                        <tbody>
                            {lexicalResult.tokens
                            .filter(tok => tok.category !== "Ws")
                            .map((tok, i) => (
                                <tr key={i}>
                                    <td className="border p-1">{tok.lineNumber}</td>
                                    <td className="border p-1">{tok.category}</td>
                                    <td className="border p-1">{tok.value}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    {lexicalResult.errors.length > 0 && (
                        <>
                            <h3 className="mt-4 text-red-600">Errors:</h3>
                            <ul className="text-red-500 list-disc ml-6">
                                {lexicalResult.errors.map((e, i) => (
                                    <li key={i}>{e.message}</li>
                                ))}
                            </ul>
                        </>
                    )}
                </div>
            )}

            {syntaxResult && (
                <div>
                    <h2 className="text-xl font-semibold">🧱 Parse Tree</h2>
                    <div className="ml-4 text-sm bg-gray-100 p-2 rounded mt-2">
                        <ParseTree tree={syntaxResult.parseTree} />
                    </div>

                    <h2 className="text-xl font-semibold mt-6">📦 Symbol Table</h2>
                    <table className="w-full table-auto border mt-2">
                        <thead>
                            <tr>
                                <th className="border p-1">Name</th>
                                <th className="border p-1">Type</th>
                                <th className="border p-1">Scope</th>
                                <th className="border p-1">Line</th>
                            </tr>
                        </thead>
                        <tbody>
                            {syntaxResult.symbolTable.map((sym, i) => (
                                <tr key={i}>
                                    <td className="border p-1">{sym.name}</td>
                                    <td className="border p-1">{sym.type}</td>
                                    <td className="border p-1">{sym.scope}</td>
                                    <td className="border p-1">{sym.lineNumber}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    {syntaxResult.errors.length > 0 && (
                        <>
                            <h2 className="text-xl font-semibold text-red-600 mt-6">❌ Errors</h2>
                            <ul className="text-red-500 list-disc ml-6">
                                {syntaxResult.errors.map((e, i) => (
                                    <li key={i}>{e.message} (Line {e.lineNumber})</li>
                                ))}
                            </ul>
                        </>
                    )}

                    {syntaxResult.threeAddressCode && (
                        <>
                            <h2 className="text-xl font-semibold mt-6">🧮 Three Address Code</h2>
                            <pre className="bg-gray-100 p-2 rounded mt-2">{syntaxResult.threeAddressCode.join("\n")}</pre>
                        </>
                    )}
                </div>
            )}
        </div>
    );
}

function ParseTree({ tree }) {
    return (
        <div className="ml-2">
            <div>
                <strong>{tree.name}</strong> {tree.value ? `: ${tree.value}` : ""}
            </div>
            <ul className="ml-4 list-disc">
                {tree.children && tree.children.map((child, i) => (
                    <li key={i}>
                        <ParseTree tree={child} />
                    </li>
                ))}
            </ul>
        </div>
    );
}