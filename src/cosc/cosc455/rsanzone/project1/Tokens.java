package cosc.cosc455.rsanzone.project1;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Class Tokens. Contains the TokenObjects that are used by the syntax and lexical analyzers to hold 
 * the data of the tokens parsed from the input file.
 *  
 * @author Ronald Sanzone
 * @course COSC 455
 * @project 1
 */
public class Tokens {
	
	/**
	 * The Enum TokenType. Used to determine the type of a given TokenObject
	 */
	public static enum TokenType {
        
        /** The document begin tag. */
        DOCUMENT_BEGIN  ("#DOCUMENT BEGIN", false),
		
		/** The document end tag. */
		DOCUMENT_END    ("#DOCUMENT END", false),
		
		/** The head begin tag. */
		HEAD_BEGIN      ("#HEAD BEGIN", false),
		
		/** The head end tag. */
		HEAD_END        ("#HEAD END", false),
		
		/** The title begin tag. */
		TITLE_BEGIN     ("#TITLE BEGIN", false),
		
		/** The title end tag. */
		TITLE_END  	    ("#TITLE END", false), 
		
		/** The paragraph begin tag. */
		PARAGRAPH_BEGIN ("#PARAGRAPH BEGIN", false),
		
		/** The paragraph end tag. */
		PARAGRAPH_END   ("#PARAGRAPH END", false), 
		
		/** The bold begin tag. */
		BOLD_BEGIN      ("#BOLD BEGIN", false),
		
		/** The bold end tag. */
		BOLD_END        ("#BOLD END", false), 
		
		/** The italics begin tag. */
		ITALICS_BEGIN 	("#ITALICS BEGIN", false),
		
		/** The italics end tag. */
		ITALICS_END 	("#ITALICS END", false), 
		
		/** The list begin tag. */
		LIST_BEGIN 		("#LIST BEGIN", false),
		
		/** The list end tag. */
		LIST_END 		("#LIST END", false),
		
		/** The item begin tag. */
		ITEM_BEGIN 		("#ITEM BEGIN", false),
		
		/** The item end tag. */
		ITEM_END 		("#ITEM END", false),
		
		/** The link begin tag. */
		LINK_BEGIN 		("#LINK BEGIN", false),
		
		/** The link end tag. */
		LINK_END		("#LINK END", false),
		
		/** The text tag tag. */
		TEXT 			("#TEXT", false), 
		
		/** The address tag tag. */
		ADDRESS 		("#ADDRESS", false),
		
		/** The define begin tag. */
		DEFINE_BEGIN 	("#DEFINE BEGIN", false),
		
		/** The define end tag. */
		DEFINE_END 		("#DEFINE END", false),
		
		/** The name tag. */
		NAME 			("#NAME", false),
		
		/** The value tag. */
		VALUE 			("#VALUE", false),
		
		/** The use begin tag. */
		USE_BEGIN 		("#USE BEGIN", false),
		
		/** The use end tag. */
		USE_END 		("#USE END", false),
		
		/** The plain text token. */
		PLAIN_TEXT		("", true);
		
		/** The tag value. */
		private final String tag;
		
		/** The is text boolean to determine if the type is plain text. */
		private final boolean isText;
		
		/**
		 * Instantiates a new token type.
		 *
		 * @param tag the tag value
		 * @param isText the is text boolean
		 */
		TokenType(String tag, boolean isText){
			this.tag = tag;
			this.isText = isText;
		}
		
		/**
		 * Gets the tag value.
		 *
		 * @return the tag value
		 */
		public String getTag() {
			return tag;
		}
		
		/**
		 * Checks if is plain text.
		 *
		 * @return true, if is plain text
		 */
		public boolean isText() {
			return isText;
		}
	}
	
	/** The token table. Used to lookup a token value and return the appropriate object */
	private HashMap<String, TagObject> tokenTable;
	
	/**
	 * Instantiates a new tokens and uses the buildtable method to set the tokenTable.
	 */
	public Tokens(){
		this.setTokenTable(buildTable());
	}

	/**
	 * Checks if a tag is an one word tag.
	 *
	 * @param s the tag value
	 * @return true, if is an one word tag
	 */
	public boolean isOneWordTag(String s){
		ArrayList<String> oneWordTags = buildOneWordList();
		return oneWordTags.contains(s);
	}
	
	/**
	 * Builds a list containg all of the one word tag values.
	 *
	 * @return the array list
	 */
	private ArrayList<String> buildOneWordList(){
		ArrayList<String> list = new ArrayList<String>();
		list.add(TokenType.TEXT.getTag());
		list.add(TokenType.ADDRESS.getTag());
		list.add(TokenType.NAME.getTag());
		list.add(TokenType.VALUE.getTag());
		return list;
	}
	
