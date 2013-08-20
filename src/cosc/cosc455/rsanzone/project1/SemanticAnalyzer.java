package cosc.cosc455.rsanzone.project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * The Class SemanticAnalyzer.
 *  
 * @author Ronald Sanzone
 * @course COSC 455
 * @project 1
 */
public class SemanticAnalyzer {

	/**
	 * The Enum MkdType used to set the MkdObjects to a certain type.
	 */
	public static enum MkdType {
        
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
		
		/** The text tag. */
		TEXT 			("#TEXT", false), 
		
		/** The address tag. */
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
		
		/** The istext boolean that determines if the type is plaintext. */
		private final boolean isText;
	
		/**
		 * Instantiates a new mkd type.
		 *
		 * @param tag the tag value
		 * @param isText the istext boolean
		 */
		MkdType(String tag, boolean isText){
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
		 * Checks if the type is plain text.
		 *
		 * @return true, if is plain text
		 */
		public boolean isText() {
			return isText;
		}
	}
	
	/**
	 * The Enum HtmlType that is used to set the HtmlObject to a certian type.
	 */
	public static enum HtmlType {
        
        /** The document begin tag. */
        DOCUMENT_BEGIN  ("<html>", false),
		
		/** The document end tag. */
		DOCUMENT_END    ("</html>", false),
		
		/** The head begin tag. */
		HEAD_BEGIN      ("<head>", false),
		
		/** The head end tag. */
		HEAD_END        ("</head>", false),
		
		/** The title begin tag. */
		TITLE_BEGIN     ("<title>", false),
		
		/** The title end tag. */
		TITLE_END  	    ("</title>", false), 
		
		/** The paragraph begin tag. */
		PARAGRAPH_BEGIN ("<p>", false),
		
		/** The paragraph end tag. */
		PARAGRAPH_END   ("</p>", false), 
		
		/** The bold begin tag. */
		BOLD_BEGIN      ("<b>", false),
		
		/** The bold end tag. */
		BOLD_END        ("</b>", false), 
		
		/** The italics begin tag. */
		ITALICS_BEGIN 	("<i>", false),
		
		/** The italics end tag. */
		ITALICS_END 	("</i>", false), 
		
		/** The list begin tag. */
		LIST_BEGIN 		("<ul>", false),
		
		/** The list end tag. */
		LIST_END 		("</ul>", false),
		
		/** The item begin tag. */
		ITEM_BEGIN 		("<li>", false),
		
		/** The item end tag. */
		ITEM_END 		("</li>", false),
		
		/** The link begin tag. */
		LINK_BEGIN 		("<a ", false),
		
		/** The link end tag. */
		LINK_END		("</a>", false),
		
		/** The text tag. */
		TEXT 			("\">", false), 
		
		/** The address tag. */
		ADDRESS 		("href=\"", false),
		
		/** The plain text type. */
		PLAIN_TEXT		("", true);
		
		/** The tag value. */
		private final String tag;
		
		/** The is plain text boolean. */
		private final boolean isText;
		
		/**
		 * Instantiates a new html type.
		 *
		 * @param tag the tag value
		 * @param isText the is plain text boolean
		 */
		HtmlType(String tag, boolean isText){
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
		 * Checks if type is plain text.
		 *
		 * @return true, if is plain text
		 */
		public boolean isText() {
			return isText;
		}
	}
	
	/**
	 * The Class MkdObject used as a holder of information about a type of Mkd tag or token.
	 */
	public class MkdObject{
		
		/** The mkdtype that determines the objects values. */
		MkdType mkdtype;
		
		/** The istext boolean to tell if the type is plain text. */
		private final boolean istext;
		
		/** The data of the plain text, empty if object is not plain text type. */
		private String data = new String();
		
		/** The tag value. Empty if object is plain text */
		private String tag = new String();
	
		/**
		 * Instantiates a new mkd object.
		 *
		 * @param mkdtype the type of object to be made
		 */
		public MkdObject(MkdType mkdtype){
			this.mkdtype = mkdtype;
			this.tag = mkdtype.getTag();
			this.istext = mkdtype.isText;
		}
		
		/**
		 * Gets the type of object.
		 *
		 * @return the mkdtype
		 */
		public MkdType getType(){
			return mkdtype;
		}
		
		/**
		 * Gets the data of the plain text.
		 *
		 * @return the data string
		 */
		public String getData() {
			return data;
		}
		
