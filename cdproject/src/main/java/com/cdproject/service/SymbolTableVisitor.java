package com.cdproject.service;

import com.cdproject.grammer.Java8BaseVisitor;
import com.cdproject.grammer.Java8Parser;
import com.cdproject.model.SymbolEntry;
import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;

@Getter
public class SymbolTableVisitor extends Java8BaseVisitor<Void> {

//    private final Map<String, String> symbolTable = new HashMap<>();
//    private final Map<String, String> globalSymbols = new HashMap<>();
//    private final List<Map<String, String>> scopes = new ArrayList<>();
//
//    public SymbolTableVisitor() {
//        scopes.add(new HashMap<>()); // Global scope
//    }
//
//    @Override
//    public Void visitVariableDeclarator(Java8Parser.VariableDeclaratorContext ctx) {
//        String variableName = ctx.variableDeclaratorId().getText();
//        String variableType = ctx.getParent().getParent().getChild(0).getText(); // Extract type from parent context
//
//        symbolTable.put(variableName, variableType);
//        currentScope().put(variableName, variableType);
//        return null;
//    }
//
//    @Override
//    public Void visitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
//        String className = ctx.normalClassDeclaration().Identifier().getText();
//        globalSymbols.put(className, "Class");
//        enterScope();
//        return super.visitClassDeclaration(ctx);
//    }
//
//    @Override
//    public Void visitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
//        String methodName = ctx.methodHeader().methodDeclarator().Identifier().getText();
//        currentScope().put(methodName, "Method");
//        enterScope();
//        visitChildren(ctx);
//        exitScope();
//        return null;
//    }
//
//    public Map<String, String> getCombinedSymbols() {
//        Map<String, String> combinedSymbols = new HashMap<>(globalSymbols);
//        for (Map<String, String> scope : scopes) {
//            combinedSymbols.putAll(scope);
//        }
//        return combinedSymbols;
//    }
//
//
//    private void enterScope() {
//        scopes.add(new HashMap<>());
//    }
//
//    private void exitScope() {
//        if (scopes.size() > 1) scopes.remove(scopes.size()-1);
//    }
//
//    private Map<String, String> currentScope() {
//        return scopes.get(scopes.size()-1);
//    }
private final List<SymbolEntry> symbolTable = new ArrayList<>();
    private final Stack<Map<String, SymbolEntry>> scopes = new Stack<>();

    public SymbolTableVisitor() {
        scopes.push(new HashMap<>()); // Global scope
    }

    @Override
    public Void visitVariableDeclarator(Java8Parser.VariableDeclaratorContext ctx) {
        String name = ctx.variableDeclaratorId().Identifier().getText();
        String type = ctx.getParent().getParent().getChild(0).getText();
        SymbolEntry entry = new SymbolEntry(name, type, "Global", ctx.start.getLine());
        symbolTable.add(entry);
        scopes.peek().put(name, entry);
        return super.visitVariableDeclarator(ctx);
    }

}