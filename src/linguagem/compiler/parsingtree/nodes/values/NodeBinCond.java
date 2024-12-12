package linguagem.compiler.parsingtree.nodes.values;

import linguagem.compiler.BinOperation;
import linguagem.compiler.code_generator.CodeGeneratorVisitor;
import linguagem.compiler.parsingtree.nodes.values.expressions.NodeExpression;
import linguagem.compiler.parsingtree.nodes.values.expressions.terms.NodeExprssTerm;

public class NodeBinCond implements NodeExpression {

    private NodeExprssTerm left;
    private NodeExprssTerm right;
    private BinOperation binOperation;
    
    public NodeBinCond(NodeExprssTerm left, NodeExprssTerm right, BinOperation binOperation) {
        this.left = left;
        this.right = right;
        this.binOperation = binOperation;
    }

    public NodeExprssTerm getLeft() {
        return left;
    }

    public void setLeft(NodeExprssTerm left) {
        this.left = left;
    }

    public NodeExprssTerm getRight() {
        return right;
    }

    public void setRight(NodeExprssTerm right) {
        this.right = right;
    }

    public BinOperation getBinOperation() {
        return binOperation;
    }

    public void setBinOperation(BinOperation binOperation) {
        this.binOperation = binOperation;
    }

    @Override
    public void accept(CodeGeneratorVisitor visitor) {
        visitor.genBinCond(this);
    }

}