		/**
		 * Sets the data of the plain text.
		 *
		 * @param data the new data string
		 */
		public void setData(String data) {
			this.data = data;
		}
		
		/**
		 * Gets the tag value of the object.
		 *
		 * @return the tag value
		 */
		public String getTag() {
			return tag;
		}
		
		/**
		 * Sets the tag value of the object.
		 *
		 * @param tag the new tag value
		 */
		public void setTag(String tag) {
			this.tag = tag;
		}
		
		/**
		 * Checks if the object is plain text.
		 *
		 * @return true, if is plain text
		 */
		public boolean isText(){
			return istext;
		}
	}
	
	/**
	 * The Class HtmlObject is a holder of the information needed from the html tags and text.
	 */
	public class HtmlObject{
		
		/** The htmltype of the object. */
		HtmlType htmltype;
		
		/** The istext boolean. Tests to see if object is plain text. */
		private final boolean istext;
		
		/** The data of the plain text of the object. Blank if object is not plain text */
		private String data = new String();
		
		/** The tag value of the object. Blank if object is plain text*/
		private String tag = new String();
		
		/**
		 * Instantiates a new html object.
		 *
		 * @param htmltype the htmltype that the object will get its information from
		 */
		public HtmlObject(HtmlType htmltype){
			this.htmltype = htmltype;
			this.tag = htmltype.getTag();
			this.istext = htmltype.isText;
		}
		
		/**
		 * Gets the type of html tag or plain text.
		 *
		 * @return the type of html
		 */
		public HtmlType getType(){
			return htmltype;
		}
		
		/**
		 * Gets the data of the plain text.
		 *
		 * @return the data of the plain text
		 */
		public String getData() {
			return data;
		}
		
		/**
		 * Sets the data of the plain text.
		 *
		 * @param data the new plain text data
		 */
		public void setData(String data) {
			this.data = data;
		}
		
		/**
		 * Gets the tag of the object.
		 *
		 * @return the tag
		 */
		public String getTag() {
			return tag;
		}
		
		/**
		 * Sets the tag of the html object.
		 *
		 * @param tag the new html tag
		 */
		public void setTag(String tag) {
			this.tag = tag;
		}
		
		/**
		 * Checks if the html object is plain text.
		 *
		 * @return true, if is text
		 */
		public boolean isText(){
			return istext;
		}
	}
	
	/**
	 * The Class ScopeKey. Used as a key value for the hashmap that is used in the macro extension
	 * of the semantic analyzer
	 */
	public class ScopeKey{
		
		/** The name value of the MkdObject. */
		private MkdObject nameValue;
		
		/** The scope that the name was defined in. */
		private MkdObject scopeDefined;
		
		/** The key that is used in the hash map. */
		private final int key;
		
		/**
		 * Instantiates a new scope key.
		 *
		 * @param nameValue the name value
		 * @param scopeDefined the scope defined
		 */
		public ScopeKey(MkdObject nameValue, MkdObject scopeDefined){
			this.setNameValue(nameValue);
			this.setScopeDefined(scopeDefined);
			this.key = genKey();
		}
		
		/**
		 * Generates a integer key value using the hash code of the name value and scope defined
		 * after they are added together in a string.
		 *
		 * @return the hash code
		 */
		private int genKey(){
			//uses the string method hashCode to generate a unique code
			return (nameValue.getData() + scopeDefined.getTag()).hashCode();
		}
		
		/**
		 * Gets the scope that the name value was defined in.
		 *
		 * @return the scope defined
		 */
		public MkdObject getScopeDefined() {
			return scopeDefined;
		}

		/**
		 * Sets the scope that the name value was defined in.
		 *
		 * @param scopeDefined the new scope defined value
		 */
		public void setScopeDefined(MkdObject scopeDefined) {
			this.scopeDefined = scopeDefined;
		}

		/**
		 * Gets the name value that was defined.
		 *
		 * @return the name value
		 */
		public MkdObject getNameValue() {
			return nameValue;
		}

		/**
		 * Sets the name value that was defined.
		 *
		 * @param nameValue the new name value defined
		 */
		public void setNameValue(MkdObject nameValue) {
			this.nameValue = nameValue;
		}
		
		/**
		 * Gets the key that was generated from the hash code.
		 *
		 * @return the key
		 */
		public int getKey() {
			return key;
		}
		
	}
	
	/** The conversion table. To convert from mkd to html */
	private final HashMap<MkdType, HtmlObject> conversionTable;
	
