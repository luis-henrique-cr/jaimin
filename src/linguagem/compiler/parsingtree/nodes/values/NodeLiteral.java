package linguagem.compiler.parsingtree.nodes.values;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;

public class NodeLiteral implements NodeValue {
    
    private String value;
    private boolean isVar;

    public NodeLiteral(String value) {
        this.value = value;
        this.isVar = false;
    }
    
    public NodeLiteral(String value, boolean isVar) {
        this.value = value;
        this.isVar = isVar;
    }

    public String getValue() {
        return value;
    }
    
    public boolean isVar() {
        return isVar;
    }

    @Override
    public void accept(CodeGeneratorVisitor visitor) {
        visitor.genLiteral(this);
    }

}
