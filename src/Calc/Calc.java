package Calc;

import java.util.*;
import java.util.stream.Collectors;

enum LexemaType {
    CONST, IDENT, LPAR, RPAR, PLS, MNS, MUL, DIV
}

class Lexema {
    Lexema(LexemaType t, String v, int pos) {
        this.type = t;
        this.value = v;
        this.pos = pos;
    }

    LexemaType getType() {
        return this.type;
    }

    public String getString() {
        return this.value;
    }

    int getPosition() {
        return this.pos;
    }

    private final LexemaType type;
    private final String value;
    private final int pos;
}

enum LexanState {
    SPACE, CONST, IDENT
}

class Lexan {
    Lexan(String expr) {
        this.expr = expr;
        this.tokenDictionary = new HashMap<Character, LexemaType>();
        tokenDictionary.put('+', LexemaType.PLS);
        tokenDictionary.put('-', LexemaType.MNS);
        tokenDictionary.put('*', LexemaType.MUL);
        tokenDictionary.put('/', LexemaType.DIV);
        tokenDictionary.put('(', LexemaType.LPAR);
        tokenDictionary.put(')', LexemaType.RPAR);
    }

    List<Lexema> analysis() throws Exception {
        List<Lexema> lexlst = new LinkedList<Lexema>();
        LexanState st = LexanState.SPACE;
        int from = 0, pos = 0;
        for (; pos < expr.length(); ++pos) {
            char ch = expr.charAt(pos);
            switch (st) {
                case SPACE:
                    if (' ' == ch)
                        continue;
                    if (isLetter(ch)) {
                        from = pos;
                        st = LexanState.IDENT;
                    } else if (isDigit(ch)) {
                        from = pos;
                        st = LexanState.CONST;
                    } else if (isToken(ch))
                        this.addToken(lexlst, expr.substring(pos, pos + 1), pos);
                    else
                        throw new Exception("Lexical error");
                    break;
                case CONST:
                    if (isDigit(ch))
                        continue;
                    this.addConst(lexlst, expr.substring(from, pos), pos);
                    st = LexanState.SPACE;
                    if (isToken(ch))
                        this.addToken(lexlst, expr.substring(pos, pos + 1), pos);
                    else if (' ' == ch)
                        continue;
                    else
                        throw new Exception("Lexical error");
                    break;
                case IDENT:
                    if (isDigit(ch) || isLetter(ch))
                        continue;
                    this.addIdent(lexlst, expr.substring(from, pos), pos);
                    st = LexanState.SPACE;
                    if (isToken(ch))
                        this.addToken(lexlst, expr.substring(pos, pos + 1), pos);
                    else if (' ' == ch)
                        continue;
                    else
                        throw new Exception("Lexical error");
                    break;
            }
        }
        if (LexanState.CONST == st)
            this.addConst(lexlst, expr.substring(from, pos), pos);
        else if (LexanState.IDENT == st)
            this.addIdent(lexlst, expr.substring(from, pos), pos);
        return lexlst;
    }

    private boolean isToken(char ch) {
        return this.tokenDictionary.keySet().contains(ch);
    }

    private boolean isDigit(char ch) {
        return 47 < ch && 58 > ch;
    }

    private boolean isLetter(char ch) {
        return (64 < ch && 91 > ch) || (96 < ch && 123 > ch);
    }

    private void addToken(List<Lexema> lst, String val, int pos) {
        lst.add(new Lexema(this.tokenDictionary.get(val.charAt(0)), val, pos));
    }

    private void addConst(List<Lexema> lst, String val, int pos) {
        lst.add(new Lexema(LexemaType.CONST, val, pos));
    }

    private void addIdent(List<Lexema> lst, String val, int pos) {
        lst.add(new Lexema(LexemaType.IDENT, val, pos));
    }

    private String expr;
    private Map<Character, LexemaType> tokenDictionary;
}

interface Operation {
    int result() throws Exception;
}

class Constant implements Operation {
    Constant(int i) {
        this.i = i;
    }

    @Override
    public int result() throws Exception {
        return i;
    }

    private int i;
}

class Variable implements Operation {
    Variable(String ident, Map<String, Integer> values) {
        this.ident = ident;
        this.values = values;
    }

    @Override
    public int result() throws Exception {
        if (!values.containsKey(ident))
            throw new Exception("Unknown variable " + ident);
        return values.get(ident);
    }

    private String ident;
    private Map<String, Integer> values;
}

class Negate implements Operation {
    Negate(Operation op) {
        this.op = op;
    }

    @Override
    public int result() throws Exception {
        return -1 * op.result();
    }

