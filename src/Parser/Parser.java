package Parser;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.IntPredicate;

class Position {
    private String text;
    private int index, line, col;

    Position(String text) {
        this(text, 0, 1, 1);
    }

    private Position(String text, int index, int line, int col) {
        this.text = text;
        this.index = index;
        this.line = line;
        this.col = col;
    }

    int getChar() {
        return index < text.length() ? text.codePointAt(index) : -1;
    }

    boolean satisfies(IntPredicate p) {
        return p.test(getChar());
    }

    Position skip() {
        int c = getChar();
        switch (c) {
            case -1:
                return this;
            case '\n':
                return new Position(text, index + 1, line + 1, 1);
            default:
                return new Position(text, index + (c > 0xFFFF ? 2 : 1), line, col + 1);
        }
    }

    Position skipWhile(IntPredicate p) {
        Position pos = this;
        while (pos.satisfies(p)) pos = pos.skip();
        return pos;
    }

    public String toString() {
        return String.format("(%d, %d)", line, col);
    }
}

class SyntaxError extends Exception {
    SyntaxError(Position pos, String msg) {
        super(String.format("Syntax error at %s: %s", pos.toString(), msg));
    }
}

enum Tag {
    IDENT,
    STRING,
    LPAREN,
    RPAREN,
    SEMICOLON,
    PLUS,
    END_OF_TEXT;

    public String toString() {
        switch (this) {
            case IDENT:
                return "identifier";
            case STRING:
                return "string";
            case LPAREN:
                return "'('";
            case RPAREN:
                return "')'";
            case SEMICOLON:
                return "':'";
            case PLUS:
                return "'+'";
            case END_OF_TEXT:
                return "end of text";
        }
        throw new RuntimeException("unreachable code");
    }
}

class Token {
    private Tag tag;
    private Position start, follow;

    Token(String text) throws SyntaxError {
        this(new Position(text));
    }

    private Token(Position cur) throws SyntaxError {
        start = cur.skipWhile(Character::isWhitespace);
        follow = start.skip();
        switch (start.getChar()) {
            case -1:
                tag = Tag.END_OF_TEXT;
                break;
            case '(':
                tag = Tag.LPAREN;
                break;
            case ')':
                tag = Tag.RPAREN;
                break;
            case ':':
                tag = Tag.SEMICOLON;
                break;
            case '+':
                tag = Tag.PLUS;
                break;
            case '"':
                follow = follow.skipWhile(c -> c != '"' && c != '\n');
                if (follow.getChar() != '"') {
                    throw new SyntaxError(follow, "newline in string literal");
                }
                follow = follow.skip();
                tag = Tag.STRING;
                break;

            default:
                if (start.satisfies(Character::isLetter)) {
                    follow = follow.skipWhile(Character::isLetterOrDigit);
                    tag = Tag.IDENT;
                } else {
                    throwError("invalid character");
                }
        }
    }

    void throwError(String msg) throws SyntaxError {
        throw new SyntaxError(start, msg);
    }

    boolean matches(Tag... tags) {
        return Arrays.stream(tags).anyMatch(t -> tag == t);
    }

    Token next() throws SyntaxError {
        return new Token(follow);
    }
}

class Parser {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        in.useDelimiter("\\Z");
        String text = in.next();
        try {
            sym = new Token(text);
            parse();
            System.out.println("success");
        } catch (SyntaxError e) {
            System.out.println(e.getMessage());
        }
    }

    //<Eq> ::= IDENT <List>
    private static void parse() throws SyntaxError {
        parseEq();
        expect(Tag.END_OF_TEXT);
    }

    //<Eq> ::= IDENT <List>
    private static void parseEq() throws SyntaxError {
        if (sym.matches(Tag.IDENT)) {
            System.out.println("<Eq> ::= IDENT <List>");
            sym = sym.next();
            parseList();
        } else {
            sym.throwError("identifier expected");
        }
    }

    //<List> ::= : <Term> <Tail> | ε
    private static void parseList() throws SyntaxError {
        if (sym.matches(Tag.SEMICOLON)) {
            System.out.println("<List> ::= : <Term> <Tail>");
            sym = sym.next();
            parseTerm();
            parseTail();
        } else {
            System.out.println("<List> ::= ε");
        }
    }

    //<Tail> ::= + <Term> <Tail> | ε
    private static void parseTail() throws SyntaxError {
        if (sym.matches(Tag.PLUS)) {
            System.out.println("<Tail> ::= + <Term>");
            sym = sym.next();
            parseTerm();
            parseTail();
        } else {
            System.out.println("<Tail> ::= ε");
        }
    }

    //<Term> ::= STRING | ( <Eq> )
    private static void parseTerm() throws SyntaxError {
        if (sym.matches(Tag.LPAREN)) {
            System.out.println("<Term> ::= ( <Eq> )");
            sym = sym.next();
            parseEq();
            expect(Tag.RPAREN);
        } else if (sym.matches(Tag.STRING)) {
            System.out.println("<Term> ::= STRING");
            sym = sym.next();
        } else {
            sym.throwError("string or '(' expected");
        }
    }

    private static void expect(Tag tag) throws SyntaxError {
        if (!sym.matches(tag)) {
            sym.throwError(tag.toString() + " expected");
        }
        sym = sym.next();
    }

    private static Token sym;
}