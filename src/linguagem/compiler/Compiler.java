package linguagem.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import linguagem.compiler.parsingtree.Parser;
import linguagem.compiler.parsingtree.nodes.NodeMain;
import linguagem.compiler.tokenization.Token;
import linguagem.compiler.tokenization.Tokenizer;

public class Compiler implements Runnable {
    
    private File mainFile;
    
    public Compiler(File mainFile) {
        this.mainFile = mainFile;
    }
    
    public void compile(File file) {
        String contents;
        try {
            // Leitura do arquivo de entrada
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder contentsBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    contentsBuilder.append(line).append("\n");
                }
                contents = contentsBuilder.toString();
            }

            // Tokenização
            Tokenizer tokenizer = new Tokenizer(contents);
            List<Token> tokens = tokenizer.tokenize();
            
//            tokens.forEach(System.out::println);

            // Parsing
            Parser parser = new Parser(tokens);
            NodeMain start = parser.parseStart();
//
//            if (start.getChildren().isEmpty()) {
//                System.err.println("Invalid program");
//                System.exit(1);
//            }

//            // Geração de código
//            Generator generator = new Generator(prog.get());
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter("out.asm"))) {
//                writer.write(generator.genProg());
//            }
//
//            // Execução de comandos do sistema
//            Process nasmProcess = Runtime.getRuntime().exec("nasm -felf64 out.asm");
//            nasmProcess.waitFor();
//            Process ldProcess = Runtime.getRuntime().exec("ld -o out out.o");
//            ldProcess.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
    
}
