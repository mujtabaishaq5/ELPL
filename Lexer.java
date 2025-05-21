import java.util.*;

class Lexer {
    private String input;
    private int pos = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            if (Character.isWhitespace(current())) {
                pos++;
                continue;
            }

            // Single-line comment: starts with /
            if (current() == '/') {
                skipSingleLineComment();
                continue;
            }

            // Multi-line comment: > ... <
            if (current() == '>') {
                skipMultiLineComment();
                continue;
            }

            // --- Language Keywords ---
            if (match("let")) tokens.add(new Token(TokenType.LET, "let"));
            else if (match("be")) tokens.add(new Token(TokenType.BE, "be"));
            else if (match("print")) tokens.add(new Token(TokenType.PRINT, "print"));
            else if (match("add")) tokens.add(new Token(TokenType.ADD, "add"));
            else if (match("subtract")) tokens.add(new Token(TokenType.SUBTRACT, "subtract"));
            else if (match("multiply")) tokens.add(new Token(TokenType.MULTIPLY, "multiply"));
            else if (match("divide")) tokens.add(new Token(TokenType.DIVIDE, "divide"));

            // Conditional logic
            else if (match("if")) tokens.add(new Token(TokenType.IF, "if"));
            else if (match("then")) tokens.add(new Token(TokenType.THEN, "then"));
            else if (match("otherwise")) tokens.add(new Token(TokenType.OTHERWISE, "otherwise"));
            else if (match("is equal to")) tokens.add(new Token(TokenType.IS_EQUAL_TO, "is equal to"));
            else if (match("is greater than")) tokens.add(new Token(TokenType.IS_GREATER_THAN, "is greater than"));
            else if (match("is less than")) tokens.add(new Token(TokenType.IS_LESS_THAN, "is less than"));

            // Loops
            else if (match("repeat")) tokens.add(new Token(TokenType.REPEAT, "repeat"));
            else if (match("times")) tokens.add(new Token(TokenType.TIMES, "times"));
            else if (match("while")) tokens.add(new Token(TokenType.WHILE, "while"));

            // Functions
            else if (match("function")) tokens.add(new Token(TokenType.FUNCTION, "function"));
            else if (match("call")) tokens.add(new Token(TokenType.CALL, "call"));

            // Blocks
            else if (current() == '{') {
                tokens.add(new Token(TokenType.LBRACE, "{"));
                pos++;
            } else if (current() == '}') {
                tokens.add(new Token(TokenType.RBRACE, "}"));
                pos++;
            }

            // String literals (e.g., "hello")
            else if (current() == '"') {
                tokens.add(new Token(TokenType.STRING, readString()));
            }

            // Numbers (e.g., 123)
            else if (Character.isDigit(current())) {
                tokens.add(new Token(TokenType.NUMBER, readNumber()));
            }

            // Identifiers (e.g., variable names)
            else if (Character.isLetter(current())) {
                tokens.add(new Token(TokenType.IDENTIFIER, readIdentifier()));
            }

            // Unknown character
            else {
                throw new RuntimeException("Unexpected character: " + current());
            }
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    // Match keyword and ensure it is followed by space or end of input
    private boolean match(String keyword) {
        int len = keyword.length();
        if (pos + len <= input.length() && input.substring(pos, pos + len).equals(keyword)) {
            if (pos + len == input.length() || Character.isWhitespace(input.charAt(pos + len))) {
                pos += len;
                return true;
            }
        }
        return false;
    }

    private char current() {
        return input.charAt(pos);
    }

    // Read string literal
    private String readString() {
        pos++; // Skip opening quote
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && input.charAt(pos) != '"') {
            sb.append(input.charAt(pos++));
        }
        pos++; // Skip closing quote
        return sb.toString();
    }

    // Read number literal
    private String readNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos++));
        }
        return sb.toString();
    }

    // Read identifier (variable/function name)
    private String readIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos++));
        }
        return sb.toString();
    }

    // Skip single-line comments
    private void skipSingleLineComment() {
        pos++; // Skip '/'
        while (pos < input.length() && input.charAt(pos) != '\n') {
            pos++;
        }
    }

    // Skip multi-line comments
    private void skipMultiLineComment() {
        pos++; // Skip '>'
        while (pos < input.length()) {
            if (input.charAt(pos) == '<') {
                pos++; // Skip closing '<'
                break;
            }
            pos++;
        }
    }
}
