package linguagem.compiler.parsingtree.nodes.values.expressions.terms;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;

public class NodeExprssTermIdentifier implements NodeExprssTerm {
    
    private String identifier;

    public NodeExprssTermIdentifier(String identifier) {
        super();
        this.identifier = identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    @Override
    public String getFormattedValue() {
        return identifier;
    }
    
    @Override
    public void accept(CodeGeneratorVisitor visitor) {
        visitor.genExprssTermIdentifier(this);
    }

}
