package tree;

/**
 * Represents a node in a LexicographicTree.
 * Each node contains a character, a flag to indicate if it is a complete word, and an array of children nodes.
 */
public class Node {
	
	private final char character;
	public final Node[] children = new Node[28];
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
        this.character = character;
    }
    
    
    /**
     * Adds a Node in the children array. 
     * If no Node correspond to the passed character, a new Node is created.
     * If a Node already exists with that character, the array remains unchanged.
     * 
     * @param character The value of the node to be inserted
     * @return The Node created or the node that contains that character
     */
    public Node addChildNode(char character) {
        int index = indexOf(character);

        if (children[index] == null) {
            children[index] = new Node(character);
        }

        return children[index];
    }

	
    /**
     * Checks if the current node has a child node corresponding to the given character.
     * The character should be a lowercase letter, a hyphen (-), or an apostrophe (').
     *
     * @param character The character to check for a child node
     * @return True if a child node exists for the given character, otherwise false
     */
    public boolean containsChild(char character) {
	    return this.children[indexOf(character)] != null;
	}
    
    
    /**
     * Returns the child node corresponding to the given character.
     * The character should be a lowercase letter, a hyphen (-), or an apostrophe (').
     *
     * @param character The character to find the child node for
     * @return The child node corresponding to the given character, or null if no child node exists for the character
     */
    public Node getChild(char character) {
	    return this.children[indexOf(character)];
	}
    
    
    /**
     * Returns the index in the children array corresponding to the given character.
     * The index ranges from 0-25 for characters a-z, 26 for the hyphen (-), and 27 for the apostrophe (').
     *
     * @param character The character to find the index for
     * @return The index corresponding to the given character in the children array
     */
    private int indexOf(char character) {
		if (character == '-' ) {
            return 26;
        } else if (character == '\'') {
        	return 27;
        } else {
            return character - 'a';
        }
	}
    
    
    /**
     * Returns true if the node represents the end of a complete word, otherwise false.
     *
     * @return True if the node represents a complete word, otherwise false
     */
    public boolean isWord() {
        return this.isWord;
    }

    
    /**
     * Sets the flag to indicate whether this node represents the end of a complete word.
     *
     * @param isWord True if the node represents a complete word, otherwise false
     */
    public boolean setWord(boolean isWord) {
    	if (this.isWord) {
    		return false;
    	}
        this.isWord = isWord;
        return true;
    }
    
    
    /**
     * Getter for the character (value) of a Node
     * @return The value of the Node
     */
    public char getLetter() {
    	return this.character;
    }
}