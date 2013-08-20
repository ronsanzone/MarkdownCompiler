package cosc.cosc455.rsanzone.project1;

import cosc.cosc455.rsanzone.project1.ParseTree.NodeType;
import cosc.cosc455.rsanzone.project1.ParseTree.ParseNode;


/**
 * The Class SyntaxAnalyzer that uses the lexical analyzer to get tokens and 
 * compare them to the syntax rules.
 * 
 * @author Ronald Sanzone
 * @course COSC 455
 * @project 1
 */
public class SyntaxAnalyzer {
	
	/**
	 * Start method that sets the root node of the compiler global tree as the document method.
	 */
	public void start(){
		//begins syntax analyzer at the document method
		ParseNode rootNode = document();
		//passes finished tree to global variable
		Compiler.tree.setRoot(rootNode);
	}
	
	/**
	 * Document syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode document(){
		//create new node
		ParseNode node = new ParseNode(NodeType.DOCUMENT);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.DOCUMENT_BEGIN){
			Compiler.lexer.lex();
		}else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.DOCUMENT_BEGIN); }
		
		//add the appropriate child nodes
		node.addChild(macroDefine());
		node.addChild(head());
		node.addChild(body());
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.DOCUMENT_END){	
			if(Compiler.lexer.hasTailingTokens()){
				throw new SyntaxEndOfFileException(Compiler.nextToken.getTokenValue());
			}
		}else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.DOCUMENT_END); }
		//return populated node
		return node;
	}
	
	/**
	 * Head syntax element.
	 *
	 *
	 * @return the parse tree node.
	
	 */
	public ParseNode head(){
		ParseNode node = new ParseNode(NodeType.HEAD);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.HEAD_BEGIN){
			if(Compiler.nextToken.getTokenType() == Tokens.TokenType.HEAD_BEGIN){
				Compiler.lexer.lex();
			}else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.HEAD_BEGIN); }
			
			//add appropriate child nodes
			node.addChild(title());

			if(Compiler.nextToken.getTokenType() == Tokens.TokenType.HEAD_END){
				Compiler.lexer.lex();
			}else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.HEAD_END); }	
		}
		else{
			//EMPTY
			return null;
		}
		
		return node;
	}
	
	/**
	 * Title syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode title(){
		ParseNode node = new ParseNode(NodeType.TITLE);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.TITLE_BEGIN){			
			//advance lexer to next token
			Compiler.lexer.lex();

			//add appropriate child nodes
			node.addChild(text());

			if(Compiler.nextToken.getTokenType() == Tokens.TokenType.TITLE_END){
				Compiler.lexer.lex();
			}else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.TITLE_END); }	
		}
		else{ 
			//EMPTY
			return null;
		}
		
		return node;
	}
	
	/**
	 * Body syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode body(){
		ParseNode node = new ParseNode(NodeType.BODY);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_BEGIN){
			//add appropriate child nodes
			node.addChild(innerText());
			node.addChild(body());
		
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.PLAIN_TEXT){
			//add appropriate child nodes
			node.addChild(innerText());
			node.addChild(body());
		
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.PARAGRAPH_BEGIN){
			//add appropriate child nodes
			node.addChild(paragraph());
			node.addChild(body());
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.BOLD_BEGIN){
			//add appropriate child nodes
			node.addChild(bold());
			node.addChild(body());
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.ITALICS_BEGIN){
			//add appropriate child nodes
			node.addChild(italics());
			node.addChild(body());
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.LIST_BEGIN){
			//add appropriate child nodes
			node.addChild(list());
			node.addChild(body());
		}
		else{
			//EMPTY
			return null;
		}
		return node;

	}
	
	/**
	 * Paragraph syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode paragraph(){
		ParseNode node = new ParseNode(NodeType.PARAGRAPH);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.PARAGRAPH_BEGIN){
			Compiler.lexer.lex();
		}else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.PARAGRAPH_BEGIN); }
		//add appropriate child nodes
		node.addChild(macroDefine());
		node.addChild(innerParagraph());
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.PARAGRAPH_END){
			Compiler.lexer.lex();
		}else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.PARAGRAPH_END); }
		
		return node;
	}
	
	/**
	 * Inner paragraph syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode innerParagraph(){
		ParseNode node = new ParseNode(NodeType.INNER_PARAGRAPH);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_BEGIN){
			//add appropriate child nodes
			node.addChild(innerText());
			node.addChild(innerParagraph());
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.PLAIN_TEXT){
			//add appropriate child nodes
			node.addChild(innerText());
			node.addChild(innerParagraph());
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.BOLD_BEGIN){
			//add appropriate child nodes
			node.addChild(bold());
			node.addChild(innerParagraph());
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.ITALICS_BEGIN){
			//add appropriate child nodes
			node.addChild(italics());
			node.addChild(innerParagraph());
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.LIST_BEGIN){
			//add appropriate child nodes
			node.addChild(list());
			node.addChild(innerParagraph());
		}
		else{
			//EMPTY
			return null;
		}
		
		return node;
	}
	
	/**
	 * Inner text syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode innerText(){
		ParseNode node = new ParseNode(NodeType.INNER_PARAGRAPH);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_BEGIN){
			//add appropriate child nodes
			node.addChild(macroUse());
			node.addChild(innerText());
		
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.PLAIN_TEXT){
			//add appropriate child nodes
			node.addChild(text());
			node.addChild(innerText());
		}
		else{
			//EMPTY
			return null;
		}
		
		return node;
	}
	
	/**
	 * Macro define syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode macroDefine(){
		ParseNode node = new ParseNode(NodeType.OUTER_DEFINE);
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.DEFINE_BEGIN){
			//add appropriate child nodes
			node.addChild(innerMacroDefine());
			node.addChild(macroDefine());
		}
		else{
			//Empty
			return null;
		}
		return node;
	}
	
	/**
	 * Inner macro define syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode innerMacroDefine(){
		ParseNode node = new ParseNode(NodeType.DEFINE);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.DEFINE_BEGIN){
			
			Compiler.lexer.lex();
			

			if(Compiler.nextToken.getTokenType() == Tokens.TokenType.NAME){
				Compiler.lexer.lex();
			} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.NAME); }
			//create a new node to hold the name text
			ParseNode nameNode = new ParseNode(NodeType.NAME);
			nameNode.addChild(text());
			//add name node to parent
			node.addChild(nameNode);

			if(Compiler.nextToken.getTokenType() == Tokens.TokenType.VALUE){
				Compiler.lexer.lex();
			} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.VALUE); }
			//create node to hold value text
			ParseNode valueNode = new ParseNode(NodeType.VALUE);
			valueNode.addChild(body());
			//add value node
			node.addChild(valueNode);

			if(Compiler.nextToken.getTokenType() == Tokens.TokenType.DEFINE_END){
				Compiler.lexer.lex();
			} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.DEFINE_END); }
			//node.addChild(macroDefine());
		}
		else{
			//EMPTY
			return null;
		}
		return node;
	}
	
	/**
	 * Macro use syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode macroUse(){
		ParseNode node = new ParseNode(NodeType.USE);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_BEGIN){
			
			Compiler.lexer.lex();
			
			//add itemNode to parent
			node.addChild(text());

			if(Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_END){
				Compiler.lexer.lex();
			} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.USE_END); }
		}
		else{
			//EMPTY
			return null;
		}
		return node;
	}
	
	/**
	 * Bold syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode bold(){
		ParseNode node = new ParseNode(NodeType.BOLD);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.BOLD_BEGIN){
			Compiler.lexer.lex();
		}else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.BOLD_BEGIN); }
		
		//add itemNode to parent
		node.addChild(macroDefine());
		node.addChild(innerText());
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.BOLD_END){
			Compiler.lexer.lex();
		}else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.BOLD_END); }
		
		return node;
	}
	
	/**
	 * Italics syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode italics(){
		ParseNode node = new ParseNode(NodeType.ITALICS);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.ITALICS_BEGIN){
			Compiler.lexer.lex();
		}else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ITALICS_BEGIN); }
		
		//add itemNode to parent
		node.addChild(macroDefine());
		node.addChild(innerText());
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.ITALICS_END){
			Compiler.lexer.lex();
		}else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ITALICS_END); }
		
		return node;
	}
	
	/**
	 * Link syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode link(){
		ParseNode node = new ParseNode(NodeType.LINK);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.LINK_BEGIN){
			Compiler.lexer.lex();
		} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.LINK_BEGIN); }

		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.TEXT){
			Compiler.lexer.lex();
		} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.TEXT); }
		
		//create new text node to hold text data
		ParseNode textNode = new ParseNode(NodeType.TEXT);
		textNode.addChild(text());
		node.addChild(textNode);

		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.ADDRESS){
			Compiler.lexer.lex();
		} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ADDRESS); }
		
		//create new address node to hold address data
		ParseNode addressNode = new ParseNode(NodeType.ADDRESS);
		addressNode.addChild(text());
		node.addChild(addressNode);

		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.LINK_END){
			Compiler.lexer.lex();
		} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.LINK_END); }
		
		return node;
	}
	
	/**
	 * List syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode list(){
		ParseNode node = new ParseNode(NodeType.LIST);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.LIST_BEGIN){
			Compiler.lexer.lex();
		} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.LIST_BEGIN); }
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.ITEM_BEGIN){
			Compiler.lexer.lex();
		} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ITEM_BEGIN); }
		
		//create a node to hold item data
		ParseNode itemNode = new ParseNode(NodeType.ITEM);
		//add children to itemNode
		itemNode.addChild(macroDefine());
		itemNode.addChild(innerList());
		//add itemNode to parent
		node.addChild(itemNode);

		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.ITEM_END){
			Compiler.lexer.lex();
		} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ITEM_END); }

		node.addChild(listItems());

		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.LIST_END){
			Compiler.lexer.lex();
		} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.LIST_END); }
		
		return node;
	}
	
	/**
	 * List items syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode listItems() {
		ParseNode node = new ParseNode(NodeType.LIST_ITEMS);
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.ITEM_BEGIN){
		
			Compiler.lexer.lex();
		
			//create a node to hold item data
			ParseNode itemNode = new ParseNode(NodeType.ITEM);
			//add children to itemNode
			itemNode.addChild(macroDefine());
			itemNode.addChild(innerList());
			//add itemNode to parent
			node.addChild(itemNode);

			if(Compiler.nextToken.getTokenType() == Tokens.TokenType.ITEM_END){
				Compiler.lexer.lex();
			} else{ setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ITEM_END); }
			
			node.addChild(listItems());
		}
		else{
			//EMPTY
			return null;
		}
		return node;
	}
	
	/**
	 * Inner list syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode innerList(){
		ParseNode node = new ParseNode(NodeType.INNER_LIST);
	
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.BOLD_BEGIN){
			//add appropriate child nodes
			node.addChild(bold());
			node.addChild(innerList());
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.ITALICS_BEGIN){
			//add appropriate child nodes
			node.addChild(italics());
			node.addChild(innerList());
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.LIST_BEGIN){
			//add appropriate child nodes
			node.addChild(list());
			node.addChild(innerList());
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_BEGIN){
			//add appropriate child nodes
			node.addChild(innerText());
			node.addChild(innerList());
		
		}
		else if(Compiler.nextToken.getTokenType() == Tokens.TokenType.PLAIN_TEXT){
			//add appropriate child nodes
			node.addChild(innerText());
			node.addChild(innerList());
		
		}
		else{
			//EMPTY
			return null;
		}
		
		return node;
	}
	
	/**
	 * Text syntax element.
	 *
	 *
	 * @return the parse tree node
	 */
	public ParseNode text(){
		ParseNode node;
		
		if(Compiler.nextToken.getTokenType() == Tokens.TokenType.PLAIN_TEXT){
			//construct a node that contains the plain text data
			node = new ParseNode(NodeType.PLAIN_TEXT, Compiler.nextToken.getTokenValue());
			//advance lexer
			Compiler.lexer.lex();
			return node;
		}
		else{
		setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.PLAIN_TEXT);
		}
		return null;
		
	}
	
	/**
	 * Sets the error message using the two passed in TokenTypes.</br>
	 * Uses the received type and expected type to build an error message that give 
	 * information about the improper tag that was found.
	 *
	 * @param recType the received type
	 * @param expType the expected type
	 * @throws SyntaxErrorException the syntax error exception
	 */
	private void setError(Tokens.TokenType recType, Tokens.TokenType expType) throws SyntaxErrorException{
		String recTag = recType.getTag();
		String expTag = expType.getTag();
		//construct a syntax error using the received tag and the expected tag.
		throw new SyntaxErrorException(recTag, expTag);
	}
	
	/**
	 * The Class SyntaxEndOfFileException. Used to throw an customized runtime exception.
	 * This is thrown when tokens are found after the document end tag.
	 */
	@SuppressWarnings("serial")
	public class SyntaxEndOfFileException extends RuntimeException {
		
		/**
		 * Instantiates a new syntax end of file exception.
		 *
		 * @param tokenValue the token value that is found after document end
		 */
		public SyntaxEndOfFileException(String tokenValue){
			super("Syntax Error: Found Token: " + tokenValue + " after closing document tag.");
		}
	}
	
	/**
	 * The Class SyntaxErrorException. Used to throw a customized runtime exception.</br>
	 * This is thrown when a tag is found that does not fit the syntax rules
	 */
	@SuppressWarnings("serial")
	public class SyntaxErrorException extends RuntimeException {
		
		/**
		 * Instantiates a new syntax error exception.
		 *
		 * @param recType the received type
		 * @param expType the expected type
		 */
		public SyntaxErrorException(String recType, String expType){
			super("Syntax Error: Found Type: " + recType + " When Expecting Type: " + expType);
		}
	}

}
