package cosc.cosc455.rsanzone.project1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;



/**
 * <p>The main class of the program. Used as a driver for the lexical, syntax, and semantic analyzers.</p>
 * 
 * <p>Reads in a .mkd file written in the markdown language and compiles it to html code</p>
 *
 * @author Ronald Sanzone
 * @course COSC 455
 * @project 1
 * 
 */
public class Compiler {
	
	/** The next token that is found by the lexer. */
	public static Tokens.TagObject nextToken;
	
	/** The lexical analyzer. */
	public static LexicalAnalyzer lexer;
	
	/** The syntax analyzer. */
	public static SyntaxAnalyzer syntax = new SyntaxAnalyzer();
	
	/** The parse tree that the syntax analyzer generates */
	public static ParseTree tree = new ParseTree();
	
	/** The semantic analyzer. */
	public static SemanticAnalyzer semant;
	
	/** The html string that is written to a file. */
	public static String htmlString = new String();
	
	/**
	 * <p>The main method.</p>
	 * <p>Takes in a file name via command line argument. Reads the file to a string
	 * and sends it through the three analyzers. In the end a string of compiled html code
	 * is written to a file.</p>
	 * 
	 *
	 * @param args filename
	 */
	public static void main(String[] args) {
		String inFileName = new String();
		String outFileName = new String();
		//check to make sure there is only one arg
		if(args.length == 1){
			//test for .mkd
			if(isMkdFileName(args[0])){
				inFileName = args[0];
				//replace .mkd with .html
				outFileName = inFileName.replace(".mkd", ".html");
			}
			else{throw new WrongFileNameException();}
		}
		//show a basic useage message
		else{showUseage();}
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
	
	/**
	 * Checks if given file name ends in ".mkd".
	 *
	 * @param filename the filename
	 * @return true, if is filename ends in ".mkd"
	 */
	private static boolean isMkdFileName(String filename){
		return filename.endsWith(".mkd");
	}
	
	/**
	 * Shows useage a basic useage message if the command line arguments are incorrect.
	 */
	private static void showUseage(){
		System.out.println("Compiler Useage: java -jar Compiler <filemane.mkd>");
	}
	
	/**
	 * Writes the htmlString to a file.
	 *
	 * @param filename the filename
	 */
	private static void writeFile(String filename){
		try{
			FileWriter filestream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(filestream);
			
			out.write(htmlString);
			out.close();
		}catch(Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	/**
	 * Reads the given filename into a string.
	 *
	 * @param pathname the pathname
	 * @return the string containing file data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static String readFile(String pathname) throws IOException {
		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int)file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = "\n";
		
		try{
			while(scanner.hasNextLine()){
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {		
			scanner.close();
		}
		
	}
	
	/**
	 * The Class WrongFileNameException is used a an error generator if the given filename 
	 * does not end in the proper file extension.
	 */
	@SuppressWarnings("serial")
	public static class WrongFileNameException extends RuntimeException {
		
		/**
		 * Instantiates a new error message.
		 */
		public WrongFileNameException(){
			super("File Name Error: File must have .mkd extension");
		}
	}

}
