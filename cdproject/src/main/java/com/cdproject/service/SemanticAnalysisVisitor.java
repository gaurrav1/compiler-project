package com.cdproject.service;

import com.cdproject.grammer.Java8BaseVisitor;
import com.cdproject.grammer.Java8Parser;
import com.cdproject.model.AnalysisError;
import lombok.Getter;

import java.util.*;

public class SemanticAnalysisVisitor extends Java8BaseVisitor<Void> {
    private final List<Map<String, String>> scopes = new ArrayList<>();
    @Getter
    private final List<AnalysisError> errors = new ArrayList<>();

    public SemanticAnalysisVisitor(Map<String, String> symbolTable) {
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
        if (!isVariableDeclared(varName)) {
            errors.add(new AnalysisError(ctx.getStart().getLine(),
                    "Semantic error: Variable '" + varName + "' not declared."));
        }
        return super.visitVariableDeclaratorId(ctx);
    }

    private boolean isVariableDeclared(String varName) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(varName)) {
                return true;
            }
        }
        return false;
    }

    private void enterScope() {
        scopes.add(new HashMap<>());
    }

    private void exitScope() {
        if (scopes.size() > 1) {
            scopes.removeLast();
        }
    }

}