package linguagem.compiler.code_generator;

import linguagem.compiler.parsingtree.nodes.NodeMain;
import linguagem.compiler.parsingtree.nodes.NodeStatement;
import linguagem.compiler.parsingtree.nodes.conditionals.NodeIfPred;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmExit;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmIdent;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmIf;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmLet;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmScope;
import linguagem.compiler.parsingtree.nodes.statements.NodeSttmWrite;
import linguagem.compiler.parsingtree.nodes.values.NodeBinCond;
import linguagem.compiler.parsingtree.nodes.values.NodeLiteral;
import linguagem.compiler.parsingtree.nodes.values.NodeValueRead;
import linguagem.compiler.parsingtree.nodes.values.expressions.NodeExprssOperation;
import linguagem.compiler.parsingtree.nodes.values.expressions.terms.NodeExprssTermIdentifier;
import linguagem.compiler.parsingtree.nodes.values.expressions.terms.NodeExprssTermNumber;

public class CodeGeneratorVisitor {
    
    private NodeMain mainNode;
    private int nLabel;
    private StringBuilder builder;

    public CodeGeneratorVisitor(NodeMain mainNode) {
        this.mainNode = mainNode;
        this.nLabel = 1;
    }

    public String genProg() {
        builder = new StringBuilder();
        builder.append("#\n");
        appendInstruction("ORG");
        
        for(NodeStatement statement : mainNode.getStatements()) {
            statement.accept(this);
        }
        
        appendInstruction("RETN");
        builder.append("#");
        return builder.toString();
    }
    
    public void cleanRegister() {
        appendInstruction("LOAD #0");
    }
    
    public void genScope(NodeSttmScope scope) {
        for(NodeStatement statement : scope.getStatements()) {
            statement.accept(this);
        }
    }
    
    public void genSttExit(NodeSttmExit node) {
        appendInstruction("RETN");
    }
    
    public void genSttWrite(NodeSttmWrite node) {
        if(node.getLiteral().isVar()) {
            appendInstruction(String.format("LOAD %s", node.getLiteral().getValue()));
            appendInstruction("WRTA ");
        }
        else
            appendInstruction(String.format("ECHO \"%s\"", node.getLiteral().getValue()));
    }
    
    public void genSttRead(NodeValueRead node) {
        appendInstruction(String.format("READ \"%s\"", node.getText()));
    }
    
    public void genSttmLet(NodeSttmLet node) {
        builder.append(String.format("%-7s DW%n", node.getIdentifier()));
        appendInstruction(String.format("STOD %s", node.getIdentifier()));
    }
    
    public void genSttmIdent(NodeSttmIdent node) {
        appendInstruction(String.format("STOD %s", node.getIdentifier()));
    }
    
    public void genLiteral(NodeLiteral node) {
        if(node.isVar())
            appendInstruction(String.format("LOAD %s", node.getValue()));
        else
            appendInstruction(String.format("LOAD #%s", node.getValue()));
    }
    
    public void genSttmIf(NodeSttmIf node) {
        String comeco = createLabel();
        String fim = createLabel();
        NodeBinCond cond = node.getNodeCond();
        switch (cond.getBinOperation()) {
        case LESS_THAN:
            appendInstruction(String.format("LOAD %s", cond.getLeft().getFormattedValue()));
            appendInstruction(String.format("SUBD %s", cond.getRight().getFormattedValue()));
            appendInstruction(String.format("JNEG %s", comeco));
            break;
        case GREATER_THAN:
            appendInstruction(String.format("LOAD %s", cond.getLeft().getFormattedValue()));
            appendInstruction(String.format("SUBD %s", cond.getRight().getFormattedValue()));
            appendInstruction(String.format("JPOS %s", comeco));
            break;
        case EQUALS_BOL:
            appendInstruction(String.format("LOAD %s", cond.getLeft().getFormattedValue()));
            appendInstruction(String.format("SUBD %s", cond.getRight().getFormattedValue()));
            appendInstruction(String.format("JZER %s", comeco));
            break;
        default:
            break;
        }
        
        if(node.getIfPred() != null)
            genSttmIfPred(node.getIfPred(), fim);
        else
            appendInstruction(String.format("JUMP %s", fim));
        appendLabel(comeco);
        genScope(node.getScope());
        appendLabel(fim);
    }
    
