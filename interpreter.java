import java.util.*;

public class interpreter {

    // Variable storage
    private final Map<String, Integer> variables = new HashMap<>();

    // Function definitions
    private final Map<String, List<ASTNode>> functions = new HashMap<>();

    // Entry point for interpreting any AST node
    public void interpret(ASTNode node) {
        try {
            if (node instanceof Program) {
                for (ASTNode stmt : ((Program) node).statements) {
                    interpret(stmt);
                }

            } else if (node instanceof AssignmentNode) {
                AssignmentNode assign = (AssignmentNode) node;
                int value = evaluate(assign.expression);
                variables.put(assign.identifier, value);

            } else if (node instanceof PrintNode) {
                PrintNode print = (PrintNode) node;
                for (ExpressionNode expr : print.expressions) {
                    if (expr instanceof StringLiteral) {
                        System.out.print(((StringLiteral) expr).value);
                    } else {
                        System.out.print(evaluate(expr));
                    }
                    System.out.print(" ");
                }
                System.out.println(); // Newline after print

            } else if (node instanceof IfNode) {
                IfNode ifNode = (IfNode) node;
                boolean result = evaluateCondition(ifNode.condition);
                interpret(result ? ifNode.thenStmt : ifNode.elseStmt);

            } else if (node instanceof RepeatNode) {
                RepeatNode repeat = (RepeatNode) node;
                for (int i = 0; i < repeat.times; i++) {
                    for (ASTNode stmt : repeat.body) {
                        interpret(stmt);
                    }
                }

            } else if (node instanceof WhileNode) {
                WhileNode whileNode = (WhileNode) node;
                while (evaluateCondition(whileNode.condition)) {
                    for (ASTNode stmt : whileNode.body) {
                        interpret(stmt);
                    }
                }

            } else if (node instanceof FunctionDeclNode) {
                FunctionDeclNode func = (FunctionDeclNode) node;
                functions.put(func.name, func.body); // Store function body

            } else if (node instanceof FunctionCallNode) {
                FunctionCallNode call = (FunctionCallNode) node;
                List<ASTNode> body = functions.get(call.name);
                if (body == null) {
                    throw new RuntimeException("Undefined function: " + call.name);
                }
                for (ASTNode stmt : body) {
                    interpret(stmt);
                }

            } else {
                throw new RuntimeException("Unsupported AST node: " + node.getClass().getSimpleName());
            }
        } catch (RuntimeException e) {
            System.err.println("Runtime Error: " + e.getMessage());
        }
    }

    // Evaluates expressions like numbers, variables, arithmetic
    private int evaluate(ExpressionNode expr) {
        if (expr instanceof NumberLiteral) {
            return ((NumberLiteral) expr).value;

        } else if (expr instanceof VariableRef) {
            String name = ((VariableRef) expr).name;
            if (!variables.containsKey(name)) {
                throw new RuntimeException("Undefined variable: " + name);
            }
            return variables.get(name);

        } else if (expr instanceof BinaryExpr) {
            BinaryExpr bin = (BinaryExpr) expr;
            int left = evaluate(bin.left);
            int right = evaluate(bin.right);
            switch (bin.op) {
                case "add": return left + right;
                case "subtract": return left - right;
                case "multiply": return left * right;
                case "divide":
                    if (right == 0) throw new RuntimeException("Division by zero");
                    return left / right;
                default:
                    throw new RuntimeException("Unsupported operator: " + bin.op);
            }

        } else if (expr instanceof StringLiteral) {
            throw new RuntimeException("Unexpected string literal in arithmetic expression");

        } else {
            throw new RuntimeException("Unknown expression node");
        }
    }

    // Evaluates conditions for if/while
    private boolean evaluateCondition(Condition cond) {
        int left = variables.getOrDefault(cond.identifier, 0);
        int right = evaluate(cond.value);
        switch (cond.comparator) {
            case "is equal to": return left == right;
            case "is greater than": return left > right;
            case "is less than": return left < right;
            default:
                throw new RuntimeException("Unknown comparator: " + cond.comparator);
        }
    }
}
