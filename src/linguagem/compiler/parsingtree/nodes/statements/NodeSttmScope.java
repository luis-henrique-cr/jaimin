package linguagem.compiler.parsingtree.nodes.statements;

import java.util.ArrayList;
import java.util.List;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;
import linguagem.compiler.parsingtree.nodes.NodeStatement;

public class NodeSttmScope implements NodeStatement {
    
    private final List<NodeStatement> statements = new ArrayList<>();

    public void addStatement(NodeStatement statement) {
        statements.add(statement);
    }
    
    public List<NodeStatement> getStatements() {
        return statements;
    }

    @Override
    public void accept(CodeGeneratorVisitor visitor) {
        visitor.genScope(this);
    }
    
}
