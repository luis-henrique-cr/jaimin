package linguagem.compiler.tokenization;

public class Token {

    private TokenType type;
    private String value;
    
    public Token(TokenType type) {
        this.type = type;
        this.value = type.getText();
    }

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Type: %s | Value: %s", type, value);
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

}
