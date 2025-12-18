package com.pascal;

import com.pascal.ast.*;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    private final Map<String, Double> variables;

    public Interpreter() {
        this.variables = new HashMap<>();
    }

    public Map<String, Double> getVariables() {
        return new HashMap<>(variables);
    }

    public void interpret(Program program) {
        visit(program);
    }

    private void visit(ASTNode node) {
        if (node instanceof Program) {
            visitProgram((Program) node);
        } else if (node instanceof ComplexStatement) {
            visitComplexStatement((ComplexStatement) node);
        } else if (node instanceof Assignment) {
            visitAssignment((Assignment) node);
        } else if (node instanceof NoOp) {
            visitNoOp((NoOp) node);
        } else {
            throw new RuntimeException("Unknown node type: " + node.getClass().getName());
        }
    }

    private void visitProgram(Program program) {
        visit(program.getComplexStatement());
    }

    private void visitComplexStatement(ComplexStatement complexStatement) {
        for (ASTNode statement : complexStatement.getStatements()) {
            visit(statement);
        }
    }

    private void visitAssignment(Assignment assignment) {
        String varName = assignment.getVariable().getName();
        double value = evaluate(assignment.getExpression());
        variables.put(varName, value);
    }

    private void visitNoOp(NoOp noOp) {
    }

    private double evaluate(ASTNode node) {
        if (node instanceof Num) {
            return evaluateNum((Num) node);
        } else if (node instanceof Var) {
            return evaluateVar((Var) node);
        } else if (node instanceof BinOp) {
            return evaluateBinOp((BinOp) node);
        } else if (node instanceof UnaryOp) {
            return evaluateUnaryOp((UnaryOp) node);
        } else {
            throw new RuntimeException("Unknown expression node type: " + node.getClass().getName());
        }
    }

    private double evaluateNum(Num num) {
        return num.getValue();
    }

    private double evaluateVar(Var var) {
        String varName = var.getName();
        if (!variables.containsKey(varName)) {
            throw new RuntimeException("Variable not found: " + varName);
        }
        return variables.get(varName);
    }

    private double evaluateBinOp(BinOp binOp) {
        double left = evaluate(binOp.getLeft());
        double right = evaluate(binOp.getRight());

        switch (binOp.getOperator().getType()) {
            case PLUS:
                return left + right;
            case MINUS:
                return left - right;
            case MUL:
                return left * right;
            case DIV:
                return left / right;
            default:
                throw new RuntimeException("Unknown operator: " + binOp.getOperator().getType());
        }
    }

    private double evaluateUnaryOp(UnaryOp unaryOp) {
        double operand = evaluate(unaryOp.getOperand());

        switch (unaryOp.getOperator().getType()) {
            case PLUS:
                return operand;
            case MINUS:
                return -operand;
            default:
                throw new RuntimeException("Unknown unary operator: " + unaryOp.getOperator().getType());
        }
    }
}
