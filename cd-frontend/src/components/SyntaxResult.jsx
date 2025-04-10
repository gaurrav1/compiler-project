import { ParseTree } from "./ParseTree";

export function SyntaxResult({syntaxResult}) {
  return (
    <>
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
    </>
  )
}
