package cosc.cosc455.rsanzone.project1;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Class Tokens. Contains the TokenObjects that are used by the syntax and lexical analyzers to hold
 * the data of the tokens parsed from the input file.
 *
 * @author Ronald Sanzone
 */
public class Tokens {
	
	public static enum TokenType {
        
        DOCUMENT_BEGIN  ("#DOCUMENT BEGIN", false),
		DOCUMENT_END    ("#DOCUMENT END", false),
		HEAD_BEGIN      ("#HEAD BEGIN", false),
		HEAD_END        ("#HEAD END", false),
		TITLE_BEGIN     ("#TITLE BEGIN", false),
        TITLE_END("#TITLE END", false),
        PARAGRAPH_BEGIN("#PARAGRAPH BEGIN", false),
        PARAGRAPH_END("#PARAGRAPH END", false),
        BOLD_BEGIN("#BOLD BEGIN", false),
        BOLD_END("#BOLD END", false),
        ITALICS_BEGIN("#ITALICS BEGIN", false),
        ITALICS_END("#ITALICS END", false),
        LIST_BEGIN("#LIST BEGIN", false),
        LIST_END("#LIST END", false),
        ITEM_BEGIN("#ITEM BEGIN", false),
        ITEM_END 		("#ITEM END", false),
		LINK_BEGIN 		("#LINK BEGIN", false),
		LINK_END		("#LINK END", false),
        TEXT("#TEXT", false),
        ADDRESS 		("#ADDRESS", false),
		DEFINE_BEGIN 	("#DEFINE BEGIN", false),
		DEFINE_END 		("#DEFINE END", false),
		NAME 			("#NAME", false),
		VALUE 			("#VALUE", false),
		USE_BEGIN 		("#USE BEGIN", false),
		USE_END 		("#USE END", false),
		PLAIN_TEXT		("", true);

        private final String tag;
        private final boolean isText;

        TokenType(String tag, boolean isText) {
            this.tag = tag;
            this.isText = isText;
		}
		
		public String getTag() {
			return tag;
		}
		
		public boolean isText() {
			return isText;
		}
	}
	
	private HashMap<String, TagObject> tokenTable;
	
	public Tokens(){
		this.setTokenTable(buildTable());
	}

	public boolean isOneWordTag(String s){
		ArrayList<String> oneWordTags = buildOneWordList();
		return oneWordTags.contains(s);
	}
	
	private ArrayList<String> buildOneWordList(){
		ArrayList<String> list = new ArrayList<String>();
		list.add(TokenType.TEXT.getTag());
		list.add(TokenType.ADDRESS.getTag());
		list.add(TokenType.NAME.getTag());
		list.add(TokenType.VALUE.getTag());
		return list;
	}
	
	private HashMap<String, TagObject> buildTable(){
		ArrayList<TagObject> temp_list= buildList();
		HashMap<String, TagObject> table = new HashMap<String, TagObject>(100);
		//builds a hash map using the token values as a key.
		for(TagObject t : temp_list){
			table.put(t.getTokenValue(), t);
		}
		return table;
	}
	
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
	
	public HashMap<String, TagObject> getTokenTable() {
		return tokenTable;
	}
	
	public void setTokenTable(HashMap<String, TagObject> tokenTable) {
		this.tokenTable = tokenTable;
	}
	
	public static class TagObject{
		
		TokenType tokentype;
		private String tokenValue;

        TagObject(TokenType t) {
            this.tokentype = t;
            this.tokenValue = tokentype.getTag();
		}
		
		TagObject(TokenType t, String text){
			this.tokentype = t;
			if(tokentype.isText()){
				this.tokenValue = text;
			}
			else{
				this.tokenValue = tokentype.getTag();
			}
		}
		
		public TokenType getTokenType(){
			return tokentype;
		}
		
		public String getTokenValue(){
			return this.tokenValue;
		}

	}

}