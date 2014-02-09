package cosc.cosc455.rsanzone.project1;


import cosc.cosc455.rsanzone.project1.Tokens.TagObject;

import java.util.HashMap;


/**
 * The Class LexicalAnalyzer takes file data in the form of a string and breaks it up into valid tokens.
 *
 * @author Ronald Sanzone
 */
public class LexicalAnalyzer {

    private final char SPACE = ' ';

    private final char LINE_SEPARATOR = '\n';

    private int curPos = 0;
    private int lineNum = 1;

    private String file = "";

    private final Tokens tokens = new Tokens();


    public LexicalAnalyzer(String filecontents) {
        this.file = filecontents;
    }


    public void lex() {
        TagObject token;
        token = getToken();
        Compiler.nextToken = token;
    }

    TagObject getToken() {
        char c = getChar();
        //if c is the beginning of a tag
        if (c == '#') {
            String tag = processTag();
            if (lookup(tag) != null) {
                //look up tag in the table of known tags
                return lookup(tag);
            }
        }
        //else c is plain text
        else {
            String text = processText();
            //remove unneeded whitespace
            if (isWhiteSpace(text) && charRemaining())
                return skipWhiteSpace();
            else
                //return new TagObject containg plaintext data
                return new TagObject(text);
        }
        return null;
    }


    boolean isWhiteSpace(String s) {
        return s.trim().isEmpty();
    }


    TagObject skipWhiteSpace() {
        //moves to next token, discarding whitespace
        return getToken();
    }


    String processTag() {
        String tag = "";
        tag += getOneWord();
        //check tag against list of one word tags
        if (tokens.isOneWordTag(tag)) {
            return tag;
        }
        //get second word of tag
        else {
            tag += SPACE;
            tag += getOneWord();
            return tag;
        }
    }

    private String getOneWord() {
        String word = "";
        char c;
        //loops until entire word is stored in string
        while (true) {
            c = getChar();
            if (c == SPACE) {
                advChar();
                break;
            } else if (c == LINE_SEPARATOR) {
                lineNum++;
                advChar();
                break;
            } else {
                word += c;
                advChar();
            }
        }
        return word;
    }

    String processText() {
        String text = "";
        char c;
        //loops until start of next tag is found or file is empty
        while (charRemaining()) {
            c = getChar();
            //Beginning of next tag found
            if (c == '#') {
                break;
            } else if (c == LINE_SEPARATOR) {
                lineNum++;
                text += c;
                advChar();
            } else {
                text += c;
                advChar();
            }
        }
        return text;
    }

    void advChar() {
        curPos++;
    }


    char getChar() {
        return file.charAt(curPos);
    }


    boolean charRemaining() {
        return curPos < file.length() - 1;
    }

    private TagObject lookup(String token) {
        HashMap<String, TagObject> tokenTable = tokens.getTokenTable();
        //looks up given token in a hashmap of all known tokens
        if (tokenTable.containsKey(token.toUpperCase())) {
            return tokenTable.get(token.toUpperCase());
        } else {
            //token not found
            throw new LexicalErrorException(token);
        }
    }

    public boolean hasTailingTokens() {

        if (charRemaining()) {
            lex();
            if (!isWhiteSpace(Compiler.nextToken.getTokenValue())) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("serial")
    public class LexicalErrorException extends RuntimeException {

        public LexicalErrorException(String token) {
            super("Error: Inavlid Token: " + token + " Line: " + lineNum);
        }
    }
}