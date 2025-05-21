import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;



public class Main {
    public static void main(String[] args) throws Exception {
        String code = Files.readString(Path.of("test.elpl"));
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        ASTNode program = parser.parse();

        interpreter interpreter = new interpreter();
        interpreter.interpret(program);

       /*  Compiler compiler = new Compiler();
        List<Instruction> bytecode = compiler.compile(ast);

        VM vm = new VM();
        vm.run(bytecode);*/
    }
}

/* if you want transpilation into the host language code below
public class Main {
    public static void main(String[] args) throws Exception {
        String code = Files.readString(Path.of("program.elpl"));
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        ASTNode program = parser.parseProgram();

        if (args.length > 0 && args[0].equals("--compile")) {
            transpiler trans = new transpiler();
            String javaCode = trans.generate(program);
            Files.writeString(Path.of("OutputProgram.java"), javaCode);
            System.out.println("Generated OutputProgram.java");
        } else {
            interpreter interpreter = new interpreter();
            interpreter.interpret(program);
        }
    }
}
public class Main {
    public static void main(String[] args) throws Exception {
        String code = Files.readString(Path.of("program.elpl"));
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        ASTNode program = parser.parseProgram();

        if (args.length > 0 && args[0].equals("--compile")) {
            CodeGenerator gen = new CodeGenerator();
            String javaCode = gen.generate(program);
            Files.writeString(Path.of("OutputProgram.java"), javaCode);
            System.out.println("Generated OutputProgram.java");
        } else {
            Interpreter interpreter = new Interpreter();
            interpreter.interpret(program);
        }
    }
}

 * 
 * 
 * 
 */