	/** The mkd table. Used as a lookup table for MkdObjects */
	private final HashMap<String, MkdObject> mkdTable;
	
	/** The tree syntax tree that is passed from the syntax analyzer. */
	private final ParseTree tree;
	
	/** A list of all the begin tags used in the macro extension */
	private final ArrayList<String> beginTags;
	
	/** A list of all the end tags used in the macro extension */
	private final ArrayList<String> endTags;
	
	/** The mkd stack containing the file contents split up into MkdObject. */
	private Stack<MkdObject> mkdStack;
	
	/** The html stack that is filled with html code during conversion */
	private Stack<HtmlObject> htmlStack = new Stack<HtmlObject>();
	
	/**
	 * Instantiates a new semantic analyzer.
	 *
	 * @param tree the parse tree sent from the syntax analyzer
	 */
	public SemanticAnalyzer(ParseTree tree){
		this.tree = tree;
		this.mkdTable = populateMkdTable();
		this.conversionTable = populateConversionTable();
		this.beginTags = populateBeginList();
		this.endTags = populateEndList();
		this.mkdStack = convertStrStack(tree.stringStack());
	}
	
	/**
	 * Gets the parse tree.
	 *
	 * @return the parse tree
	 */
	public ParseTree getTree() {
		return tree;
	}
	
	/**
	 * Gets the conversion table.
	 *
	 * @return the conversion table
	 */
	public HashMap<MkdType, HtmlObject> getConversionTable() {
		return conversionTable;
	}
	
	/**
	 * Gets the mkd stack.
	 *
	 * @return the mkd stack
	 */
	public Stack<MkdObject> getMkdStack() {
		return mkdStack;
	}
	
	/**
	 * Sets the mkd stack.
	 *
	 * @param mkdStack the new mkd stack
	 */
	public void setMkdStack(Stack<MkdObject> mkdStack) {
		this.mkdStack = mkdStack;
	}
	
	/**
	 * Gets the html stack.
	 *
	 * @return the html stack
	 */
	public Stack<HtmlObject> getHtmlStack() {
		return htmlStack;
	}
	
	/**
	 * Sets the html stack.
	 *
	 * @param htmlStack the new html stack
	 */
	public void setHtmlStack(Stack<HtmlObject> htmlStack) {
		this.htmlStack = htmlStack;
	}
	
	/**
	 * Run method. Builds a translated mkdStack that fills in the macro information then translates 
	 * to html and send to the compiler.
	 */
	public void run(){
		//translates all macro tags in mkdStack to their proper values
		mkdStack =  translateMacroStack(mkdStack);
		//translate mkd to html
		translateMkdToHtml();
		//send html string to global variable
		Compiler.htmlString = htmlStackToString();
	}
	
	/**
	 * Populates a conversion table that is used to convert mkd to html.
	 *
	 * @return the conversion table
	 */
	private HashMap<MkdType, HtmlObject> populateConversionTable(){
		HashMap<MkdType, HtmlObject> table = new HashMap<MkdType, HtmlObject>();
		table.put(MkdType.DOCUMENT_BEGIN, new HtmlObject(HtmlType.DOCUMENT_BEGIN));
		table.put(MkdType.DOCUMENT_END, new HtmlObject(HtmlType.DOCUMENT_END));
		table.put(MkdType.HEAD_BEGIN, new HtmlObject(HtmlType.HEAD_BEGIN));
		table.put(MkdType.HEAD_END, new HtmlObject(HtmlType.HEAD_END));
		table.put(MkdType.TITLE_BEGIN, new HtmlObject(HtmlType.TITLE_BEGIN));
		table.put(MkdType.TITLE_END, new HtmlObject(HtmlType.TITLE_END));
		table.put(MkdType.PARAGRAPH_BEGIN, new HtmlObject(HtmlType.PARAGRAPH_BEGIN));
		table.put(MkdType.PARAGRAPH_END, new HtmlObject(HtmlType.PARAGRAPH_END));
		table.put(MkdType.BOLD_BEGIN, new HtmlObject(HtmlType.BOLD_BEGIN));
		table.put(MkdType.BOLD_END, new HtmlObject(HtmlType.BOLD_END));
		table.put(MkdType.ITALICS_BEGIN, new HtmlObject(HtmlType.ITALICS_BEGIN));
		table.put(MkdType.ITALICS_END, new HtmlObject(HtmlType.ITALICS_END));
		table.put(MkdType.LIST_BEGIN, new HtmlObject(HtmlType.LIST_BEGIN));
		table.put(MkdType.LIST_END, new HtmlObject(HtmlType.LIST_END));
		table.put(MkdType.ITEM_BEGIN, new HtmlObject(HtmlType.ITEM_BEGIN));
		table.put(MkdType.ITEM_END, new HtmlObject(HtmlType.ITEM_END));
		table.put(MkdType.LINK_BEGIN, new HtmlObject(HtmlType.LINK_BEGIN));
		table.put(MkdType.LINK_END, new HtmlObject(HtmlType.LINK_END));
		table.put(MkdType.TEXT, new HtmlObject(HtmlType.TEXT));
		table.put(MkdType.ADDRESS, new HtmlObject(HtmlType.ADDRESS));
		table.put(MkdType.PLAIN_TEXT, new HtmlObject(HtmlType.PLAIN_TEXT));
		return table;
	}
	
