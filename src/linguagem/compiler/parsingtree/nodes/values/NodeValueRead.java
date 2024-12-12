package linguagem.compiler.parsingtree.nodes.values;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;

public class NodeValueRead implements NodeValue {

    private String text;

    public NodeValueRead(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
    @Override
    public void accept(CodeGeneratorVisitor visitor) {
        visitor.genSttRead(this);
    }

}
