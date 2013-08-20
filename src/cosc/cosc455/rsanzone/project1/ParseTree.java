package cosc.cosc455.rsanzone.project1;

import java.util.ArrayList;
import java.util.Stack;


/**
 * The Class ParseTree.
 *  
 * @author Ronald Sanzone
 * @course COSC 455
 * @project 1
 */
public class ParseTree {
	//Enum to set the ParseNode to a certain tag type
	/**
	 * The Enum NodeType.
	 */
	public static enum NodeType {
        
        /** The document node. */
        DOCUMENT  		("#DOCUMENT BEGIN", "#DOCUMENT END"),
		
		/** The head node. */
		HEAD      		("#HEAD BEGIN", "#HEAD END"),
		
		/** The title node. */
		TITLE     		("#TITLE BEGIN", "#TITLE END"),
		
		/** The paragraph node. */
		PARAGRAPH 		("#PARAGRAPH BEGIN", "#PARAGRAPH END"),
		
		/** The bold node. */
		BOLD      		("#BOLD BEGIN", "#BOLD END"),
		
		/** The italics node. */
		ITALICS	  		("#ITALICS BEGIN", "#ITALICS END"), 
		
		/** The list node. */
		LIST 	  		("#LIST BEGIN", "#LIST END"),
		
		/** The item node. */
		ITEM 	  		("#ITEM BEGIN", "#ITEM END"),
		
		/** The link node. */
		LINK 	  		("#LINK BEGIN", "#LINK END"),
		
		/** The text node. */
		TEXT      		("#TEXT"), 
		
		/** The address node. */
		ADDRESS   		("#ADDRESS"),
		
		/** The define node. */
		DEFINE 	  		("#DEFINE BEGIN", "#DEFINE END"),
		
		/** The name node. */
		NAME      		("#NAME"),
		
		/** The value node. */
		VALUE 	  		("#VALUE"),
		
		/** The use node. */
		USE		  		("#USE BEGIN", "#USE END"),
		
		/** The plain text node. */
		PLAIN_TEXT		(true),
		//Used as tagless place holders to match BNF structure
		/** The body node. */
		BODY			(false),
		
		/** The inner paragraph node. */
		INNER_PARAGRAPH	(false),
		
		/** The inner text node. */
		INNER_TEXT		(false),
		
		/** The list items node. */
		LIST_ITEMS		(false),
		
		/** The inner list node. */
		INNER_LIST		(false),
		
		/** The outer define node. */
		OUTER_DEFINE	(false);
		
		/** The opening tag of the node. */
		private final String openingTag;
		
		/** The closing tag of the node. */
		private final String closingTag;
		
		/** The has opening tag boolean of the node. */
		private final boolean hasOpeningTag;
		
		/** The has closing tag boolean of the node. */
		private final boolean hasClosingTag;
		
		/** The is text boolean of the node. */
		private final boolean isText;
		
		/**
		 * Instantiates a new node type.
		 *
		 * @param openingTag the opening tag of the node
		 * @param closingTag the closing tag of the node
		 */
		NodeType(String openingTag, String closingTag){
			this.openingTag = openingTag;
			this.closingTag = closingTag;
			this.hasOpeningTag = true;
			this.hasClosingTag = true;
			this.isText = false;
		}
		
		/**
		 * Instantiates a new node type.
		 *
		 * @param openingTag the opening tag of the node
		 */
		NodeType(String openingTag){
			this.openingTag = openingTag;
			this.closingTag = "";
			this.hasOpeningTag = true;
			this.hasClosingTag = false;
			this.isText = false;
		}
		
		/**
		 * Instantiates a new node type.
		 *
		 * @param isText the is text boolean of the node
		 */
		NodeType(boolean isText){
			this.openingTag = "";
			this.closingTag = "";
			this.hasOpeningTag = false;
			this.hasClosingTag = false;
			this.isText = isText;
		}
		
		/**
		 * Gets the opening tag.
		 *
		 * @return the opening tag
		 */
		public String getOpeningTag() {
			return openingTag;
		}		
		
		/**
		 * Gets the closing tag.
		 *
		 * @return the closing tag
		 */
		public String getClosingTag() {
			return closingTag;
		}
		
		/**
		 * Checks for opening tag.
		 *
		 * @return true, if successful
		 */
		public boolean hasOpeningTag() {
			return hasOpeningTag;
		}
		
		/**
		 * Checks for closing tag.
		 *
		 * @return true, if successful
		 */
		public boolean hasClosingTag() {
			return hasClosingTag;
		}
		
		/**
		 * Checks if is text.
		 *
		 * @return true, if is text
		 */
		public boolean isText() {
			return isText;
		}
	}
	
	/** The root node. */
	private ParseNode root;


	/**
	 * Gets the root node.
	 *
	 * @return the root
	 */
	public ParseNode getRoot() {
		return root;
	}

