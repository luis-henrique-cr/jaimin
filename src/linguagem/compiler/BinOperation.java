package linguagem.compiler;

import java.util.stream.Stream;

import linguagem.compiler.tokenization.TokenType;

public enum BinOperation {
    
    LESS_THAN("<"), 
    GREATER_THAN(">"), 
    EQUALS_BOL("==");
    
    private String symbol;

    private BinOperation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
    
    public static BinOperation getBy(TokenType tokenType) throws IllegalArgumentException {
        return Stream.of(values())
            .filter(operation -> operation.getSymbol().equals(tokenType.getText()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("'%s' não é uma operação válida.", tokenType.getText())));
    }

}
