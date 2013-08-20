package cosc.cosc455.rsanzone.project1;


import java.util.HashMap;

import cosc.cosc455.rsanzone.project1.Tokens.TagObject;
import cosc.cosc455.rsanzone.project1.Tokens.TokenType;



/**
 * The Class LexicalAnalyzer takes file data in the form of a string and breaks it up into valid tokens. 
 * 
 * @author Ronald Sanzone
 * @course COSC 455
 * @project 1
 */
public class LexicalAnalyzer {
	
	/** Character constant used for the to represent a space */
	private final char SPACE = ' ';
	
	/** Char constant that represents a line separator. Use '\n' because the JVM will convert it to the
	 * appropriate system line separator. */
	private final char LINE_SEPARATOR = '\n';
	
	/** The current position in the given file data string. */
	private int curPos = 0;
	
	/** The current line number. */
	private int lineNum = 1;
	
	/** The String containing the data from the input file. */
	private String file = new String();
	
	/** Token class that is used to store token data */
	private Tokens tokens = new Tokens();
	
	/**
	 * Instantiates a new lexical analyzer and sets the file contents as the passed string.
	 *
	 * @param filecontents the string containing the file data
	 */
	public LexicalAnalyzer(String filecontents){
		this.file = filecontents;
	}
	
	/**
	 * <p>Lex method that moves the lexical analyzer forward.</p>
	 * <p>Gets the next token and sets it to the global compiler variable.</p>
	 */
	public void lex(){
		//get next token
		TagObject token;
		token = getToken();
		//set to global variable
		Compiler.nextToken = token;
	}

	/**
	 * Gets the next token from the file. The token is either a tag or plaintext. Uses the lookup() method
	 * to test tags, otherwise just returns the plaintext token.
	 *
	 * @return the token
	 */
	public TagObject getToken(){
		char c = getChar();
		//if c is the beginning of a tag
		if(c == '#'){
			String tag = processTag();
			if(lookup(tag) != null){
				//look up tag in the table of known tags
				return lookup(tag);
			}
		}
		//else c is plain text
		else{
			String text = processText();
			//remove unneeded whitespace
			if(isWhiteSpace(text) && charRemaining())
				return skipWhiteSpace();
			else
				//return new TagObject containg plaintext data
				return  new TagObject(TokenType.PLAIN_TEXT, text);
		}
		return null;
	}
	
	/**
	 * Checks if the passed string is only white space.
	 *
	 * @param s the string
	 * @return true, if s is white space
	 */
	public boolean isWhiteSpace(String s){
		return s.trim().isEmpty();		
	}
	
	/**
	 * Skip white space method. Just recalls the getToken() method to move onto
	 * the next token in the file
	 *
	 * @return the next tag object
	 */
	public TagObject skipWhiteSpace(){
		//moves to next token, discarding whitespace
		return getToken();
	}
	
	/**
	 * Process tag method. Processes a token that is determined to be a tag token.
	 * Checks first for a one word token then if needed checks for a two word token
	 * 
	 *
	 * @return the tag token
	 */
	public String processTag(){
		String tag = new String();
		tag += getOneWord();
		//check tag against list of one word tags
		if(tokens.isOneWordTag(tag)){
			return tag;
		}
		//get second word of tag
		else{
			tag += SPACE;
			tag += getOneWord();
			return tag;
		}
	}
	
	/**
	 * Gets the one word from the file string. Looks for spaces and line
	 * separators to determine end of word.
	 *
	 * @return the one word
	 */
	private String getOneWord(){
		String word = new String();
		char c;
		//loops until entire word is stored in string
		while(true){
			c = getChar();
			if(c == SPACE){
				advChar();
				break;
			}
			else if(c == LINE_SEPARATOR){
				lineNum++;
				advChar();
				break;
			}
			else{
				word += c;
				advChar();
			}
		}
		return word;
	}
	
	/**
	 * Process a token that is determined to be plain text. Looks for a # to determine the end 
	 * the plain text and the start of a tag.
	 *
	 * @return the string containing plaintext
	 */
	public String processText(){
		String text = new String();
		char c;
		//loops until start of next tag is found or file is empty
		while(charRemaining()){
			c = getChar();
			//Beginning of next tag found
			if(c == '#'){
				break;
			}
			else if(c == LINE_SEPARATOR){
				lineNum++;
				text += c;
				advChar();
			}
			else{
				text += c;
				advChar();
			}
		}
		return text;
	}
	
	/**
	 * advChar method increments the current position in the file string by one.
	 */
	public void advChar() {
		curPos++;
	}
	
	/**
	 * Gets the char at the current position determined by curPos variable.
	 *
	 * @return the char
	 */
	public char getChar(){
		return file.charAt(curPos);
	}
	
	/**
	 * Determines the amount of characters left in the file string.
	 *
	 * @return true, if current position is less than the file length.</br>
	 * 			or false, if current position is greater than the file length.
	 */
	public boolean charRemaining(){
		return curPos < file.length()-1;
	}
    
    /**
     * Looks up the passed token by comparing the token string to a HashMap of token value, token object pairs.
     *
     * @param token the string of the token value
     * @return the tag object
     */
    private TagObject lookup(String token){
    	HashMap<String, TagObject> tokenTable = tokens.getTokenTable();
    	//looks up given token in a hashmap of all known tokens
    	if(tokenTable.containsKey(token.toUpperCase())){
    		return tokenTable.get(token.toUpperCase());
    	}
    	else{
    		//token not found
    		throw new LexicalErrorException(token);
    	}
    }
    
    /**
     * Checks for tailing tokens. Used to make sure that there are no tokens after the expected end of file
     *
     * @return true, if there are trailing tokens
     */
    public boolean hasTailingTokens(){
    	//checks for remaining tokens.
    	//used after the #DOCUMENT END tag.
    	if(charRemaining()){
    		lex();
    		if(!isWhiteSpace(Compiler.nextToken.getTokenValue())){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * The Class LexicalErrorException that is used to throw an error if an 
     * invalid token is found.
     */
    @SuppressWarnings("serial")
	public class LexicalErrorException extends RuntimeException{
    	
	    /**
	     * Instantiates a new lexical error exception to throw the proper message.
	     *
	     * @param token the invalid token value
	     */
	    public LexicalErrorException(String token){
    		super("Error: Inavlid Token: " + token + " Line: " + lineNum);
    	}
    }
}