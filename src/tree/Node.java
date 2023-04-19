package tree;

import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Represents a node in a LexicographicTree.
 * Each node contains a character, a flag to indicate if it is a complete word, and a TreeMap of children nodes.
 */
public class Node {
	
	private static final String ALLOWED_CHARS = "^[a-z'-]$";
    private final char character;
    private TreeMap<Character, Node> children = new TreeMap<>();
    private boolean isWord;

    /**
     * Creates a root node with an empty character.
     */
    public Node() {
    	this.character = '\0';
    }

    /**
     * Creates a new node with the specified character.
     *
     * @param character The character of the node
     */
    public Node(char character){
    	if (!Pattern.matches(ALLOWED_CHARS, Character.toString(character))) {
    		throw new IllegalArgumentException("A character found in the word is not valid (" + character + "). It must be a letter, a hyphen (-), or an apostrophe (').");
    	}
        this.character = character;
    }

    /**
     * Returns the TreeMap of children nodes for this node.
     *
     * @return The TreeMap of children nodes
     */
    public TreeMap<Character, Node> getChildren() {
        return children;
    }

    /**
     * Sets the TreeMap of children nodes for this node.
     *
     * @param children The TreeMap of children nodes
     */
    public void setChildren(TreeMap<Character, Node> children) {
        this.children = children;
    }

    /**
     * Returns true if the node represents the end of a complete word, otherwise false.
     *
     * @return True if the node represents a complete word, otherwise false
     */
    public boolean isWord() {
        return isWord;
    }

    /**
     * Sets the flag to indicate whether this node represents the end of a complete word.
     *
     * @param isWord True if the node represents a complete word, otherwise false
     */
    public void setWord(boolean isWord) {
        this.isWord = isWord;
    }
}