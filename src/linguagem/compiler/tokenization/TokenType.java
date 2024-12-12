package linguagem.compiler.tokenization;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import linguagem.compiler.BinOperation;
import linguagem.compiler.Operation;

public enum TokenType {

    EXIT("acaba"), 
    WRITE("late"), 
    READ("fala_cmg"), 
    IF("se"), 
    ELIF("se_tambem"), 
    ELSE("senao"), 
    LET("lembra"), 
    IDENT("identifier"),
    // TODO Expressões Condicionais
    //    OR("||"),
    //    AND("&&"),

    INT_LIT("inteiro literal"),
    STRING_LIT("string literal"),
    TRUE("favoravel"),
    FALSE("osso"),

    JUMP_LINE("\n"),
    QUOTES("\""),
    OPEN_PAREN("("), 
    CLOSE_PAREN(")"), 
    SEMI(";"), 
    EQUAL("="), 
    LESS_THAN(BinOperation.LESS_THAN.getSymbol()),
    GREATER_THAN(BinOperation.GREATER_THAN.getSymbol()), 
    EQUALS_BOL(BinOperation.EQUALS_BOL.getSymbol()), 
    PLUS(Operation.PLUS.getSymbol()), 
    STAR(Operation.MULT.getSymbol()), 
    MINUS(Operation.MINUS.getSymbol()), 
    FSLASH(Operation.DIVISION.getSymbol()),
    OPEN_CURLY("{"), 
    CLOSE_CURLY("}");
    
    static {
        try (BufferedReader reader = new BufferedReader(new FileReader("config.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] a = line.split("=");
                TokenType.valueOf(a[0]).setText(a[1].trim());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static final Set<TokenType> MATH_OPERATIONS = Set.of(PLUS, STAR, MINUS, FSLASH);
    private static final Set<TokenType> COND_OPERATIONS = Set.of(LESS_THAN, GREATER_THAN, EQUALS_BOL);
    private static final Set<TokenType> ALPHANUMERIC_TOKENS_TYPE = Set.of(EXIT, WRITE, READ, LET, IF, ELIF, ELSE, TRUE, FALSE);
    private static final Set<TokenType> NON_ALPHANUMERIC_TOKENS_TYPE = Set.of(
            QUOTES, OPEN_PAREN, CLOSE_PAREN, SEMI, EQUAL, LESS_THAN, GREATER_THAN,
            EQUALS_BOL, PLUS, STAR, MINUS, FSLASH, OPEN_CURLY, CLOSE_CURLY);

    private String text;

    private TokenType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
    public String setText(String text) {
        return this.text = text;
    }
    
    public boolean isMathOperation() {
        return MATH_OPERATIONS.contains(this);
    }
    
    public boolean isCondOperation() {
        return COND_OPERATIONS.contains(this);
    }
    
    public static TokenType getTokenTypeByAlphanumeric(String s) {
        return ALPHANUMERIC_TOKENS_TYPE
                .stream()
                .filter(tokenType -> tokenType.getText().equals(s))
                .findFirst()
                .orElse(IDENT);
    }
    
    public static TokenType getTokenTypeByNonAlphanumeric(String s) {
        return NON_ALPHANUMERIC_TOKENS_TYPE
                .stream()
                .filter(tokenType -> tokenType.getText().equals(s))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Token '%s' não identificado.", s)));
    }

}
