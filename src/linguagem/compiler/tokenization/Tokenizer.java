package linguagem.compiler.tokenization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class Tokenizer {

    private String contents;
    private int index;

    public Tokenizer(String contents) {
        this.contents = contents;
        this.index = 0;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Optional<Character> c;

        while ((c = peek()).isPresent()) {

            if (c.get() == '\n') {
                tokens.add(new Token(TokenType.JUMP_LINE));
                index++;
            } else if (c.get() == '"') {
                tokens.add(new Token(TokenType.QUOTES));
                index++;
                addLiteralString(tokens);
            } else if (c.get() == ';') {
                tokens.add(new Token(TokenType.SEMI));
                index++;
            }
            else if (Character.isDigit(c.get())) {
                tokens.add(new Token(TokenType.INT_LIT, getNumber()));
            } else if (Character.isAlphabetic(c.get()) || c.get() == '_') {
                String s = getWord();
                tokens.add(new Token(TokenType.getTokenTypeByAlphanumeric(s), s));
            } else if (isIgnorableChar(c.get())) {
                jumpIgnorableChar();
            } else {
                String s = getNonAlphanumericalSequence();
                tokens.add(new Token(TokenType.getTokenTypeByNonAlphanumeric(s), s));
            }
        }

        return tokens;
    }

    private void addLiteralString(List<Token> tokens) {
        StringBuilder builder = new StringBuilder();
        Optional<Character> c;

        while ((c = peek()).isPresent() && c.get() != '"') {
            builder.append(contents.charAt(index++));
        }
        tokens.add(new Token(TokenType.STRING_LIT, builder.toString()));
        
        if (c.isPresent() && c.get() == '"') {
            tokens.add(new Token(TokenType.QUOTES));
            index++;
        }
    }

    private Optional<Character> peek() {
        return index < contents.length() ? Optional.ofNullable(contents.charAt(index)) : Optional.empty();
    }
    
    private String getWord() {
        StringBuilder builder = new StringBuilder();
        Optional<Character> c;

        while ((c = peek()).isPresent() && (Character.isAlphabetic(c.get()) || Character.isDigit(c.get())
                || c.get() == '_' || c.get() == '-')) {
            builder.append(contents.charAt(index++));
        }

        return builder.toString();
    }

    private String getNumber() {
        StringBuilder builder = new StringBuilder();
        Optional<Character> c;

        while ((c = peek()).isPresent() && Character.isDigit(c.get())) {
            builder.append(contents.charAt(index++));
        }

        return builder.toString();
    }
    
    private String getNonAlphanumericalSequence() {
        StringBuilder builder = new StringBuilder();
        Optional<Character> c;

        while ((c = peek()).isPresent() && !isIgnorableChar(c.get()) && !(Character.isDigit(c.get()) || Character.isAlphabetic(c.get()))) {
            builder.append(contents.charAt(index++));
        }

        return builder.toString();
    }

    private void jumpIgnorableChar() {
        Optional<Character> c;
        while ((c = peek()).isPresent() && isIgnorableChar(c.get()))
            index++;
    }

    private boolean isIgnorableChar(final char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' || ch == '"' || ch == ';';
    }

}
