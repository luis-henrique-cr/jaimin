package linguagem.compiler.parsingtree.nodes.statements;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;
import linguagem.compiler.parsingtree.nodes.NodeStatement;

public class NodeSttmExit implements NodeStatement {

    @Override
    public void accept(CodeGeneratorVisitor visitor) {
        visitor.genSttExit(this);
    }

}
