package com.ronsanzone.markdown.compiler;

import java.io.IOException;


/**
 * <p>The main class of the program. Used as a driver for the lexical, syntax, and semantic analyzers.</p>
 * <p/>
 * <p>Reads in a .mkd file written in the compiler language and compiles it to html code</p>
 *
 * @author Ronald Sanzone
 */
class Compiler
{

    public static final ParseTree tree = new ParseTree();
    private static final SyntaxAnalyzer syntax = new SyntaxAnalyzer();
    public static Tokens.TagObject nextToken;
    public static LexicalAnalyzer lexer;
    public static String htmlString = "";
    private static SemanticAnalyzer semant;

    public static void main(String[] args)
    {
        ArgumentParser argumentParser = new ArgumentParser().ParseArgument(args);
        String inFileName = argumentParser.getInFileName();
        String outFileName = argumentParser.getOutFileName();
        //read file into string
        try
        {
            String file_contents = FileManager.readFile(inFileName);
            //initialize lexer
            lexer = new LexicalAnalyzer(file_contents);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        lexer.lex();
        syntax.start();
        //initialize sematic analyzer
        semant = new SemanticAnalyzer(tree);
        semant.run();
        //write html file
        FileManager.writeFile(outFileName);
    }

    private static class ArgumentParser
    {
        private String[] args;
        private String inFileName;
        private String outFileName;


        public String getInFileName()
        {
            return inFileName;
        }

        public String getOutFileName()
        {
            return outFileName;
        }

        public ArgumentParser ParseArgument(String... args)
        {
            this.args = args;
            if (ValidFilenameArgument())
                ConvertFileNameToHtml();
            else
                PrintProperUse();
            return this;
        }

        private boolean ValidFilenameArgument()
        {
            return args.length == 1 && isMkdFileName(args[0]);
        }

        private void ConvertFileNameToHtml()
        {
            inFileName = args[0];
            outFileName = inFileName.replace(".mkd", ".html");
        }

        private static boolean isMkdFileName(String filename)
        {
            return filename.endsWith(".mkd");
        }

        private static void PrintProperUse()
        {
            System.out.println("Compiler Useage: java -jar Compiler <filemane.mkd>");
        }
    }
}
