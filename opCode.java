public enum opCode {
    LOAD_CONST,     // push literal to stack
    LOAD_VAR,       // push variable value to stack
    STORE_VAR,      // store top of stack to variable
    ADD, SUB, MUL, DIV,
    PRINT,
    JMP_IF_FALSE,   // conditional jump
    JMP,            // unconditional jump
    HALT
}
