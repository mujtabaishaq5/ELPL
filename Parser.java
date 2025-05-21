import java.util.*;

public class Parser {
    private List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Program parse() {
        List<ASTNode> statements = new ArrayList<>();
        while (!match(TokenType.EOF)) {
            statements.add(parseStatement());
        }
        return new Program(statements);
    }

    private ASTNode parseStatement() {
        if (match(TokenType.LET)) return parseAssignment();
        if (match(TokenType.PRINT)) return parsePrint();
        if (match(TokenType.IF)) return parseIf();
        if (match(TokenType.REPEAT)) return parseRepeat();
        if (match(TokenType.WHILE)) return parseWhile();
        if (match(TokenType.FUNCTION)) return parseFunction();
        if (match(TokenType.CALL)) return parseFunctionCall();
        throw new RuntimeException("Unknown statement at: " + peek().value);
    }

    private AssignmentNode parseAssignment() {
        String id = consume(TokenType.IDENTIFIER).value;
        consume(TokenType.BE);
        ExpressionNode expr = parseExpression();
        return new AssignmentNode(id, expr);
    }

    private PrintNode parsePrint() {
        List<ExpressionNode> parts = new ArrayList<>();
        while (!checkAny(
            TokenType.EOF, TokenType.LET, TokenType.IF, TokenType.PRINT,
            TokenType.OTHERWISE, TokenType.THEN, TokenType.RBRACE, TokenType.TIMES
        )) {
            if (match(TokenType.STRING)) {
                parts.add(new StringLiteral(previous().value));
            } else if (check(TokenType.NUMBER) || check(TokenType.IDENTIFIER)) {
                parts.add(parseExpression());
            } else {
                // Avoid parsing invalid token
                break;
            }
        }
        return new PrintNode(parts);
    }
    

    private IfNode parseIf() {
        Condition condition = parseCondition();
        consume(TokenType.THEN);
        ASTNode thenStmt = parseStatement();
        consume(TokenType.OTHERWISE);
        ASTNode elseStmt = parseStatement();
        return new IfNode(condition, thenStmt, elseStmt);
    }

    private RepeatNode parseRepeat() {
        ExpressionNode expr = parseExpression();
        if (!(expr instanceof NumberLiteral)) {
            throw new RuntimeException("Repeat count must be a number literal.");
        }
        int times = ((NumberLiteral) expr).value;
        consume(TokenType.TIMES);
        List<ASTNode> body = parseBlock();
        return new RepeatNode(times, body);
    }

    private WhileNode parseWhile() {
        Condition condition = parseCondition();
        List<ASTNode> body = parseBlock();
        return new WhileNode(condition, body);
    }

    private FunctionDeclNode parseFunction() {
        String name = consume(TokenType.IDENTIFIER).value;
        List<ASTNode> body = parseBlock();
        return new FunctionDeclNode(name, body);
    }

    private FunctionCallNode parseFunctionCall() {
        String name = consume(TokenType.IDENTIFIER).value;
        return new FunctionCallNode(name);
    }

    private List<ASTNode> parseBlock() {
        consume(TokenType.LBRACE);
        List<ASTNode> statements = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !check(TokenType.EOF)) {
            statements.add(parseStatement());
        }
        consume(TokenType.RBRACE);
        return statements;
    }

    private Condition parseCondition() {
        String id = consume(TokenType.IDENTIFIER).value;
        String comp;
        if (match(TokenType.IS_EQUAL_TO)) comp = "is equal to";
        else if (match(TokenType.IS_GREATER_THAN)) comp = "is greater than";
        else if (match(TokenType.IS_LESS_THAN)) comp = "is less than";
        else throw new RuntimeException("Expected comparator");
        ExpressionNode value = parseExpression();
        return new Condition(id, comp, value);
    }

    private ExpressionNode parseExpression() {
        ExpressionNode left = parseTerm();
        while (match(TokenType.ADD) || match(TokenType.SUBTRACT)) {
            String op = previous().type == TokenType.ADD ? "add" : "subtract";
            ExpressionNode right = parseTerm();
            left = new BinaryExpr(left, op, right);
        }
        return left;
    }

    private ExpressionNode parseTerm() {
        ExpressionNode left = parseFactor();
        while (match(TokenType.MULTIPLY) || match(TokenType.DIVIDE)) {
            String op = previous().type == TokenType.MULTIPLY ? "multiply" : "divide";
            ExpressionNode right = parseFactor();
            left = new BinaryExpr(left, op, right);
        }
        return left;
    }

    private ExpressionNode parseFactor() {
        if (match(TokenType.NUMBER)) {
            return new NumberLiteral(Integer.parseInt(previous().value));
        } else if (match(TokenType.IDENTIFIER)) {
            return new VariableRef(previous().value);
        } else {
            throw new RuntimeException("Expected number or identifier");
        }
    }

    // Utility methods
    private boolean match(TokenType type) {
        if (check(type)) {
            pos++;
            return true;
        }
        return false;
    }

    private Token consume(TokenType type) {
        if (check(type)) return tokens.get(pos++);
        throw new RuntimeException("Expected token: " + type);
    }

    private boolean check(TokenType type) {
        return pos < tokens.size() && tokens.get(pos).type == type;
    }

    private boolean checkAny(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) return true;
        }
        return false;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token previous() {
        return tokens.get(pos - 1);
    }
}
