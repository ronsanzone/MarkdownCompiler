package cosc.cosc455.rsanzone.project1;

import cosc.cosc455.rsanzone.project1.ParseTree.NodeType;
import cosc.cosc455.rsanzone.project1.ParseTree.ParseNode;


/**
 * The Class SyntaxAnalyzer that uses the lexical analyzer to get tokens and
 * compare them to the syntax rules.
 *
 * @author Ronald Sanzone
 */
public class SyntaxAnalyzer {

    public void start() {
        //begins syntax analyzer at the document method
        ParseNode rootNode = document();
        //passes finished tree to global variable
        Compiler.tree.setRoot(rootNode);
    }

    public ParseNode document() {
        ParseNode node = new ParseNode(NodeType.DOCUMENT);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.DOCUMENT_BEGIN) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.DOCUMENT_BEGIN);
        }

        node.addChild(macroDefine());
        node.addChild(head());
        node.addChild(body());

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.DOCUMENT_END) {
            if (Compiler.lexer.hasTailingTokens()) {
                throw new SyntaxEndOfFileException(Compiler.nextToken.getTokenValue());
            }
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.DOCUMENT_END);
        }
        //return populated node
        return node;
    }

    public ParseNode head() {
        ParseNode node = new ParseNode(NodeType.HEAD);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.HEAD_BEGIN) {
            if (Compiler.nextToken.getTokenType() == Tokens.TokenType.HEAD_BEGIN) {
                Compiler.lexer.lex();
            } else {
                setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.HEAD_BEGIN);
            }

            //add appropriate child nodes
            node.addChild(title());

            if (Compiler.nextToken.getTokenType() == Tokens.TokenType.HEAD_END) {
                Compiler.lexer.lex();
            } else {
                setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.HEAD_END);
            }
        } else {
            //EMPTY
            return null;
        }

        return node;
    }

    public ParseNode title() {
        ParseNode node = new ParseNode(NodeType.TITLE);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.TITLE_BEGIN) {
            //advance lexer to next token
            Compiler.lexer.lex();

            //add appropriate child nodes
            node.addChild(text());

            if (Compiler.nextToken.getTokenType() == Tokens.TokenType.TITLE_END) {
                Compiler.lexer.lex();
            } else {
                setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.TITLE_END);
            }
        } else {
            //EMPTY
            return null;
        }

        return node;
    }

    public ParseNode body() {
        ParseNode node = new ParseNode(NodeType.BODY);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_BEGIN) {
            node.addChild(innerText());
            node.addChild(body());

        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.PLAIN_TEXT) {
            node.addChild(innerText());
            node.addChild(body());

        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.PARAGRAPH_BEGIN) {
            node.addChild(paragraph());
            node.addChild(body());
        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.BOLD_BEGIN) {
            node.addChild(bold());
            node.addChild(body());
        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.ITALICS_BEGIN) {
            //add appropriate child nodes
            node.addChild(italics());
            node.addChild(body());
        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.LIST_BEGIN) {
            //add appropriate child nodes
            node.addChild(list());
            node.addChild(body());
        } else {
            //EMPTY
            return null;
        }
        return node;

    }

    public ParseNode paragraph() {
        ParseNode node = new ParseNode(NodeType.PARAGRAPH);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.PARAGRAPH_BEGIN) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.PARAGRAPH_BEGIN);
        }
        node.addChild(macroDefine());
        node.addChild(innerParagraph());

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.PARAGRAPH_END) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.PARAGRAPH_END);
        }

        return node;
    }

    public ParseNode innerParagraph() {
        ParseNode node = new ParseNode(NodeType.INNER_PARAGRAPH);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_BEGIN) {
            node.addChild(innerText());
            node.addChild(innerParagraph());
        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.PLAIN_TEXT) {
            node.addChild(innerText());
            node.addChild(innerParagraph());
        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.BOLD_BEGIN) {
            node.addChild(bold());
            node.addChild(innerParagraph());
        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.ITALICS_BEGIN) {
            node.addChild(italics());
            node.addChild(innerParagraph());
        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.LIST_BEGIN) {
            node.addChild(list());
            node.addChild(innerParagraph());
        } else {
            return null;
        }

        return node;
    }

    public ParseNode innerText() {
        ParseNode node = new ParseNode(NodeType.INNER_PARAGRAPH);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_BEGIN) {
            node.addChild(macroUse());
            node.addChild(innerText());

        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.PLAIN_TEXT) {
            node.addChild(text());
            node.addChild(innerText());
        } else {
            //EMPTY
            return null;
        }

        return node;
    }

    public ParseNode macroDefine() {
        ParseNode node = new ParseNode(NodeType.OUTER_DEFINE);
        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.DEFINE_BEGIN) {
            node.addChild(innerMacroDefine());
            node.addChild(macroDefine());
        } else {
            //Empty
            return null;
        }
        return node;
    }

    public ParseNode innerMacroDefine() {
        ParseNode node = new ParseNode(NodeType.DEFINE);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.DEFINE_BEGIN) {

            Compiler.lexer.lex();


            if (Compiler.nextToken.getTokenType() == Tokens.TokenType.NAME) {
                Compiler.lexer.lex();
            } else {
                setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.NAME);
            }
            ParseNode nameNode = new ParseNode(NodeType.NAME);
            nameNode.addChild(text());
            node.addChild(nameNode);

            if (Compiler.nextToken.getTokenType() == Tokens.TokenType.VALUE) {
                Compiler.lexer.lex();
            } else {
                setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.VALUE);
            }
            ParseNode valueNode = new ParseNode(NodeType.VALUE);
            valueNode.addChild(body());
            node.addChild(valueNode);

            if (Compiler.nextToken.getTokenType() == Tokens.TokenType.DEFINE_END) {
                Compiler.lexer.lex();
            } else {
                setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.DEFINE_END);
            }
        } else {
            //EMPTY
            return null;
        }
        return node;
    }

    public ParseNode macroUse() {
        ParseNode node = new ParseNode(NodeType.USE);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_BEGIN) {

            Compiler.lexer.lex();

            node.addChild(text());

            if (Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_END) {
                Compiler.lexer.lex();
            } else {
                setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.USE_END);
            }
        } else {
            //EMPTY
            return null;
        }
        return node;
    }

    public ParseNode bold() {
        ParseNode node = new ParseNode(NodeType.BOLD);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.BOLD_BEGIN) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.BOLD_BEGIN);
        }

        node.addChild(macroDefine());
        node.addChild(innerText());

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.BOLD_END) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.BOLD_END);
        }

        return node;
    }

    public ParseNode italics() {
        ParseNode node = new ParseNode(NodeType.ITALICS);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.ITALICS_BEGIN) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ITALICS_BEGIN);
        }

        node.addChild(macroDefine());
        node.addChild(innerText());

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.ITALICS_END) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ITALICS_END);
        }

        return node;
    }

    public ParseNode link() {
        ParseNode node = new ParseNode(NodeType.LINK);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.LINK_BEGIN) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.LINK_BEGIN);
        }

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.TEXT) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.TEXT);
        }

        ParseNode textNode = new ParseNode(NodeType.TEXT);
        textNode.addChild(text());
        node.addChild(textNode);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.ADDRESS) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ADDRESS);
        }

        //create new address node to hold address data
        ParseNode addressNode = new ParseNode(NodeType.ADDRESS);
        addressNode.addChild(text());
        node.addChild(addressNode);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.LINK_END) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.LINK_END);
        }

        return node;
    }

    public ParseNode list() {
        ParseNode node = new ParseNode(NodeType.LIST);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.LIST_BEGIN) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.LIST_BEGIN);
        }

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.ITEM_BEGIN) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ITEM_BEGIN);
        }

        ParseNode itemNode = new ParseNode(NodeType.ITEM);
        itemNode.addChild(macroDefine());
        itemNode.addChild(innerList());
        node.addChild(itemNode);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.ITEM_END) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ITEM_END);
        }

        node.addChild(listItems());

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.LIST_END) {
            Compiler.lexer.lex();
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.LIST_END);
        }

        return node;
    }

    public ParseNode listItems() {
        ParseNode node = new ParseNode(NodeType.LIST_ITEMS);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.ITEM_BEGIN) {

            Compiler.lexer.lex();

            //create a node to hold item data
            ParseNode itemNode = new ParseNode(NodeType.ITEM);
            //add children to itemNode
            itemNode.addChild(macroDefine());
            itemNode.addChild(innerList());
            //add itemNode to parent
            node.addChild(itemNode);

            if (Compiler.nextToken.getTokenType() == Tokens.TokenType.ITEM_END) {
                Compiler.lexer.lex();
            } else {
                setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.ITEM_END);
            }

            node.addChild(listItems());
        } else {
            //EMPTY
            return null;
        }
        return node;
    }

    public ParseNode innerList() {
        ParseNode node = new ParseNode(NodeType.INNER_LIST);

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.BOLD_BEGIN) {
            node.addChild(bold());
            node.addChild(innerList());
        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.ITALICS_BEGIN) {
            node.addChild(italics());
            node.addChild(innerList());
        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.LIST_BEGIN) {
            node.addChild(list());
            node.addChild(innerList());
        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.USE_BEGIN) {
            node.addChild(innerText());
            node.addChild(innerList());

        } else if (Compiler.nextToken.getTokenType() == Tokens.TokenType.PLAIN_TEXT) {
            //add appropriate child nodes
            node.addChild(innerText());
            node.addChild(innerList());

        } else {
            //EMPTY
            return null;
        }

        return node;
    }

    public ParseNode text() {
        ParseNode node;

        if (Compiler.nextToken.getTokenType() == Tokens.TokenType.PLAIN_TEXT) {
            //construct a node that contains the plain text data
            node = new ParseNode(NodeType.PLAIN_TEXT, Compiler.nextToken.getTokenValue());
            //advance lexer
            Compiler.lexer.lex();
            return node;
        } else {
            setError(Compiler.nextToken.getTokenType(), Tokens.TokenType.PLAIN_TEXT);
        }
        return null;

    }

    private void setError(Tokens.TokenType recType, Tokens.TokenType expType) throws SyntaxErrorException {
        String recTag = recType.getTag();
        String expTag = expType.getTag();
        //construct a syntax error using the received tag and the expected tag.
        throw new SyntaxErrorException(recTag, expTag);
    }

    @SuppressWarnings("serial")
    public class SyntaxEndOfFileException extends RuntimeException {

        public SyntaxEndOfFileException(String tokenValue) {
            super("Syntax Error: Found Token: " + tokenValue + " after closing document tag.");
        }
    }

    @SuppressWarnings("serial")
    public class SyntaxErrorException extends RuntimeException {

        public SyntaxErrorException(String recType, String expType) {
            super("Syntax Error: Found Type: " + recType + " When Expecting Type: " + expType);
        }
    }

}
