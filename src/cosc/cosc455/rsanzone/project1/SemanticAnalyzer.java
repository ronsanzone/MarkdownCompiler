package cosc.cosc455.rsanzone.project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * The Class SemanticAnalyzer.
 *
 * @author Ronald Sanzone
 */
public class SemanticAnalyzer {

    /**
     * The Enum MkdType used to set the MkdObjects to a certain type.
     */
    public static enum MkdType {

        DOCUMENT_BEGIN("#DOCUMENT BEGIN", false),
        DOCUMENT_END("#DOCUMENT END", false),
        HEAD_BEGIN("#HEAD BEGIN", false),
        HEAD_END("#HEAD END", false),
        TITLE_BEGIN("#TITLE BEGIN", false),
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
        ITEM_END("#ITEM END", false),
        LINK_BEGIN("#LINK BEGIN", false),
        LINK_END("#LINK END", false),
        TEXT("#TEXT", false),
        ADDRESS("#ADDRESS", false),
        DEFINE_BEGIN("#DEFINE BEGIN", false),
        DEFINE_END("#DEFINE END", false),
        NAME("#NAME", false),
        VALUE("#VALUE", false),
        USE_BEGIN("#USE BEGIN", false),
        USE_END("#USE END", false),
        PLAIN_TEXT("", true);

        private final String tag;
        private final boolean isText;