	/**
	 * Builds the table that contains all of the tag values paired with their objects.
	 *
	 * @return the hash map containg the pairs
	 */
	private HashMap<String, TagObject> buildTable(){
		ArrayList<TagObject> temp_list= buildList();
		HashMap<String, TagObject> table = new HashMap<String, TagObject>(100);
		//builds a hash map using the token values as a key.
		for(TagObject t : temp_list){
			table.put(t.getTokenValue(), t);
		}
		return table;
	}
	
	/**
	 * Builds a list of all possible tag objects. Used in the generation of the hashmap table
	 *
	 * @return the array list
	 */
	private ArrayList<TagObject> buildList(){
		ArrayList<TagObject> list = new ArrayList<TagObject>(22);
		list.add(new TagObject(TokenType.DOCUMENT_BEGIN));
		list.add(new TagObject(TokenType.DOCUMENT_END));
		list.add(new TagObject(TokenType.HEAD_BEGIN));
		list.add(new TagObject(TokenType.HEAD_END));
		list.add(new TagObject(TokenType.TITLE_BEGIN));
		list.add(new TagObject(TokenType.TITLE_END));
		list.add(new TagObject(TokenType.PARAGRAPH_BEGIN));
		list.add(new TagObject(TokenType.PARAGRAPH_END));
		list.add(new TagObject(TokenType.BOLD_BEGIN));
		list.add(new TagObject(TokenType.BOLD_END));
		list.add(new TagObject(TokenType.ITALICS_BEGIN));
		list.add(new TagObject(TokenType.ITALICS_END));
		list.add(new TagObject(TokenType.LIST_BEGIN));
		list.add(new TagObject(TokenType.LIST_END));
		list.add(new TagObject(TokenType.ITEM_BEGIN));
		list.add(new TagObject(TokenType.ITEM_END));
		list.add(new TagObject(TokenType.LINK_BEGIN));
		list.add(new TagObject(TokenType.LINK_END));
		list.add(new TagObject(TokenType.TEXT));
		list.add(new TagObject(TokenType.ADDRESS));
		list.add(new TagObject(TokenType.DEFINE_BEGIN));
		list.add(new TagObject(TokenType.DEFINE_END));
		list.add(new TagObject(TokenType.NAME));
		list.add(new TagObject(TokenType.VALUE));
		list.add(new TagObject(TokenType.USE_BEGIN));
		list.add(new TagObject(TokenType.USE_END));
		return list;
	}
	
	/**
	 * Gets the token table.
	 *
	 * @return the token table
	 */
	public HashMap<String, TagObject> getTokenTable() {
		return tokenTable;
	}
	
	/**
	 * Sets the token table.
	 *
	 * @param tokenTable the token table hashmap
	 */
	public void setTokenTable(HashMap<String, TagObject> tokenTable) {
		this.tokenTable = tokenTable;
	}
	
	/**
	 * The Class TagObject. Contains all of the tag data needed by the syntax analyzer and lexical analyzer.
	 */
	public static class TagObject{
		
		/** The tokentype of the tag object. */
		TokenType tokentype;
		
		/** The token value. */
		private String tokenValue;
		
		/**
		 * Instantiates a new tag object.
		 * Sets the token type and the tag value
		 *
		 * @param t the token type of the tag object
		 */
		TagObject(TokenType t){
			this.tokentype = t;
			this.tokenValue = tokentype.getTag();
		}
		
		/**
		 * Instantiates a new tag object.
		 * Sets the token type, and if the token type 
		 * is plain text, sets the tokenValue to the plain text.
		 * @param t the token type
		 * @param text the text of the plain text
		 */
		TagObject(TokenType t, String text){
			this.tokentype = t;
			if(tokentype.isText()){
				this.tokenValue = text;
			}
			else{
				this.tokenValue = tokentype.getTag();
			}
		}
		
		/**
		 * Gets the token type.
		 *
		 * @return the token type
		 */
		public TokenType getTokenType(){
			return tokentype;
		}
		
		/**
		 * Gets the token value.
		 *
		 * @return the token value
		 */
		public String getTokenValue(){
			return this.tokenValue;
		}
		
		/**
		 * Checks to see if two tokens are equal by comparing the tokenValue 
		 *
		 * @param text the text
		 * @return true, if successful
		 */
		public boolean equals(String text){
			return this.tokenValue.equalsIgnoreCase(text);
		}
	}

}