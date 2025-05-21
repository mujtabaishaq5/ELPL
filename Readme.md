# ELPL: English-Like Programming Language in Java

This project implements a simple, English-like programming language using Java. It walks through creating a **Lexer**, **Parser**, and **Interpreter** to process and execute human-readable code.

---

## ✨ Features

- Variable declaration using English syntax:

- Arithmetic operations using English verbs:

- Conditional statements using natural phrasing:

- Output using plain language:

---

## 📜 Grammar (BNF Syntax)

```bnf
<program> ::= <statement>*
<statement> ::= <assignment> | <print_stmt> | <if_stmt>
<assignment> ::= "let" <identifier> "be" <expression>
<print_stmt> ::= "print" <string> | "print" <identifier>
<if_stmt> ::= "if" <condition> "then" <statement> "otherwise" <statement>
<condition> ::= <identifier> <comparison> <expression>
<expression> ::= <term> (("add" | "subtract") <term>)*
<term> ::= <factor> (("multiply" | "divide") <factor>)*
<factor> ::= <number> | <identifier>
<comparison> ::= "is equal to" | "is greater than" | "is less than"
<identifier> ::= [a-zA-Z_][a-zA-Z0-9_]*
<number> ::= [0-9]+
<string> ::= "[^"]*"

# Architecture
## Lexer
- Tokenizes input string into types like **LET**, **PRINT**, **ADD**, **IDENTIFIER**, **NUMBER**, etc.

- Handles multi-word tokens like **is greater than**.

## Parser
- Parses tokens into an Abstract Syntax Tree (AST).

- Supports basic **arithmetic**, **assignments**, **print**, and **conditionals**.

## Interpreter
- Evaluates the AST by executing logic in **Java**.

- Stores and retrieves variable values.

Handles **if-then-otherwise** logic.

