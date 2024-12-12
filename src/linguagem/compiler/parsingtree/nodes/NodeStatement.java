package linguagem.compiler.parsingtree.nodes;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;

public interface NodeStatement {

    public void accept(CodeGeneratorVisitor codeGeneratorVisitor);
    
}
