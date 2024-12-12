package linguagem.compiler.parsingtree.nodes.statements;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;
import linguagem.compiler.parsingtree.nodes.NodeStatement;
import linguagem.compiler.parsingtree.nodes.conditionals.NodeIfPred;
import linguagem.compiler.parsingtree.nodes.values.NodeBinCond;

public class NodeSttmIf implements NodeStatement {

    private NodeBinCond nodeCond;
    private NodeSttmScope scope;
    private NodeIfPred ifPred;

    public NodeSttmIf(NodeBinCond nodeCond, NodeSttmScope scope, NodeIfPred ifPred) {
        this.nodeCond = nodeCond;
        this.scope = scope;
        this.ifPred = ifPred;
    }

    public NodeBinCond getNodeCond() {
        return nodeCond;
    }
    
    public void setExpr(NodeBinCond nodeCond) {
        this.nodeCond = nodeCond;
    }

    public NodeSttmScope getScope() {
        return scope;
    }

    public void setScope(NodeSttmScope scope) {
        this.scope = scope;
    }

    public NodeIfPred getIfPred() {
        return ifPred;
    }

    public void setIfPred(NodeIfPred ifPred) {
        this.ifPred = ifPred;
    }

    @Override
    public void accept(CodeGeneratorVisitor visitor) {
        visitor.genSttmIf(this);
    }

}
