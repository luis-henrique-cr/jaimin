package linguagem.compiler.parsingtree;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import linguagem.compiler.BinOperation;
import linguagem.compiler.Operation;
import linguagem.compiler.parsingtree.nodes.NodeMain;
import linguagem.compiler.parsingtree.nodes.NodeStatement;
import linguagem.compiler.parsingtree.nodes.conditionals.NodeIfPred;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmExit;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmIdent;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmIf;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmLet;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmScope;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmWrite;
import linguagem.compiler.parsingtree.nodes.values.NodeBinCond;
import linguagem.compiler.parsingtree.nodes.values.NodeLiteral;
import linguagem.compiler.parsingtree.nodes.values.NodeValue;
import linguagem.compiler.parsingtree.nodes.values.NodeValueRead;
import linguagem.compiler.parsingtree.nodes.values.expressions.NodeExpression;
import linguagem.compiler.parsingtree.nodes.values.expressions.NodeExprssOperation;
import linguagem.compiler.parsingtree.nodes.values.expressions.terms.NodeExprssTerm;
import linguagem.compiler.parsingtree.nodes.values.expressions.terms.NodeExprssTermIdentifier;
import linguagem.compiler.parsingtree.nodes.values.expressions.terms.NodeExprssTermNumber;
import linguagem.compiler.tokenization.Token;
import linguagem.compiler.tokenization.TokenType;

public class Parser {

