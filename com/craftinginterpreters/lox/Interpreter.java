package com.craftinginterpreters.lox;

import java.util.List;

// import static com.craftinginterpreters.lox.TokenType.BANG_EQUAL;
// import static com.craftinginterpreters.lox.TokenType.EQUAL_EQUAL;
// import static com.craftinginterpreters.lox.TokenType.GREATER;
// import static com.craftinginterpreters.lox.TokenType.GREATER_EQUAL;
// import static com.craftinginterpreters.lox.TokenType.LESS;
// import static com.craftinginterpreters.lox.TokenType.LESS_EQUAL;
// import static com.craftinginterpreters.lox.TokenType.PLUS;
// import static com.craftinginterpreters.lox.TokenType.STAR;

import javax.management.RuntimeErrorException;

class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    private Environment environment = new Environment();


    
    @Override
    public Void visitClassStmt(Stmt.Class stmt) {
        // Implement the logic for visitClassStmt
        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        // Implement the logic for visitReturnStmt
        return null;
    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        // Implement the logic for visitIfStmt
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;
        if(stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
        // Implement the logic for visitWhileStmt
        return null;
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        // Implement the logic for visitExpressionStmt
        return null;
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt) {
        // Implement the logic for visitFunctionStmt
        return null;
    }

    @Override
    public Object visitSuperExpr(Expr.Super expr) {
        // Implement the logic for visitSuperExpr
        return null;
    }

    @Override
    public Object visitThisExpr(Expr.This expr) {
        // Implement the logic for visitThisExpr
        return null;
    }

    // Removed duplicate visitVariableExpr method

    @Override
    public Object visitGetExpr(Expr.Get expr) {
        // Implement the logic for visitGetExpr
        return null;
    }

    @Override
    public Object visitCallExpr(Expr.Call expr) {
        // Implement the logic for visitCallExpr
        return null;
    }

    @Override
    public Object visitSetExpr(Expr.Set expr) {
        // Implement the logic for visitSetExpr
        return null;
    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name, value);
        return value;
    }

    @Override
    public Object visitLogicalExpr(Expr.Logical expr) {
        // Implement the logic for visitLogicalExpr
        return null;
    }

    void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double) right;
        }

        return null;
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return  environment.get(expr.name);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double)
            return;
        throw new RuntimeError(operator, "Operand nust be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double)
            return;

        throw new RuntimeError(operator, "Operands must a numbers.");
    }

    private boolean isTruthy(Object object) {
        if (object == null)
            return false;
        if (object instanceof Boolean)
            return (boolean) object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null)
            return true;
        if (a == null)
            return false;

        return a.equals(b);
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;

        try {
            this.environment = environment;
            for(Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);
        switch (expr.operator.type) {
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left <= (double) right;
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return isEqual(left, right);
            case MINUS:
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }
                if (left instanceof String || right instanceof String) {
                    return left.toString() + right.toString();
                }
                throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings");
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                if ((double) right == 0) {
                    throw new RuntimeError(expr.operator, "Divsion by 0.");
                }
                return (double) left / (double) right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double) left * (double) right;
        }

        return null;
    }

    private String stringify(Object object) {
        if (object == null)
            return "nill";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }
}



