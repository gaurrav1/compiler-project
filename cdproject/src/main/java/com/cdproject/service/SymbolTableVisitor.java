package com.cdproject.service;

import com.cdproject.grammer.Java8BaseVisitor;
import com.cdproject.grammer.Java8Parser;
import com.cdproject.model.SymbolEntry;
import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;

public class SymbolTableVisitor extends Java8BaseVisitor<Void> {

    @Getter
    private final List<SymbolEntry> symbolTable = new ArrayList<>();
    private final Stack<Map<String, SymbolEntry>> scopes = new Stack<>();
    private final Stack<String> scopeNames = new Stack<>();

    public SymbolTableVisitor() {
        scopes.push(new HashMap<>());
        scopeNames.push("Global");
    }

    private String getCurrentScopeName() {
        return scopeNames.peek();
    }

    @Override
    public Void visitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        Java8Parser.NormalClassDeclarationContext normalClass = ctx.normalClassDeclaration();
        if (normalClass != null) {
            String className = normalClass.Identifier().getText();
            scopeNames.push(className);
            scopes.push(new HashMap<>());
            visit(normalClass.classBody()); // Access classBody via normalClass
            scopes.pop();
            scopeNames.pop();
        }
        return null;
    }

    @Override
    public Void visitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        String methodName = ctx.methodHeader().methodDeclarator().Identifier().getText();
        scopeNames.push(methodName);
        scopes.push(new HashMap<>());

        // Process parameters
        Java8Parser.FormalParameterListContext params = ctx.methodHeader().methodDeclarator().formalParameterList();
        if (params != null) {
            visit(params);
        }

        // Visit method body without pushing a new scope for the block
        visit(ctx.methodBody());

        scopes.pop();
        scopeNames.pop();
        return null;
    }

    @Override
    public Void visitBlock(Java8Parser.BlockContext ctx) {
        // Push new scope only if not part of method body
        if (!isMethodBody(ctx)) {
            scopeNames.push("block");
            scopes.push(new HashMap<>());
        }

        super.visitBlock(ctx);

        if (!isMethodBody(ctx)) {
            scopes.pop();
            scopeNames.pop();
        }
        return null;
    }

    private boolean isMethodBody(Java8Parser.BlockContext ctx) {
        return ctx.getParent() instanceof Java8Parser.MethodBodyContext;
    }

    @Override
    public Void visitFormalParameter(Java8Parser.FormalParameterContext ctx) {
        String type = ctx.unannType().getText();
        String name = ctx.variableDeclaratorId().Identifier().getText();
        SymbolEntry entry = new SymbolEntry(name, type, getCurrentScopeName(), ctx.start.getLine());
        symbolTable.add(entry);
        scopes.peek().put(name, entry);
        return super.visitFormalParameter(ctx);
    }

    @Override
    public Void visitLocalVariableDeclaration(Java8Parser.LocalVariableDeclarationContext ctx) {
        String type = ctx.unannType().getText();
        for (Java8Parser.VariableDeclaratorContext vdCtx : ctx.variableDeclaratorList().variableDeclarator()) {
            String name = vdCtx.variableDeclaratorId().Identifier().getText();
            SymbolEntry entry = new SymbolEntry(name, type, getCurrentScopeName(), vdCtx.start.getLine());
            symbolTable.add(entry);
            scopes.peek().put(name, entry);
        }
        return super.visitLocalVariableDeclaration(ctx);
    }

    @Override
    public Void visitFieldDeclaration(Java8Parser.FieldDeclarationContext ctx) {
        String type = ctx.unannType().getText();
        for (Java8Parser.VariableDeclaratorContext vdCtx : ctx.variableDeclaratorList().variableDeclarator()) {
            String name = vdCtx.variableDeclaratorId().getText();
            SymbolEntry entry = new SymbolEntry(name, type, getCurrentScopeName(), ctx.start.getLine());
            symbolTable.add(entry);
            scopes.peek().put(name, entry);
        }
        return null;
    }

    @Override
    public Void visitForStatement(Java8Parser.ForStatementContext ctx) {
        Java8Parser.BasicForStatementContext basicFor = ctx.basicForStatement();
        if (basicFor != null) {
            // Visit forInit, expression, forUpdate, and statement via basicFor
            if (basicFor.forInit() != null) {
                visit(basicFor.forInit());
            }
            if (basicFor.expression() != null) {
                visit(basicFor.expression());
            }
            if (basicFor.forUpdate() != null) {
                visit(basicFor.forUpdate());
            }
            visit(basicFor.statement());
        }
        return null;
    }
}