    private List<Token> tokens;
    private Set<String> variables;
    private int index;
    private int nLinha;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.variables = new HashSet<>();
        this.index = 0;
        this.nLinha = 1;
    }

    public NodeMain parseStart() {
        NodeMain startNode = new NodeMain();

        while (peek().isPresent()) {
            if(tryConsume(TokenType.JUMP_LINE).isPresent()) {
                nLinha++;
                continue;
            }
                
            startNode.addStatement(parseNextStatement());
        }

        return startNode;
    }
    
    private NodeStatement parseNextStatement() {
        Token t = peek().get();
        NodeStatement statement = null;
        
        if(t.getType() == TokenType.EXIT)
            statement = parseStatementExit().get();
        else if (t.getType() == TokenType.WRITE)
            statement = parseStatementWrite().get();
        else if(t.getType() == TokenType.LET)
            statement = parseStatementLet().get();
        else if(t.getType() == TokenType.IDENT)
            statement = parseStatementIdent().get();
        else if (t.getType() == TokenType.IF)
            return parseStatementIf().get();
        
        if(statement != null) {
            tryConsumeErr(TokenType.SEMI);
            return statement;
        }
        
        Optional<NodeSttmScope> optScope = parseScope();
        if(optScope.isPresent())
            return optScope.get();
        
        throw new IllegalArgumentException(String.format("Erro na linha %d | Não foi possível definir a instrução.", nLinha));
    }

    private Optional<NodeStatement> parseStatementExit() {
        tryConsumeErr(TokenType.EXIT);
        tryConsumeErr(TokenType.OPEN_PAREN);
        tryConsumeErr(TokenType.CLOSE_PAREN);
        return Optional.ofNullable(new NodeSttmExit());
    }
    
    private Optional<NodeSttmWrite> parseStatementWrite() {
        tryConsumeErr(TokenType.WRITE);
        tryConsumeErr(TokenType.OPEN_PAREN);
        
        Optional<NodeLiteral> optLiteral = parseLiteral();
        if(!optLiteral.isPresent())
            throw new IllegalArgumentException(String.format("Erro na linha %d | Erro no literal do parâmetro da função '%s'.", nLinha, TokenType.WRITE.getText()));
        
        tryConsumeErr(TokenType.CLOSE_PAREN);
        
        return Optional.ofNullable(new NodeSttmWrite(optLiteral.get()));
    }
    
    private Optional<NodeStatement> parseStatementLet() {
        tryConsumeErr(TokenType.LET);
        String ident = tryConsumeErr(TokenType.IDENT).getValue();
        tryConsumeErr(TokenType.EQUAL);
        
        if(variables.contains(ident))
            throw new IllegalArgumentException(String.format("Erro na linha %d | Variável '%s' já foi definida.", nLinha, ident));
        
        variables.add(ident);
        return Optional.ofNullable(new NodeSttmLet(ident, parseValue()));
    }
    
    private Optional<NodeStatement> parseStatementIdent() {
        String ident = tryConsumeErr(TokenType.IDENT).getValue();
        tryConsumeErr(TokenType.EQUAL);
        
        if(!variables.contains(ident))
            throw new IllegalArgumentException(String.format("Erro na linha %d | Variável '%s' não foi definida.", nLinha, ident));
        
        return Optional.ofNullable(new NodeSttmIdent(ident, parseValue()));
    }
    
    private Optional<NodeStatement> parseStatementIf() {
        tryConsumeErr(TokenType.IF);
        tryConsumeErr(TokenType.OPEN_PAREN);
        
        Optional<NodeExpression> optExpression = parseBinCond();
        if(!optExpression.isPresent())
            throw new IllegalArgumentException(String.format("Erro na linha %d | Erro na expressão do 'if'.", nLinha));
        
        tryConsumeErr(TokenType.CLOSE_PAREN);
        Optional<NodeSttmScope> scope = parseScope();
        if(!scope.isPresent())
            throw new IllegalArgumentException(String.format("Erro na linha %d | Um escopo deve ser definido.", nLinha));
        
        return Optional.ofNullable(new NodeSttmIf((NodeBinCond) optExpression.get(), scope.get(), parseStatementIfPred().orElse(null)));
    }
    
    private Optional<NodeIfPred> parseStatementIfPred() {
        if(!tryConsume(TokenType.ELIF).isPresent())
            return Optional.empty();
        tryConsumeErr(TokenType.OPEN_PAREN);
        
        Optional<NodeExpression> optExpression = parseBinCond();
        if(!optExpression.isPresent())
            throw new IllegalArgumentException(String.format("Erro na linha %d | Erro na expressão do 'elif'.", nLinha));
        
        tryConsumeErr(TokenType.CLOSE_PAREN);
        Optional<NodeSttmScope> scope = parseScope();
        if(!scope.isPresent())
            throw new IllegalArgumentException(String.format("Erro na linha %d | Um escopo deve ser definido.", nLinha));
        
        return Optional.ofNullable(new NodeIfPred((NodeBinCond) optExpression.get(), scope.get(), parseStatementIfPred().orElse(null)));
    }
    
    private Optional<NodeSttmScope> parseScope() {
        if(!tryConsume(TokenType.OPEN_CURLY).isPresent())
            return Optional.empty();

        NodeSttmScope scopeNode = new NodeSttmScope();
        while (peek().isPresent() && peek().get().getType() != TokenType.CLOSE_CURLY) {
            if(tryConsume(TokenType.JUMP_LINE).isPresent()) {
                nLinha++;
                continue;
            }
                
            scopeNode.addStatement(parseNextStatement());
        }
        
        tryConsumeErr(TokenType.CLOSE_CURLY);
        return scopeNode.getStatements().isEmpty() ? Optional.empty() : Optional.ofNullable(scopeNode);
    }
    
    private NodeValue parseValue() {
        Optional<NodeValueRead> read = parseRead();
        if(read.isPresent())
            return read.get();
        
        Optional<NodeExpression> cond = parseBinCond();
        if(cond.isPresent())
            return cond.get();
        
        Optional<NodeExpression> exprs = parseExpression();
        if(exprs.isPresent())
            return exprs.get();
        
        throw new IllegalArgumentException(String.format("Erro na linha %d | Valor inválido.", nLinha));
    }
    
    private Optional<NodeExpression> parseBinCond() {
        Optional<Token> t;
        if((t = peek(1)).isPresent() && !t.get().getType().isCondOperation()) {
            return Optional.empty();
        }
        
        Optional<NodeExpression> exprssLeft = parseExprssTerm();
        if(!exprssLeft.isPresent())
            throw new IllegalArgumentException(String.format("Erro na linha %d | Erro na expressão.", nLinha));
        
        BinOperation binOperation = BinOperation.getBy(consume().getType());
        
        Optional<NodeExpression> exprssRight = parseExprssTerm();
        if(!exprssRight.isPresent())
            throw new IllegalArgumentException("Erro na expressão.");
        
        return Optional.ofNullable(new NodeBinCond((NodeExprssTerm) exprssLeft.get(), (NodeExprssTerm) exprssRight.get(), binOperation));
    }

    private Optional<NodeValueRead> parseRead() {
        if(!tryConsume(TokenType.READ).isPresent())
            return Optional.empty();
        tryConsumeErr(TokenType.OPEN_PAREN);
        tryConsumeErr(TokenType.QUOTES, "A função aceita apenas String como parâmetro.");
        Token t = tryConsumeErr(TokenType.STRING_LIT);
        tryConsumeErr(TokenType.QUOTES, "A String parâmetro deve estar entre aspas.");
        tryConsumeErr(TokenType.CLOSE_PAREN);
        return Optional.ofNullable(new NodeValueRead(String.format("%s", t.getValue())));
    }
    
    // TODO Revisitar para arrumar expressões dentro de expressões
    private Optional<NodeExpression> parseExpression() {
        Optional<Token> t;
        return (t = peek(1)).isPresent() && t.get().getType().isMathOperation() ? 
                parseExprssOperation() : parseExprssTerm();
        
    }
    
    private Optional<NodeExpression> parseExprssTerm() {
        try {
            Optional<Token> token;
            if((token = tryConsume(TokenType.INT_LIT)).isPresent())
                return Optional.ofNullable(new NodeExprssTermNumber(NumberFormat.getInstance().parse(token.get().getValue())));
            
            if((token = tryConsume(TokenType.IDENT)).isPresent()) {
                if(!variables.contains(token.get().getValue()))
                    throw new IllegalArgumentException(String.format("Erro na linha %d | Variável '%s' não foi definida.", nLinha, token.get().getValue()));
                return Optional.ofNullable(new NodeExprssTermIdentifier(token.get().getValue()));
            }
            
            if (tryConsume(TokenType.QUOTES).isPresent())
                throw new IllegalArgumentException("Erro na linha %d | O Assembler não suporta texto em expressões.");
            
         // TODO Revisitar para arrumar expressões dentro de expressões
         //            
         //            if(!tryConsume(TokenType.OPEN_PAREN).isPresent())
         //                return Optional.empty();
         //            
         //            Optional<NodeExpression> exprss = parseExpression();
         //            
         //            if(!tryConsume(TokenType.CLOSE_PAREN).isPresent())
         //                return Optional.empty();
         //            
         //            if(exprss.isPresent())
         //                return Optional.ofNullable(exprss.get());
         //            
        } catch (ParseException e) {
            e.printStackTrace();
        }
            
        return Optional.empty();
    }

    private Optional<NodeExpression> parseExprssOperation() {
        Optional<NodeExpression> exprssLeft = parseExprssTerm();
        if(!exprssLeft.isPresent())
            throw new IllegalArgumentException(String.format("Erro na linha %d | Erro na expressão.", nLinha));
        
        Operation operation = Operation.getBy(consume().getType());
        // TODO Revisitar para arrumar expressões dentro de expressões
        //        Optional<NodeExpression> exprssRight = parseExpression();
        Optional<NodeExpression> exprssRight = parseExprssTerm();
        if(!exprssRight.isPresent())
            throw new IllegalArgumentException("Erro na expressão.");
        
        return Optional.ofNullable(new NodeExprssOperation((NodeExprssTerm) exprssLeft.get(), (NodeExprssTerm) exprssRight.get(), operation));
    }
    
    private Optional<NodeLiteral> parseLiteral() {
        if(tryConsume(TokenType.QUOTES).isPresent()) {
            String string = tryConsumeErr(TokenType.STRING_LIT, "Uma String deve ser definida entre as aspas.").getValue();
            tryConsumeErr(TokenType.QUOTES, "Você esqueceu de fechar as aspas.");
            return Optional.ofNullable(new NodeLiteral(String.format("%s", string)));
        }

        Optional<Token> t;
        if((t = tryConsume(TokenType.IDENT)).isPresent())
            return Optional.ofNullable(new NodeLiteral(t.get().getValue(), true));
        if((t = tryConsume(TokenType.INT_LIT)).isPresent())
            return Optional.ofNullable(new NodeLiteral(t.get().getValue()));
        
        throw new IllegalAccessError(String.format("Erro na linha %d | Primitivo não definido.", nLinha));
    }
    
    private Optional<Token> peek(int offset) {
        return (index + offset) < tokens.size() ? Optional.ofNullable(tokens.get(index + offset)) : Optional.empty();
    }

    private Optional<Token> peek() {
        return peek(0);
    }

    private Token consume() {
        return tokens.get(index++);
    }

    private Optional<Token> tryConsume(TokenType type) {
        Optional<Token> t;
        if ((t = peek()).isPresent() && t.get().getType() == type) {
            return Optional.of(consume());
        }
        return Optional.empty();
    }

    private Token tryConsumeErr(TokenType type) {
        return tryConsumeErr(type, "'%s' esperado mas não encontrado.");
    }
    
    private Token tryConsumeErr(TokenType type, String message) {
        Optional<Token> t;
        if ((t = peek()).isPresent() && t.get().getType() == type) {
            return consume();
        }
        message = "Erro na linha %d | " + message;
        throw new IllegalArgumentException(String.format(message, nLinha, type.getText()) );
    }

}