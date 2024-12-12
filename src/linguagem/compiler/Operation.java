package linguagem.compiler;

import java.util.stream.Stream;

import linguagem.compiler.tokenization.TokenType;

public enum Operation {
    
    PLUS("+"), 
    MULT("*"), 
    MINUS("-"), 
    DIVISION("/");
    
    private String symbol;

    private Operation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
    
    public static Operation getBy(TokenType tokenType) throws IllegalArgumentException {
        return Stream.of(values())
            .filter(operation -> operation.getSymbol().equals(tokenType.getText()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("'%s' não é uma operação válida.", tokenType.getText())));
    }

}
