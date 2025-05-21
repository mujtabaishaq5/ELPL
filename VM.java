import java.util.*;

class VM {
    Stack<Object> stack = new Stack<>();
    Map<String, Object> memory = new HashMap<>();

    public void run(List<Instruction> code) {
        int ip = 0;
        while (ip < code.size()) {
            Instruction inst = code.get(ip);
            switch (inst.op) {
                case LOAD_CONST:
                    stack.push(inst.arg);
                    break;
                case LOAD_VAR:
                    stack.push(memory.get((String) inst.arg));
                    break;
                case STORE_VAR:
                    memory.put((String) inst.arg, stack.pop());
                    break;
                case ADD:
                    stack.push((int) stack.pop() + (int) stack.pop());
                    break;
                case SUB:
                    int b = (int) stack.pop(), a = (int) stack.pop();
                    stack.push(a - b);
                    break;
                case PRINT:
                    System.out.println(stack.pop());
                    break;
                case HALT:
                    return;
                default:
                     System.out.println("Error: please check with code or contact me at smmisbbb@gmail.com");
                    break;
            }
            ip++;
        }
    }
}