	/**
	 * Populates the mkd table that is used as a look up to populate the mkdStack.
	 *
	 * @return the mkdTable
	 */
	private HashMap<String, MkdObject> populateMkdTable(){
		HashMap<String, MkdObject> table = new HashMap<String, MkdObject>();
		table.put(MkdType.DOCUMENT_BEGIN.getTag(), new MkdObject(MkdType.DOCUMENT_BEGIN));
		table.put(MkdType.DOCUMENT_END.getTag(), new MkdObject(MkdType.DOCUMENT_END));
		table.put(MkdType.HEAD_BEGIN.getTag(), new MkdObject(MkdType.HEAD_BEGIN));
		table.put(MkdType.HEAD_END.getTag(), new MkdObject(MkdType.HEAD_END));
		table.put(MkdType.TITLE_BEGIN.getTag(), new MkdObject(MkdType.TITLE_BEGIN));
		table.put(MkdType.TITLE_END.getTag(), new MkdObject(MkdType.TITLE_END));
		table.put(MkdType.PARAGRAPH_BEGIN.getTag(), new MkdObject(MkdType.PARAGRAPH_BEGIN));
		table.put(MkdType.PARAGRAPH_END.getTag(), new MkdObject(MkdType.PARAGRAPH_END));
		table.put(MkdType.BOLD_BEGIN.getTag(), new MkdObject(MkdType.BOLD_BEGIN));
		table.put(MkdType.BOLD_END.getTag(), new MkdObject(MkdType.BOLD_END));
		table.put(MkdType.ITALICS_BEGIN.getTag(), new MkdObject(MkdType.ITALICS_BEGIN));
		table.put(MkdType.ITALICS_END.getTag(), new MkdObject(MkdType.ITALICS_END));
		table.put(MkdType.LIST_BEGIN.getTag(), new MkdObject(MkdType.LIST_BEGIN));
		table.put(MkdType.LIST_END.getTag(), new MkdObject(MkdType.LIST_END));
		table.put(MkdType.ITEM_BEGIN.getTag(), new MkdObject(MkdType.ITEM_BEGIN));
		table.put(MkdType.ITEM_END.getTag(), new MkdObject(MkdType.ITEM_END));
		table.put(MkdType.LINK_BEGIN.getTag(), new MkdObject(MkdType.LINK_BEGIN));
		table.put(MkdType.LINK_END.getTag(), new MkdObject(MkdType.LINK_END));
		table.put(MkdType.TEXT.getTag(), new MkdObject(MkdType.TEXT));
		table.put(MkdType.ADDRESS.getTag(), new MkdObject(MkdType.ADDRESS));
		table.put(MkdType.DEFINE_BEGIN.getTag(), new MkdObject(MkdType.DEFINE_BEGIN));
		table.put(MkdType.DEFINE_END.getTag(), new MkdObject(MkdType.DEFINE_END));
		table.put(MkdType.NAME.getTag(), new MkdObject(MkdType.NAME));
		table.put(MkdType.VALUE.getTag(), new MkdObject(MkdType.VALUE));
		table.put(MkdType.USE_BEGIN.getTag(), new MkdObject(MkdType.USE_BEGIN));
		table.put(MkdType.USE_END.getTag(), new MkdObject(MkdType.USE_END));
		table.put(MkdType.PLAIN_TEXT.getTag(), new MkdObject(MkdType.PLAIN_TEXT));
		return table;
	}
	
