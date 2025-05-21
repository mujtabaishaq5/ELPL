import java.util.*;

// Base class for all AST nodes
public abstract class ASTNode {}

// Root of the program
class Program extends ASTNode {
    List<ASTNode> statements;
    Program(List<ASTNode> statements) {
        this.statements = statements;
    }
}

// let x be 5
class AssignmentNode extends ASTNode {
    String identifier;
    ExpressionNode expression;
    AssignmentNode(String identifier, ExpressionNode expression) {
        this.identifier = identifier;
        this.expression = expression;
    }
}

// print "Hello" x
class PrintNode extends ASTNode {
    List<ExpressionNode> expressions;
    PrintNode(List<ExpressionNode> expressions) {
        this.expressions = expressions;
    }
}

// "Hello world"
class StringLiteral extends ExpressionNode {
    String value;
    StringLiteral(String value) {
        this.value = value;
    }
}

// if x is greater than y then { ... } otherwise { ... }
class IfNode extends ASTNode {
    Condition condition;
    ASTNode thenStmt;
    ASTNode elseStmt;
    IfNode(Condition condition, ASTNode thenStmt, ASTNode elseStmt) {
        this.condition = condition;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }
}

// repeat 5 times { ... }
class RepeatNode extends ASTNode {
    int times;
    List<ASTNode> body;
    RepeatNode(int times, List<ASTNode> body) {
        this.times = times;
        this.body = body;
    }
}

// while x is less than 10 { ... }
class WhileNode extends ASTNode {
    Condition condition;
    List<ASTNode> body;
    WhileNode(Condition condition, List<ASTNode> body) {
        this.condition = condition;
        this.body = body;
    }
}

// function greet { print "hi" }
class FunctionDeclNode extends ASTNode {
    String name;
    List<ASTNode> body;
    FunctionDeclNode(String name, List<ASTNode> body) {
        this.name = name;
        this.body = body;
    }
}

// call greet
class FunctionCallNode extends ASTNode {
    String name;
    FunctionCallNode(String name) {
        this.name = name;
    }
}

// 5 + 3, x * 4
class BinaryExpr extends ExpressionNode {
    ExpressionNode left;
    String op;
    ExpressionNode right;
    BinaryExpr(ExpressionNode left, String op, ExpressionNode right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
}

// 42
class NumberLiteral extends ExpressionNode {
    int value;
    NumberLiteral(int value) {
        this.value = value;
    }
}

// x
class VariableRef extends ExpressionNode {
    String name;
    VariableRef(String name) {
        this.name = name;
    }
}

// x is greater than y
class Condition extends ASTNode {
    String identifier;
    String comparator;
    ExpressionNode value;
    Condition(String identifier, String comparator, ExpressionNode value) {
        this.identifier = identifier;
        this.comparator = comparator;
        this.value = value;
    }
}

// Expression base class
abstract class ExpressionNode extends ASTNode {}
