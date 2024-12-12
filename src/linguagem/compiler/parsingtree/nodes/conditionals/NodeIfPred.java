package linguagem.compiler.parsingtree.nodes.conditionals;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;
import linguagem.compiler.parsingtree.nodes.NodeStatement;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmScope;
import linguagem.compiler.parsingtree.nodes.values.NodeBinCond;

public class NodeIfPred implements NodeStatement {

    private NodeBinCond nodeCond;
    private NodeSttmScope nodeScope;
    private NodeIfPred ifPred;

    public NodeIfPred(NodeBinCond nodeCond, NodeSttmScope nodeScope, NodeIfPred nodeIfPred) {
        super();
        this.nodeCond = nodeCond;
        this.nodeScope = nodeScope;
        this.ifPred = nodeIfPred;
    }

    public NodeBinCond getCond() {
        return nodeCond;
    }
    
    public void setCond(NodeBinCond nodeCond) {
        this.nodeCond = nodeCond;
    }

    public NodeSttmScope getScope() {
        return nodeScope;
    }

    public void setScope(NodeSttmScope nodeScope) {
        this.nodeScope = nodeScope;
    }

    public NodeIfPred getIfPred() {
        return ifPred;
    }

    public void setIfPred(NodeIfPred nodeIfPred) {
        this.ifPred = nodeIfPred;
    }

    @Override
    public void accept(CodeGeneratorVisitor codeGeneratorVisitor) {
        // Não precisa porque o NodeSttmIf já faz a chamada
    }

}