    private Operation op;
}

class Product implements Operation {
    Product(Operation lha, Operation rha) {
        this.lha = lha;
        this.rha = rha;
    }

    @Override
    public int result() throws Exception {
        return lha.result() * rha.result();
    }

    private Operation lha, rha;
}

class Quotient implements Operation {
    Quotient(Operation divident, Operation divisîr) {
        this.divident = divident;
        this.divisîr = divisîr;
    }

    @Override
    public int result() throws Exception {
        int nonZero = divisîr.result();
        if (0 == nonZero)
            throw new Exception("Division by zero");
        return divident.result() / divisîr.result();
    }

    private Operation divident, divisîr;
}

class Sum implements Operation {
    Sum(Operation lha, Operation rha) {
        this.lha = lha;
        this.rha = rha;
    }

    @Override
    public int result() throws Exception {
        return lha.result() + rha.result();
    }

    private Operation lha, rha;
}

class Difference implements Operation {
    Difference(Operation minuend, Operation subrtahend) {
        this.minuend = minuend;
        this.subrtahend = subrtahend;
    }

    @Override
    public int result() throws Exception {
        return minuend.result() - subrtahend.result();
    }

    private Operation minuend, subrtahend;
}

class Syntan {
    Syntan(List<Lexema> lexstream, Map<String, Integer> values) {
        this.lexstream = lexstream;
        this.values = values;
    }

    Operation analysis() throws Exception {
        ListIterator<Lexema> it = lexstream.listIterator();
        Operation op = this.expression(it);
        if (it.hasNext()) {
            Lexema lex = it.next();
            throw new Exception("ANALYSIS : expecting the end of the expression, but " + lex.getType() + " found at " + lex.getPosition());
        }
        return op;
    }

    private Operation expression(ListIterator<Lexema> it) throws Exception {
        if (!it.hasNext())
            throw new Exception("EXPRESSION : sudden end of the expression");
        return this.summator(it, this.coefficient(it));
    }

    private Operation coefficient(ListIterator<Lexema> it) throws Exception {
        return this.multiplicator(it, this.fundamental(it));
    }

    private Operation summator(ListIterator<Lexema> it, Operation op) throws Exception {
        if (!it.hasNext())
            return op;
        switch (it.next().getType()) {
            case PLS:
                return this.summator(it, new Sum(op, this.coefficient(it)));
            case MNS:
                return this.summator(it, new Difference(op, this.coefficient(it)));
            default:
                it.previous();
                return op;
        }
    }

    private Operation multiplicator(ListIterator<Lexema> it, Operation op) throws Exception {
        if (!it.hasNext())
            return op;
        switch (it.next().getType()) {
            case MUL:
                return this.multiplicator(it, new Product(op, this.fundamental(it)));
            case DIV:
                return this.multiplicator(it, new Quotient(op, this.fundamental(it)));
            default:
                it.previous();
                return op;
        }
    }

    private Operation fundamental(ListIterator<Lexema> it) throws Exception {
        if (!it.hasNext())
            throw new Exception("FUNDAMENTAL : sudden end of the expression");
        Lexema lex = it.next();
        switch (lex.getType()) {
            case CONST:
                return new Constant(Integer.parseInt(lex.getString()));
            case IDENT:
                return new Variable(lex.getString(), this.values);
            case MNS:
                return new Negate(this.fundamental(it));
            case LPAR:
                Operation op = this.expression(it);
                if (it.hasNext() && LexemaType.RPAR == it.next().getType())
                    return op;
                else
                    throw new Exception("FUNDAMENTAL : expecting `)` but " + lex.getType() + " found at " + lex.getPosition());
            default:
                throw new Exception("FUNDAMENTAL : expecting variable or constant but " + lex.getType() + " found at " + lex.getPosition());
        }
    }

    private List<Lexema> lexstream;
    private Map<String, Integer> values;
}

public class Calc {
    public static void main(String[] args) {
        try {
            Scanner sn = new Scanner(System.in);
            List<Lexema> lexemas = new Lexan(sn.nextLine()).analysis();
            Set<String> variables = lexemas.stream().filter(lex -> LexemaType.IDENT == lex.getType()).map(lex -> lex.getString()).collect(Collectors.toCollection(LinkedHashSet::new));
            Map<String, Integer> values = new HashMap<>();
            Operation res = new Syntan(lexemas, values).analysis();
            for (String s : variables)
                values.put(s, sn.nextInt());
            System.out.println(res.result());
        } catch (Exception e) {
            System.out.println("error");
        }
    }
}