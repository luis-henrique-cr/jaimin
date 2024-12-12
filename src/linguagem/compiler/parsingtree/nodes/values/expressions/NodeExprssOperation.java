package linguagem.compiler.parsingtree.nodes.values.expressions;

import linguagem.compiler.Operation;
import linguagem.compiler.code_generator.CodeGeneratorVisitor;
import linguagem.compiler.parsingtree.nodes.values.expressions.terms.NodeExprssTerm;

public class NodeExprssOperation implements NodeExpression {
    
    // TODO Revisitar para arrumar as expressões dentro de expressões
    //    private NodeExpression left;
    //    private NodeExpression right;
    //    private Operation operation;
    
    private NodeExprssTerm left;
    private NodeExprssTerm right;
    private Operation operation;
    
    public NodeExprssOperation(NodeExprssTerm left, NodeExprssTerm right, Operation operation) {
        super();
        this.left = left;
        this.right = right;
        this.operation = operation;
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

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
    
    public Operation getOperation() {
        return operation;
    }

    @Override
    public void accept(CodeGeneratorVisitor visitor) {
        visitor.genExprssOperation(this);
    }

}
