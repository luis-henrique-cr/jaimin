package linguagem.compiler.parsingtree.nodes;

import java.util.ArrayList;
import java.util.List;

public class NodeMain {

    private final List<NodeStatement> statements = new ArrayList<>();

    public void addStatement(NodeStatement statement) {
        statements.add(statement);
    }

    public List<NodeStatement> getStatements() {
        return statements;
    }
    
}
