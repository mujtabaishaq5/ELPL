import java.util.*;

class transpiler {
    private StringBuilder output = new StringBuilder();
    private Set<String> declaredVars = new HashSet<>();

    public String generate(ASTNode node) {
        output.append("public class OutputProgram {\n");
        output.append("  public static void main(String[] args) {\n");

        if (node instanceof Program) {
            for (ASTNode stmt : ((Program) node).statements) {
                generateStatement(stmt);
            }
        }

        output.append("  }\n");
        output.append("}\n");
        return output.toString();
    }

    private void generateStatement(ASTNode stmt) {
        if (stmt instanceof AssignmentNode) {
            AssignmentNode assign = (AssignmentNode) stmt;
            String var = assign.identifier;
            String expr = generateExpression(assign.expression);
            if (!declaredVars.contains(var)) {
                output.append("    int ").append(var).append(" = ").append(expr).append(";\n");
                declaredVars.add(var);
            } else {
                output.append("    ").append(var).append(" = ").append(expr).append(";\n");
            }
        } else if (stmt instanceof PrintNode) {
            PrintNode print = (PrintNode) stmt;
            if (print.isString) {
                output.append("    System.out.println(\"").append(print.message).append("\");\n");
            } else {
                output.append("    System.out.println(").append(print.message).append(");\n");
            }
        } else if (stmt instanceof IfNode) {
            IfNode ifNode = (IfNode) stmt;
            String condition = generateCondition(ifNode.condition);
            output.append("    if (").append(condition).append(") {\n");
            generateStatement(ifNode.thenStmt);
            output.append("    } else {\n");
            generateStatement(ifNode.elseStmt);
            output.append("    }\n");
        }
    }

    private String generateExpression(ExpressionNode expr) {
        if (expr instanceof NumberLiteral) {
            return Integer.toString(((NumberLiteral) expr).value);
        } else if (expr instanceof VariableRef) {
            return ((VariableRef) expr).name;
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr bin = (BinaryExpr) expr;
            String left = generateExpression(bin.left);
            String right = generateExpression(bin.right);
            return left + " " + mapOp(bin.op) + " " + right;
        }
        return "0";
    }

    private String generateCondition(Condition cond) {
        return cond.identifier + " " + cond.comparator + " " + generateExpression(cond.value);
    }

    private String mapOp(String op) {
        switch (op) {
            case "add": return "+";
            case "subtract": return "-";
            case "multiply": return "*";
            case "divide": return "/";
            default: return "?";
        }
    }
}
