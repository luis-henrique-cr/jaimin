package linguagem.compiler.parsingtree.nodes.values.expressions.terms;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;

public class NodeExprssTermNumber implements NodeExprssTerm {
    
    private Number n;

    public NodeExprssTermNumber(Number n) {
        super();
        this.n = n;
    }

    public void setN(Number n) {
        this.n = n;
    }
    
    @Override
    public String getFormattedValue() {
        return String.format("#%s", n.toString());
    }

    @Override
    public void accept(CodeGeneratorVisitor visitor) {
        visitor.genExprssTermNumber(this);
    }

}
