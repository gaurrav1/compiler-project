package com.cdproject.service;

import com.cdproject.grammer.Java8BaseVisitor;
import com.cdproject.grammer.Java8Parser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ThreeAddressCodeVisitor extends Java8BaseVisitor<String> {
    @Getter
    private final List<String> code = new ArrayList<>();
    private int tempCount = 0;

    private String newTemp() {
        return "t" + tempCount++;
    }

    @Override
    public String visitAssignment(Java8Parser.AssignmentContext ctx) {
        String lhs = ctx.leftHandSide().getText();
        String rhs = visit(ctx.expression());
        if (rhs != null) {
            code.add(lhs + " = " + rhs);
        }
        return lhs;
    }

    @Override
    public String visitAdditiveExpression(Java8Parser.AdditiveExpressionContext ctx) {
        if (ctx.getChildCount() == 3) { // Format: expr + expr or expr - expr
            String left = visit(ctx.getChild(0)); // Left operand
            String right = visit(ctx.getChild(2)); // Right operand
            String operator = ctx.getChild(1).getText(); // + or -

            if (left == null || right == null) return null;

            String temp = newTemp();
            code.add(temp + " = " + left + " " + operator + " " + right);
            return temp;
        }
        return visitChildren(ctx);
    }

    @Override
    public String visitMultiplicativeExpression(Java8Parser.MultiplicativeExpressionContext ctx) {
        if (ctx.getChildCount() == 3) { // Format: expr * expr, expr / expr, or expr % expr
            String left = visit(ctx.getChild(0)); // Left operand
            String right = visit(ctx.getChild(2)); // Right operand
            String operator = ctx.getChild(1).getText(); // *, /, or %

            if (left == null || right == null) return null;

            String temp = newTemp();
            code.add(temp + " = " + left + " " + operator + " " + right);
            return temp;
        }
        return visitChildren(ctx);
    }

    private String processBinaryOperation(Java8Parser.ExpressionContext ctx, String operator) {
        if (ctx.getChildCount() == 3) {
            String left = visit(ctx.getChild(0));
            String right = visit(ctx.getChild(2));
            if (left == null || right == null) return null;

            String temp = newTemp();
            code.add(temp + " = " + left + " " + operator + " " + right);
            return temp;
        }
        return visitChildren(ctx);
    }

    @Override
    public String visitPrimary(Java8Parser.PrimaryContext ctx) {
        if (ctx.primaryNoNewArray_lfno_primary() != null) {
            return visit(ctx.primaryNoNewArray_lfno_primary());
        } else if (ctx.arrayCreationExpression() != null) {
            return visit(ctx.arrayCreationExpression());
        } else if (!ctx.primaryNoNewArray_lf_primary().isEmpty()) {
            // If it has a list of primary subcontexts, you can visit the first one (or all, if needed)
            return visit(ctx.primaryNoNewArray_lf_primary(0));
        }
        return super.visitPrimary(ctx);
    }
    @Override
    public String visitPrimaryNoNewArray_lfno_primary(Java8Parser.PrimaryNoNewArray_lfno_primaryContext ctx) {
        if (ctx.literal() != null) {
            return visit(ctx.literal());
        } else if (ctx.fieldAccess_lfno_primary() != null) {
            return visit(ctx.fieldAccess_lfno_primary());
        } else if (ctx.methodInvocation_lfno_primary() != null) {
            return visit(ctx.methodInvocation_lfno_primary());
        } else if (ctx.expression() != null) {
            return visit(ctx.expression());
        }
        return super.visitPrimaryNoNewArray_lfno_primary(ctx);
    }

    @Override
    public String visitLocalVariableDeclaration(Java8Parser.LocalVariableDeclarationContext ctx) {
        for (Java8Parser.VariableDeclaratorContext varCtx : ctx.variableDeclaratorList().variableDeclarator()) {
            String varName = varCtx.variableDeclaratorId().getText();
            if (varCtx.variableInitializer() != null) {
                Java8Parser.VariableInitializerContext initializerCtx = varCtx.variableInitializer();
                if (initializerCtx.expression() != null) {
                    String rhs = visit(initializerCtx.expression());
                    if (rhs != null) {
                        code.add(varName + " = " + rhs);
                    }
                }
            }
        }
        return null;
    }


    @Override
    public String visitVariableInitializer(Java8Parser.VariableInitializerContext ctx) {
        return visit(ctx.getChild(0)); // could be expression or array initializer
    }
    @Override
    public String visitMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        return visit(ctx.methodBody()); // Visit the method body
    }

    @Override
    public String visitMethodBody(Java8Parser.MethodBodyContext ctx) {
        return visit(ctx.block());
    }

    @Override
    public String visitBlock(Java8Parser.BlockContext ctx) {
        for (Java8Parser.BlockStatementContext stmt : ctx.blockStatements().blockStatement()) {
            visit(stmt);
        }
        return null;
    }

    @Override
    public String visitBlockStatement(Java8Parser.BlockStatementContext ctx) {
        return visitChildren(ctx); // Handles local variable declarations and statements
    }

    @Override
    public String visitStatement(Java8Parser.StatementContext ctx) {
        return visitChildren(ctx); // Traverse inner statements like expressions
    }

    @Override
    public String visitExpressionStatement(Java8Parser.ExpressionStatementContext ctx) {
        return visit(ctx.statementExpression());
    }




    @Override
    public String visitLiteral(Java8Parser.LiteralContext ctx) {
        return ctx.getText();
    }
}