        MkdType(String tag, boolean isText) {
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

    public static enum HtmlType {

        DOCUMENT_BEGIN("<html>", false),
        DOCUMENT_END("</html>", false),
        HEAD_BEGIN("<head>", false),
        HEAD_END("</head>", false),
        TITLE_BEGIN("<title>", false),
        TITLE_END("</title>", false),
        PARAGRAPH_BEGIN("<p>", false),
        PARAGRAPH_END("</p>", false),
        BOLD_BEGIN("<b>", false),
        BOLD_END("</b>", false),
        ITALICS_BEGIN("<i>", false),
        ITALICS_END("</i>", false),
        LIST_BEGIN("<ul>", false),
        LIST_END("</ul>", false),
        ITEM_BEGIN("<li>", false),
        ITEM_END("</li>", false),
        LINK_BEGIN("<a ", false),
        LINK_END("</a>", false),
        TEXT("\">", false),
        ADDRESS("href=\"", false),
        PLAIN_TEXT("", true);

        private final String tag;

        private final boolean isText;

        HtmlType(String tag, boolean isText) {
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

    /**
     * The Class MkdObject used as a holder of information about a type of Mkd tag or token.
     */
    public class MkdObject {

        final MkdType mkdtype;

        private final boolean istext;

        private String data = "";

        private String tag = "";

        public MkdObject(MkdType mkdtype) {
            this.mkdtype = mkdtype;
            this.tag = mkdtype.getTag();
            this.istext = mkdtype.isText;
        }

        public MkdType getType() {
            return mkdtype;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public boolean isText() {
            return istext;
        }
    }

    /**
     * The Class HtmlObject is a holder of the information needed from the html tags and text.
     */
    public class HtmlObject {

        final HtmlType htmltype;

        private final boolean istext;

        private String data = "";

        private String tag = "";

        public HtmlObject(HtmlType htmltype) {
            this.htmltype = htmltype;
            this.tag = htmltype.getTag();
            this.istext = htmltype.isText;
        }

        public HtmlType getType() {
            return htmltype;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public boolean isText() {
            return istext;
        }
    }

    /**
     * The Class ScopeKey. Used as a key value for the hashmap that is used in the macro extension
     * of the semantic analyzer
     */
    public class ScopeKey {

        private MkdObject nameValue;

        private MkdObject scopeDefined;

        private final int key;

        public ScopeKey(MkdObject nameValue, MkdObject scopeDefined) {
            this.setNameValue(nameValue);
            this.setScopeDefined(scopeDefined);
            this.key = genKey();
        }

        private int genKey() {
            //uses the string method hashCode to generate a unique code
            return (nameValue.getData() + scopeDefined.getTag()).hashCode();
        }

        public MkdObject getScopeDefined() {
            return scopeDefined;
        }

        public void setScopeDefined(MkdObject scopeDefined) {
            this.scopeDefined = scopeDefined;
        }

        public MkdObject getNameValue() {
            return nameValue;
        }

        public void setNameValue(MkdObject nameValue) {
            this.nameValue = nameValue;
        }

        public int getKey() {
            return key;
        }

    }

    private final HashMap<MkdType, HtmlObject> conversionTable;

    private final HashMap<String, MkdObject> mkdTable;

    private final ParseTree tree;

    private final ArrayList<String> beginTags;

    private final ArrayList<String> endTags;

    private Stack<MkdObject> mkdStack;

    private Stack<HtmlObject> htmlStack = new Stack<HtmlObject>();

    public SemanticAnalyzer(ParseTree tree) {
        this.tree = tree;
        this.mkdTable = populateMkdTable();
        this.conversionTable = populateConversionTable();
        this.beginTags = populateBeginList();
        this.endTags = populateEndList();
        this.mkdStack = convertStrStack(tree.stringStack());
    }

    public ParseTree getTree() {
        return tree;
    }

    public HashMap<MkdType, HtmlObject> getConversionTable() {
        return conversionTable;
    }

    public Stack<MkdObject> getMkdStack() {
        return mkdStack;
    }

    public void setMkdStack(Stack<MkdObject> mkdStack) {
        this.mkdStack = mkdStack;
    }

    public Stack<HtmlObject> getHtmlStack() {
        return htmlStack;
    }

    public void setHtmlStack(Stack<HtmlObject> htmlStack) {
        this.htmlStack = htmlStack;
    }

    public void run() {
        //translates all macro tags in mkdStack to their proper values
        mkdStack = translateMacroStack(mkdStack);
        //translate mkd to html
        translateMkdToHtml();
        //send html string to global variable
        Compiler.htmlString = htmlStackToString();
    }

    private HashMap<MkdType, HtmlObject> populateConversionTable() {
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

    private HashMap<String, MkdObject> populateMkdTable() {
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

    private ArrayList<String> populateBeginList() {
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

    private ArrayList<String> populateEndList() {
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

    private Stack<MkdObject> convertStrStack(Stack<String> strStack) {
        Stack<MkdObject> mkdStack = new Stack<MkdObject>();
        //loops untill strStack is empty
        while (!strStack.isEmpty()) {
            String curStr = strStack.pop();
            //if mkdTable has curStr, curString is a tag token
            if (mkdTable.containsKey(curStr)) {
                //look up tag and add object to stack
                MkdObject curMkdO = mkdTable.get(curStr);
                mkdStack.push(curMkdO);
            }
            //else curString is plain text
            else {
                //creates a mkdobject to hold plaintext values
                MkdObject tempobject = new MkdObject(MkdType.PLAIN_TEXT);
                tempobject.setData(curStr);
                mkdStack.push(tempobject);
            }
        }
        return mkdStack;
    }

    @SuppressWarnings("unchecked")
    private Stack<MkdObject> copyMkdStack(Stack<MkdObject> initStack) {
        return (Stack<MkdObject>) initStack.clone();
    }

    @SuppressWarnings("unchecked")
    private Stack<HtmlObject> copyHtmlStack(Stack<HtmlObject> initStack) {
        return (Stack<HtmlObject>) initStack.clone();
    }

    private Stack<MkdObject> translateMacroStack(Stack<MkdObject> initialStack) {
        HashMap<Integer, MkdObject> nameValueMap = new HashMap<Integer, MkdObject>();
        Stack<MkdObject> finalStack = new Stack<MkdObject>();

        Stack<MkdObject> remainingScopes = new Stack<MkdObject>();


        while (!initialStack.isEmpty()) {
            //next object is a beginning tag
            if (beginTags.contains(initialStack.peek().getTag())) {
                MkdObject temp = initialStack.pop();
                //add the begin tag to the stack of scopes
                remainingScopes.push(temp);
                //push object on translated stack
                finalStack.push(temp);
            }
            //next object is end tag
            else if (endTags.contains(initialStack.peek().getTag())) {
                //push object on translated stack
                finalStack.push(initialStack.pop());
                //pop off last added scope
                remainingScopes.pop();
            }
            //next object is define tag
            else if (initialStack.peek().getType() == MkdType.DEFINE_BEGIN) {
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
            }
            //next object is use tag
            else if (initialStack.peek().getType() == MkdType.USE_BEGIN) {
                //pop off unneeded use begin tag
                initialStack.pop();
                //store name value
                MkdObject name = initialStack.pop();
                //check all of the scopes to see if there is a definition of the name value
                MkdObject value = checkAllScopes(name, remainingScopes, nameValueMap);
                //definition exists
                if (value != null) {
                    //push stack onto translated stack
                    finalStack.push(value);
                } else {
                    //error
                    throw new SemanticException(name);
                }
                initialStack.pop();
            }
            //next object is plain text
            else {
                finalStack.push(initialStack.pop());
            }
        }
        return finalStack;
    }

    MkdObject checkAllScopes(MkdObject name, Stack<MkdObject> scopes, HashMap<Integer, MkdObject> nameValues) {
        @SuppressWarnings("unchecked")
        Stack<MkdObject> clonedScopes = (Stack<MkdObject>) scopes.clone();
        while (!clonedScopes.isEmpty()) {
            MkdObject tScope = clonedScopes.pop();
            //make a new scope key
            ScopeKey tKey = new ScopeKey(name, tScope);
            //check for definition in current scope
            if (nameValues.containsKey(tKey.getKey())) {
                return nameValues.get(tKey.getKey());
            }
        }
        return null;
    }

    private void translateMkdToHtml() {
        Stack<MkdObject> tempMkdS = copyMkdStack(mkdStack);
        while (!tempMkdS.isEmpty()) {
            MkdObject tempMkdO = tempMkdS.pop();
            //tempMkdO is plain text
            if (tempMkdO.isText()) {
                //build new html object
                HtmlObject textObject = new HtmlObject(HtmlType.PLAIN_TEXT);
                textObject.setData(tempMkdO.getData());
                htmlStack.push(textObject);
            }
            //tempMkdO is tag
            else {
                //convert mkd to html object and push to stack
                HtmlObject tempHtmlO = conversionTable.get(tempMkdO.getType());
                htmlStack.push(tempHtmlO);
            }
        }

    }

    private String htmlStackToString() {
        Stack<HtmlObject> tempHtmlStack = copyHtmlStack(htmlStack);
        String htmlString = "";
        while (!tempHtmlStack.isEmpty()) {
            //htmlobject is plaintext
            if (tempHtmlStack.peek().isText()) {
                //append object data
                htmlString += tempHtmlStack.pop().getData();
            }
            //object is tag object
            else {
                //append object tag
                htmlString += tempHtmlStack.pop().getTag();
            }
        }
        return htmlString;
    }

    @SuppressWarnings("serial")
    public class SemanticException extends RuntimeException {

        /**
         * Instantiates a new semantic exception.
         *
         * @param name the name of the use macro that is causing the error
         */
        public SemanticException(MkdObject name) {
            super("Semantic Error: No definition found for macro name: " + name.getData());
        }
    }
}
