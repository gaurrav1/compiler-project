package com.cdproject.service;

import com.cdproject.grammer.Java8BaseVisitor;
import com.cdproject.grammer.Java8Parser;
import com.cdproject.model.AnalysisError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class SemanticAnalysisVisitor extends Java8BaseVisitor<Void> {
    private final Map<String, String> symbolTable;
    private final List<AnalysisError> errors;
    private final List<Map<String, String>> scopes = new ArrayList<>();

    public SemanticAnalysisVisitor(Map<String, String> symbolTable) {
        this.symbolTable = new HashMap<>(symbolTable);
        this.errors = new ArrayList<>();
        scopes.add(new HashMap<>(symbolTable)); // Global scope
    }

    @Override
    public Void visitBlock(Java8Parser.BlockContext ctx) {
        enterScope();
        super.visitBlock(ctx);
        exitScope();
        return null;
    }

    @Override
    public Void visitVariableDeclaratorId(Java8Parser.VariableDeclaratorIdContext ctx) {
        String varName = ctx.Identifier().getText();
        if (!currentScope().containsKey(varName)) {
            errors.add(new AnalysisError(ctx.getStart().getLine(), 
                "Semantic error: Variable '" + varName + "' not declared."));
        }
        return super.visitVariableDeclaratorId(ctx);
    }

    private void enterScope() {
        scopes.add(new HashMap<>());
    }

    private void exitScope() {
        if (scopes.size() > 1) scopes.remove(scopes.size() - 1);
    }

    private Map<String, String> currentScope() {
        return scopes.get(scopes.size() - 1);
    }

    public List<AnalysisError> getErrors() {
        return errors;
    }
}