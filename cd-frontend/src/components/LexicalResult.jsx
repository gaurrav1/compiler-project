export function LexicalResult({lexicalResult}) {
  return (
    <>
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
    </>
  )
}