    public void genSttmIfPred(NodeIfPred node, String labelFim) {
        String label = createLabel();
        NodeBinCond cond = node.getCond();
        switch (cond.getBinOperation()) {
        case LESS_THAN:
            appendInstruction(String.format("LOAD %s", cond.getLeft().getFormattedValue()));
            appendInstruction(String.format("SUBD %s", cond.getRight().getFormattedValue()));
            appendInstruction(String.format("JNEG %s", label));
            break;
        case GREATER_THAN:
            appendInstruction(String.format("LOAD %s", cond.getLeft().getFormattedValue()));
            appendInstruction(String.format("SUBD %s", cond.getRight().getFormattedValue()));
            appendInstruction(String.format("JPOS %s", label));
            break;
        case EQUALS_BOL:
            appendInstruction(String.format("LOAD %s", cond.getLeft().getFormattedValue()));
            appendInstruction(String.format("SUBD %s", cond.getRight().getFormattedValue()));
            appendInstruction(String.format("JZER %s", label));
            break;
        default:
            break;
        }
        
        if(node.getIfPred() != null)
            genSttmIfPred(node.getIfPred(), labelFim);
        else
            appendInstruction(String.format("JUMP %s", labelFim));
        
        appendLabel(label);
        genScope(node.getScope());
        appendInstruction(String.format("JUMP %s", labelFim));
    }
    
    public void genExprssOperation(NodeExprssOperation node) {
        switch (node.getOperation()) {
        case PLUS:
            appendInstruction(String.format("LOAD %s", node.getLeft().getFormattedValue()));
            appendInstruction(String.format("ADDD %s", node.getRight().getFormattedValue()));
            break;
        case MULT:
            appendInstruction(String.format("LOAD %s", node.getLeft().getFormattedValue()));
            appendInstruction(String.format("MULT %s", node.getRight().getFormattedValue()));
            break;
        case MINUS:
            appendInstruction(String.format("LOAD %s", node.getLeft().getFormattedValue()));
            appendInstruction(String.format("SUBD %s", node.getRight().getFormattedValue()));
            break;
        case DIVISION:
            appendInstruction(String.format("LOAD %s", node.getLeft().getFormattedValue()));
            appendInstruction(String.format("IDIV %s", node.getRight().getFormattedValue()));
            break;
        default:
            break;
        }
    }
    
    public void genBinCond(NodeBinCond node) {
        switch (node.getBinOperation()) {
        case LESS_THAN:
            appendInstruction(String.format("LOAD %s", node.getLeft().getFormattedValue()));
            appendInstruction(String.format("SUBD %s", node.getRight().getFormattedValue()));
            break;
        case GREATER_THAN:
            appendInstruction(String.format("LOAD %s", node.getLeft().getFormattedValue()));
            appendInstruction(String.format("SUBD %s", node.getRight().getFormattedValue()));
            break;
        case EQUALS_BOL:
            appendInstruction(String.format("LOAD %s", node.getLeft().getFormattedValue()));
            appendInstruction(String.format("SUBD %s", node.getRight().getFormattedValue()));
            break;
        default:
            break;
        }
    }
    
    public void genExprssTermIdentifier(NodeExprssTermIdentifier node) {
        appendInstruction(String.format("LOAD %s", node.getFormattedValue()));
    }
    
    public void genExprssTermNumber(NodeExprssTermNumber node) {
        appendInstruction(String.format("LOAD %s", node.getFormattedValue()));
    }
    
    private void appendInstruction(String s) {
        builder.append("\t\t");
        builder.append(s);
        builder.append("\n");
    }
    
    private void appendLabel(String s) {
        builder.append(String.format("%-7s %n", s));
    }
    
    private String createLabel() {
        return "label" + nLabel++;
    }

}
