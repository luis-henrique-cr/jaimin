package linguagem.compiler.parsingtree.nodes.statements;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;
import linguagem.compiler.parsingtree.nodes.NodeStatement;
import linguagem.compiler.parsingtree.nodes.values.NodeValue;

public class NodeSttmIdent implements NodeStatement {

    private String ident;
    private NodeValue value;

    public NodeSttmIdent(String ident, NodeValue value) {
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
        visitor.genSttmIdent(this);
    }

}
