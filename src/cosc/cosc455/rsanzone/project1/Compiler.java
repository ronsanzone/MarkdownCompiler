package cosc.cosc455.rsanzone.project1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


/**
 * <p>The main class of the program. Used as a driver for the lexical, syntax, and semantic analyzers.</p>
 * <p/>
 * <p>Reads in a .mkd file written in the markdown language and compiles it to html code</p>
 *
 * @author Ronald Sanzone
 */
class Compiler {

    public static Tokens.TagObject nextToken;

    public static LexicalAnalyzer lexer;

    private static final SyntaxAnalyzer syntax = new SyntaxAnalyzer();

    public static final ParseTree tree = new ParseTree();

    private static SemanticAnalyzer semant;

    public static String htmlString = "";


    public static void main(String[] args) {
        String inFileName = "";
        String outFileName = "";
        //check to make sure there is only one arg
        if (args.length == 1) {
            //test for .mkd
            if (isMkdFileName(args[0])) {
                inFileName = args[0];
                //replace .mkd with .html
                outFileName = inFileName.replace(".mkd", ".html");
            } else {
                throw new WrongFileNameException();
            }
        }
        //show a basic useage message
        else {
            showUseage();
        }
        //read file into string
        try {
            String file_contents = readFile(inFileName);
            //initialize lexer
            lexer = new LexicalAnalyzer(file_contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lexer.lex();
        syntax.start();
        //initialize sematic analyzer
        semant = new SemanticAnalyzer(tree);
        semant.run();
        //write html file
        writeFile(outFileName);
    }

    private static boolean isMkdFileName(String filename) {
        return filename.endsWith(".mkd");
    }

    private static void showUseage() {
        System.out.println("Compiler Useage: java -jar Compiler <filemane.mkd>");
    }

    private static void writeFile(String filename) {
        try {
            FileWriter filestream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(filestream);

            out.write(htmlString);
            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String readFile(String pathname) throws IOException {
        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = "\n";

        try {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    @SuppressWarnings("serial")
    public static class WrongFileNameException extends RuntimeException {

        public WrongFileNameException() {
            super("File Name Error: File must have .mkd extension");
        }
    }

}