	/**
	 * Populate the list of begin tags.
	 *
	 * @return the begin tags
	 */
	private ArrayList<String> populateBeginList(){
		ArrayList<String> list = new ArrayList<String>();
		list.add(MkdType.BOLD_BEGIN.getTag());
		list.add(MkdType.DOCUMENT_BEGIN.getTag());
		list.add(MkdType.HEAD_BEGIN.getTag());
		list.add(MkdType.ITALICS_BEGIN.getTag());
		list.add(MkdType.ITEM_BEGIN.getTag());
		list.add(MkdType.LINK_BEGIN.getTag());
		list.add(MkdType.LIST_BEGIN.getTag());
		list.add(MkdType.PARAGRAPH_BEGIN.getTag());
		list.add(MkdType.TITLE_BEGIN.getTag());
		return list;
	}
	
	/**
	 *  Populate the list of end tags.
	 *
	 * @return the end tags
	 */
	private ArrayList<String> populateEndList(){
		ArrayList<String> list = new ArrayList<String>();
		list.add(MkdType.BOLD_END.getTag());
		list.add(MkdType.DOCUMENT_END.getTag());
		list.add(MkdType.HEAD_END.getTag());
		list.add(MkdType.ITALICS_END.getTag());
		list.add(MkdType.ITEM_END.getTag());
		list.add(MkdType.LINK_END.getTag());
		list.add(MkdType.LIST_END.getTag());
		list.add(MkdType.PARAGRAPH_END.getTag());
		list.add(MkdType.TITLE_END.getTag());
		return list;
	}
	
	/**
	 * Converts the string stack that is obtained from the parse tree
	 * to a mkdStack that is used in both the macro expansion and the 
	 * translation from mkd to html.
	 *
	 * @param strStack the string stack
	 * @return the mkd stack
	 */
	private Stack<MkdObject> convertStrStack(Stack<String> strStack){
		Stack<MkdObject> mkdStack = new Stack<MkdObject>(); 
		//loops untill strStack is empty
		while(!strStack.isEmpty()){
			String curStr = strStack.pop();
			//if mkdTable has curStr, curString is a tag token
			if(mkdTable.containsKey(curStr)){
				//look up tag and add object to stack
				MkdObject curMkdO = mkdTable.get(curStr);
				mkdStack.push(curMkdO);
			}
			//else curString is plain text
			else
			{ 
				//creates a mkdobject to hold plaintext values
				MkdObject tempobject = new MkdObject(MkdType.PLAIN_TEXT);
				tempobject.setData(curStr);
				mkdStack.push(tempobject);
			}
		}
		return mkdStack;
	}
	
	/**
	 * Creates a copy of an mkd stack to preserve the initial stack data
	 *
	 * @param initStack the initial stack
	 * @return the coppied stack
	 */
	@SuppressWarnings("unchecked")
	private Stack<MkdObject> copyMkdStack(Stack<MkdObject> initStack){
		return (Stack<MkdObject>) initStack.clone();
	}
	
	/**
	 * Creates a copy of an html stack to preserve the initial stack data
	 *
	 * @param initStack the initial stack
	 * @return the coppied stack
	 */
	@SuppressWarnings("unchecked")
	private Stack<HtmlObject> copyHtmlStack(Stack<HtmlObject> initStack){
		return (Stack<HtmlObject>) initStack.clone();
	}
	/**
	 * Translates the mkd stack to find and replace all of the macro notations 
	 * with their proper values. Uses the helper method of checkAllScopes to find
	 * the proper value for each macro.
	 *
	 * @param initialStack the initial stack with macro notations
	 * @return the final  translated stack
	 */
	private Stack<MkdObject> translateMacroStack(Stack<MkdObject> initialStack){
		HashMap<Integer, MkdObject> nameValueMap = new HashMap<Integer, MkdObject>();
		Stack<MkdObject> finalStack = new Stack<MkdObject>();
		
		Stack<MkdObject> remainingScopes = new Stack<MkdObject>();
		
		
		while(!initialStack.isEmpty()){
			//next object is a beginning tag
			if(beginTags.contains(initialStack.peek().getTag())){
				MkdObject temp = initialStack.pop();
				//add the begin tag to the stack of scopes
				remainingScopes.push(temp);
				//push object on translated stack
				finalStack.push(temp);
				continue;
			}
			//next object is end tag
			else if(endTags.contains(initialStack.peek().getTag())){
				//push object on translated stack
				finalStack.push(initialStack.pop());
				//pop off last added scope
				remainingScopes.pop();
				continue;
			}
			//next object is define tag
			else if(initialStack.peek().getType() == MkdType.DEFINE_BEGIN){
				//pop off the unneeded define begin and name tags
				initialStack.pop();
				initialStack.pop();
				//store name value
				MkdObject name = initialStack.pop();
				//pop off unneeded value tag
				initialStack.pop();
				//store value text
				MkdObject value = initialStack.pop();
				//pop off define end
				initialStack.pop();
				//add an entry into nameValueMap, using ScopeKey object as key
				nameValueMap.put(new ScopeKey(name, remainingScopes.peek()).getKey(), value);
				continue;
			}
			//next object is use tag
			else if(initialStack.peek().getType() == MkdType.USE_BEGIN){
				//pop off unneeded use begin tag
				initialStack.pop();
				//store name value
				MkdObject name = initialStack.pop();
				//check all of the scopes to see if there is a definition of the name value
				MkdObject value = checkAllScopes(name, remainingScopes, nameValueMap);
				//definition exists
				if(value != null){
					//push stack onto translated stack
					finalStack.push(value);
				}else{
					//error
					throw new SemanticException(name);
				}
				initialStack.pop();
				continue;
			}
			//next object is plain text
			else{
				finalStack.push(initialStack.pop());
			}
		}
		return finalStack;
	}
	
