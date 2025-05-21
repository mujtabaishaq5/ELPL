import java.util.*;

class Compiler {
    List<Instruction> instructions = new ArrayList<>();
    Map<String, Integer> variables = new HashMap<>();

    public List<Instruction> compile(ASTNode node) {
        if (node instanceof Program) {
            for (ASTNode stmt : ((Program) node).statements) {
                compile(stmt);
            }
            instructions.add(new Instruction(opCode.HALT, null));
        } else if (node instanceof AssignmentNode) {
            AssignmentNode assign = (AssignmentNode) node;
            compile(assign.expression);
            instructions.add(new Instruction(opCode.STORE_VAR, assign.identifier));
        } else if (node instanceof PrintNode) {
            if (((PrintNode) node).isString) {
                instructions.add(new Instruction(opCode.LOAD_CONST, ((PrintNode) node).message));
            } else {
                instructions.add(new Instruction(opCode.LOAD_VAR, ((PrintNode) node).message));
            }
            instructions.add(new Instruction(opCode.PRINT, null));
        } else if (node instanceof BinaryExpr) {
            compile(((BinaryExpr) node).left);
            compile(((BinaryExpr) node).right);
            String op = ((BinaryExpr) node).op;
            if (op.equals("add")) instructions.add(new Instruction(opCode.ADD, null));
            else if (op.equals("subtract")) instructions.add(new Instruction(opCode.SUB, null));
            else if (op.equals("multiply")) instructions.add(new Instruction(opCode.MUL, null));
            else if (op.equals("divide")) instructions.add(new Instruction(opCode.DIV, null));
        }
        return instructions;
    }
}
