package cosc.cosc455.rsanzone.project1;

import java.util.ArrayList;
import java.util.Stack;


/**
 * The Class ParseTree.
 *  
 * @author Ronald Sanzone
 */
public class ParseTree {
	public static enum NodeType {
        
        DOCUMENT  		("#DOCUMENT BEGIN", "#DOCUMENT END"),
		HEAD      		("#HEAD BEGIN", "#HEAD END"),
		TITLE     		("#TITLE BEGIN", "#TITLE END"),
		PARAGRAPH 		("#PARAGRAPH BEGIN", "#PARAGRAPH END"),
		BOLD      		("#BOLD BEGIN", "#BOLD END"),
        ITALICS("#ITALICS BEGIN", "#ITALICS END"),
        LIST 	  		("#LIST BEGIN", "#LIST END"),
		ITEM 	  		("#ITEM BEGIN", "#ITEM END"),
		LINK 	  		("#LINK BEGIN", "#LINK END"),
        TEXT("#TEXT"),
        ADDRESS   		("#ADDRESS"),
		DEFINE 	  		("#DEFINE BEGIN", "#DEFINE END"),
		NAME      		("#NAME"),
		VALUE 	  		("#VALUE"),
		USE		  		("#USE BEGIN", "#USE END"),
		PLAIN_TEXT		(true),
		BODY			(false),
		INNER_PARAGRAPH	(false),
		INNER_TEXT		(false),
		LIST_ITEMS		(false),
		INNER_LIST		(false),
		OUTER_DEFINE	(false);

        private final String openingTag;
        private final String closingTag;
		private final boolean hasOpeningTag;
		private final boolean hasClosingTag;
		private final boolean isText;

        NodeType(String openingTag, String closingTag) {
            this.openingTag = openingTag;
            this.closingTag = closingTag;
			this.hasOpeningTag = true;
			this.hasClosingTag = true;
			this.isText = false;
		}
		
		NodeType(String openingTag){
			this.openingTag = openingTag;
			this.closingTag = "";
			this.hasOpeningTag = true;
			this.hasClosingTag = false;
			this.isText = false;
		}
		
		NodeType(boolean isText){
			this.openingTag = "";
			this.closingTag = "";
			this.hasOpeningTag = false;
			this.hasClosingTag = false;
			this.isText = isText;
		}
		
		public String getOpeningTag() {
			return openingTag;
		}		
		
		public String getClosingTag() {
			return closingTag;
		}
		
		public boolean hasOpeningTag() {
			return hasOpeningTag;
		}

        public boolean hasClosingTag() {
            return hasClosingTag;
		}

        public boolean isText() {
            return isText;
		}
	}
	
	private ParseNode root;


	public ParseNode getRoot() {
		return root;
	}

	public void setRoot(ParseNode root) {
		this.root = root;
	}

    public Stack<String> stringStack() {
        Stack<String> stack = new Stack<String>();

        stack.push(root.getOpeningTag());
		//call stack builder to begin recursive walk through tree
		stackBuilder(root, stack);
		
		stack.push(root.getClosingTag());
		
		return stack;
	}

    private Stack<String> stackBuilder(ParseNode node, Stack<String> stack) {
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


    public static class ParseNode {

        NodeType nodetype;
		private final String openingTag;
		private final String closeingTag;
		private final String data;
		private final boolean hasOpeningTag;
		private final boolean hasClosingTag;
		private final boolean isText;
		private ParseNode parent;
		private ArrayList<ParseNode> children;
		
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


        public ParseNode(NodeType nodetype, String data) {
            this.nodetype = nodetype;
			this.openingTag = nodetype.getOpeningTag();
			this.closeingTag = nodetype.getClosingTag();
			this.hasOpeningTag = nodetype.hasOpeningTag();
			this.hasClosingTag = nodetype.hasClosingTag();
			this.isText = nodetype.isText();
			this.data = data;

            this.setChildren(new ArrayList<ParseNode>());

        }


        public String getData() {
            return data;
		}

        public ParseNode getParent() {
            return parent;
		}
		
		public void setParent(ParseNode parent) {
			this.parent = parent;
		}
		
		public ArrayList<ParseNode> getChildren() {
			return children;
		}
		
		public void setChildren(ArrayList<ParseNode> children) {
			this.children = children;
		}
		
		public void addChild(ParseNode n){
			if(n != null){
				n.setParent(this);
				this.children.add(n);
			}
		}
		
		public String getOpeningTag() {
			return openingTag;
		}
		
		public String getClosingTag() {
			return closeingTag;
		}
		
		public boolean hasOpeningTag() {
			return hasOpeningTag;
		}
		
		public boolean hasClosingTag() {
			return hasClosingTag;
		}
		
		public boolean isText() {
			return isText;
		}
		
	}
}