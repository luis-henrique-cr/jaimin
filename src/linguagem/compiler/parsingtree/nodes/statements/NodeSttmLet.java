package linguagem.compiler.parsingtree.nodes.statements;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;
import linguagem.compiler.parsingtree.nodes.NodeStatement;
import linguagem.compiler.parsingtree.nodes.values.NodeValue;

public class NodeSttmLet implements NodeStatement {

    private String ident;
    private NodeValue value;

    public NodeSttmLet(String ident, NodeValue value) {
        this.ident = ident;
        this.value = value;
    }

    public NodeValue getNodeValue() {
        return value;
    }
    
    public String getIdentifier() {
        return ident;
    }

    @Override
    public void accept(CodeGeneratorVisitor visitor) {
        value.accept(visitor);
        visitor.genSttmLet(this);
    }

}
