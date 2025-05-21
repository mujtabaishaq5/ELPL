class Instruction {
    opCode op;
    Object arg; // could be int, string, or address

    Instruction(opCode op, Object arg) {
        this.op = op;
        this.arg = arg;
    }
}