	/**
	 * Sets the root node.
	 *
	 * @param root the new root
	 */
	public void setRoot(ParseNode root) {
		this.root = root;
	}
	/**
	 * String stack.
	 * Converts parse tree to a stack of strings to pass to semantic
	 * @return the stack
	 */
	public Stack<String> stringStack(){
		Stack<String> stack = new Stack<String>();
		
		stack.push(root.getOpeningTag());
		//call stack builder to begin recursive walk through tree
		stackBuilder(root, stack);
		
		stack.push(root.getClosingTag());
		
		return stack;
	}
	/**
	 * Stack builder.
	 * String stack recursive helper
	 * @param node the node
	 * @param stack the stack
	 * @return the stack
	 */
	private Stack<String> stackBuilder(ParseNode node, Stack<String> stack){
		for(ParseNode child : node.getChildren()){
			if(child.isText()){
				stack.push(child.getData());
				stackBuilder(child, stack);
			}
			if(child.hasOpeningTag() && child.hasClosingTag()){
				stack.push(child.getOpeningTag());
				stackBuilder(child, stack);
				stack.push(child.getClosingTag());
			}
			if(child.hasOpeningTag() && !child.hasClosingTag()){
				stack.push(child.getOpeningTag());
				stackBuilder(child, stack);
			}
			if(!child.hasOpeningTag() && !child.hasOpeningTag() && !child.isText){
				stackBuilder(child, stack);
			}
		}
		return stack;
		
	}
	
	/**
	 * The Class ParseNode. Contains the data needed by each node
	 */
	public static class ParseNode {
		
		/** The nodetype. */
		NodeType nodetype;
		//tag or text data
		/** The opening tag. */
		private final String openingTag;
		
		/** The closeing tag. */
		private final String closeingTag;
		
		/** The data. */
		private final String data;
		//booleans used in string building and other methods
		/** The has opening tag. */
		private final boolean hasOpeningTag;
		
		/** The has closing tag. */
		private final boolean hasClosingTag;
		
		/** The is text boolean. */
		private final boolean isText;
		
		/** The parent. */
		private ParseNode parent;
		
		/** The children. */
		private ArrayList<ParseNode> children;
		
		/**
		 * Instantiates a new parses the node.
		 *
		 * @param nodetype the nodetype of the node
		 */
		public ParseNode(NodeType nodetype){
			this.nodetype = nodetype;
			this.openingTag = nodetype.getOpeningTag();
			this.closeingTag = nodetype.getClosingTag();
			this.hasOpeningTag = nodetype.hasOpeningTag();
			this.hasClosingTag = nodetype.hasClosingTag();
			this.isText = nodetype.isText();
			this.data = "";
			
			this.setChildren(new ArrayList<ParseNode>());
			
		}
		
		/**
		 * Instantiates a new parses the node.
		 *
		 * @param nodetype the nodetype of the node
		 * @param data the data that is contained in a plaintext node
		 */
		public ParseNode(NodeType nodetype, String data){
			this.nodetype = nodetype;
			this.openingTag = nodetype.getOpeningTag();
			this.closeingTag = nodetype.getClosingTag();
			this.hasOpeningTag = nodetype.hasOpeningTag();
			this.hasClosingTag = nodetype.hasClosingTag();
			this.isText = nodetype.isText();
			this.data = data;
			
			this.setChildren(new ArrayList<ParseNode>());
			
		}
		
		/**
		 * Gets the data.
		 *
		 * @return the data
		 */
		public String getData() {
			return data;
		}
		
		/**
		 * Gets the parent.
		 *
		 * @return the parent
		 */
		public ParseNode getParent() {
			return parent;
		}
		
		/**
		 * Sets the parent.
		 *
		 * @param parent the new parent
		 */
		public void setParent(ParseNode parent) {
			this.parent = parent;
		}
		
		/**
		 * Gets the children.
		 *
		 * @return the children
		 */
		public ArrayList<ParseNode> getChildren() {
			return children;
		}
		
		/**
		 * Sets the children.
		 *
		 * @param children the new children
		 */
		public void setChildren(ArrayList<ParseNode> children) {
			this.children = children;
		}
		
		/**
		 * Adds the child.
		 *
		 * @param n the node child
		 */
		public void addChild(ParseNode n){
			if(n != null){
				n.setParent(this);
				this.children.add(n);
			}
		}
		
		/**
		 * Gets the opening tag.
		 *
		 * @return the opening tag
		 */
		public String getOpeningTag() {
			return openingTag;
		}
		
		/**
		 * Gets the closing tag.
		 *
		 * @return the closing tag
		 */
		public String getClosingTag() {
			return closeingTag;
		}
		
		/**
		 * Checks for opening tag.
		 *
		 * @return true, if successful
		 */
		public boolean hasOpeningTag() {
			return hasOpeningTag;
		}
		
		/**
		 * Checks for closing tag.
		 *
		 * @return true, if successful
		 */
		public boolean hasClosingTag() {
			return hasClosingTag;
		}
		
		/**
		 * Checks if is text.
		 *
		 * @return true, if is text
		 */
		public boolean isText() {
			return isText;
		}
		
	}
}