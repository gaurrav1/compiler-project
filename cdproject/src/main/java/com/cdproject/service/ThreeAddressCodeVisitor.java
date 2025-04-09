// ThreeAddressCodeVisitor.java
package com.cdproject.service;

import com.cdproject.grammer.Java8BaseVisitor;
import com.cdproject.grammer.Java8Parser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class ThreeAddressCodeVisitor extends Java8BaseVisitor<String> {
    private final List<String> code = new ArrayList<>();
    private int tempCount = 0;

    public List<String> getCode() {
        return code;
    }

    private String newTemp() {
        return "t" + tempCount++;
    }

    @Override
    public String visitAssignment(Java8Parser.AssignmentContext ctx) {
        String lhs = ctx.leftHandSide().getText();
        String rhs = visit(ctx.expression());
        code.add(lhs + " = " + rhs);
        return lhs;
    }

    @Override
    public String visitAdditiveExpression(Java8Parser.AdditiveExpressionContext ctx) {
        if (ctx.getChildCount() == 3) { // e.g., expr + expr
            String left = visit(ctx.getChild(0));
            String right = visit(ctx.getChild(2));
            String temp = newTemp();
            code.add(temp + " = " + left + " " + ctx.getChild(1).getText() + " " + right);
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
        }
        return super.visitPrimary(ctx);
    }


    @Override
    public String visitLiteral(Java8Parser.LiteralContext ctx) {
        return ctx.getText();
    }
}