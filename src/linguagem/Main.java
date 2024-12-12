package linguagem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import linguagem.compiler.code_generator.CodeGeneratorVisitor;
import linguagem.compiler.parsingtree.Parser;
import linguagem.compiler.parsingtree.nodes.NodeMain;
import linguagem.compiler.tokenization.Token;
import linguagem.compiler.tokenization.Tokenizer;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Incorrect usage. Correct usage is...");
            System.err.println("jaimin <input.jm>");
            System.exit(1);
        }

        String contents;
        try {
            // Leitura do arquivo de entrada
            try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
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
            NodeMain mainNode = parser.parseStart();
            
            System.out.println("Sucesso?");

            if (mainNode.getStatements().isEmpty()) {
                System.err.println("Invalid program");
                System.exit(1);
            }

            // Geração de código
            CodeGeneratorVisitor generator = new CodeGeneratorVisitor(mainNode);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("out.sc"))) {
                writer.write(generator.genProg());
            }

//            // Execução de comandos do sistema
//            Process nasmProcess = Runtime.getRuntime().exec("nasm -felf64 out.asm");
//            nasmProcess.waitFor();
//            Process ldProcess = Runtime.getRuntime().exec("ld -o out out.o");
//            ldProcess.waitFor();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