	/**
	 * Checks all of the current scopes stored in the namevalues table. Uses the table to translate
	 * a given macro name into a value.
	 *
	 * @param name the name of the use macro
	 * @param scopes the stack of current scopes
	 * @param nameValues the name value pairs stored in a hashMap
	 * @return the mkd object that contains the translated macro information
	 */
	public MkdObject checkAllScopes(MkdObject name, Stack<MkdObject> scopes, HashMap<Integer, MkdObject> nameValues){
		@SuppressWarnings("unchecked")
		Stack<MkdObject> clonedScopes = (Stack<MkdObject>)scopes.clone();
		while(!clonedScopes.isEmpty()){
			MkdObject tScope = clonedScopes.pop();
			//make a new scope key
			ScopeKey tKey = new ScopeKey(name, tScope);
			//check for definition in current scope
			if(nameValues.containsKey(tKey.getKey())){
				MkdObject value = nameValues.get(tKey.getKey());
				return value;
			}
		}
		return null;
	}
	
	/**
	 * Translate mkd to html. Takes the mkdStack and converts it to html code using the conversion table.
	 */
	private void translateMkdToHtml(){
		Stack<MkdObject> tempMkdS = copyMkdStack(mkdStack);
		while(!tempMkdS.isEmpty()){
			MkdObject tempMkdO = tempMkdS.pop();
			//tempMkdO is plain text
			if(tempMkdO.isText()){
				//build new html object
				HtmlObject textObject = new HtmlObject(HtmlType.PLAIN_TEXT);
				textObject.setData(tempMkdO.getData());
				htmlStack.push(textObject);
			}
			//tempMkdO is tag
			else{
				//convert mkd to html object and push to stack
				HtmlObject tempHtmlO = conversionTable.get(tempMkdO.getType());
				htmlStack.push(tempHtmlO);
			}
		}
		
	}
	
	/**
	 * Converts the translated html stack to a string which will eventually be written 
	 * out to a file.
	 *
	 * @return the string of compiled file contents
	 */
	private String htmlStackToString(){
		Stack<HtmlObject> tempHtmlStack = copyHtmlStack(htmlStack);
		String htmlString = new String();
		while(!tempHtmlStack.isEmpty()){
			//htmlobject is plaintext
			if(tempHtmlStack.peek().isText()){
				//append object data
				htmlString += tempHtmlStack.pop().getData();
			}
			//object is tag object
			else
			{
				//append object tag
				htmlString += tempHtmlStack.pop().getTag();
			}
		}
		return htmlString;
	}
	
	/**
	 * The Class SemanticException. A custom exception that is used to throw an error when a 
	 * use macro is found with no definition.
	 */
	@SuppressWarnings("serial")
	public class SemanticException extends RuntimeException{
		
		/**
		 * Instantiates a new semantic exception.
		 *
		 * @param name the name of the use macro that is causing the error
		 */
		public SemanticException(MkdObject name){
			super("Semantic Error: No definition found for macro name: " + name.getData());			
		}
	}
}
