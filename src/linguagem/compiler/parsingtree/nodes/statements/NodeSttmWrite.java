package linguagem.compiler.parsingtree.nodes.statements;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;
import linguagem.compiler.parsingtree.nodes.NodeStatement;
import linguagem.compiler.parsingtree.nodes.values.NodeLiteral;

public class NodeSttmWrite implements NodeStatement {

    private NodeLiteral literal;

    public NodeSttmWrite(NodeLiteral literal) {
        this.literal = literal;
    }

    public NodeLiteral getLiteral() {
        return literal;
    }
    
    @Override
    public void accept(CodeGeneratorVisitor visitor) {
        visitor.genSttWrite(this);
    }

}
