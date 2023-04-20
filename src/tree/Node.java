package tree;


/**
 * Represents a node in a LexicographicTree.
 * Each node contains a character, a flag to indicate if it is an ending node, and an array of children nodes.
 */
public final class Node {
	
	private static final short HYPHEN_INDEX = 26;
	private static final short APOSTROPHE_INDEX = 27;
	private static final short ALPHABET_SIZE = 28;
	
	private final char character;
    private boolean isEndingNode;
    public Node[] children;

    
    /**
     * Creates a root node with an empty character used for the root node.
     * 
     * CTT : O(1) 
     * CST : O(1)
     */
    public Node() {
    	this.character = '\0';
    }

    
    /**
     * Creates a new node with the given character.
     * 
     * CTT : O(1) 
     * CST : O(1)
     *
     * @param character The character of the new node
     */
    public Node(char character){
        this.character = character;
    }
    
    
    /**
     * Adds a Node in the children array. 
     * Lazy initialization of the array of children
     * If no Node correspond to the passed character, a new Node is created.
     * If a Node already exists with that character, the array remains unchanged.
     * 
     * CTT : O(1) 
     * CST : O(1)
     * 
     * @param character The value of the node to be inserted
     * @return The Node created or the node that contains that character
     */
    public Node addChildNode(char character) {
    	if (this.children == null) {
    		this.children = new Node[ALPHABET_SIZE];
    	}
    	
        int index = indexOf(character);

        if (children[index] == null) {
            children[index] = new Node(character);
        }

        return children[index];
    }

	
    /**
     * Checks if the current node has a child node corresponding to the given character.
     * If the children array hasn't been initialized yet, returns zero.
     * 
     * CTT : O(1)
     * CST : O(1)
     *
     * @param character The character to check for a child node
     * @return True if a child node exists for the given character, otherwise false
     */
    public boolean containsChild(char character) {
	    return this.children == null ? false : this.children[indexOf(character)] != null;
	}
    
    
    /**
     * Returns the child node corresponding to the given character.
     *
     * CTT : O(1)
     * CST : O(1)
     *
     * @param character The character to find the child node for
     * @return The child node corresponding to the given character, or null if no child node exists for the character
     */
    public Node getChild(char character) {
	    return this.children[indexOf(character)];
	}
    
    
    /**
     * Returns true if the node is an ending node, otherwise false.
     * 
     * CTT : O(1)
     * CST : O(1)
     *
     * @return True if the node is an ending node, otherwise false
     */
    public boolean isEndingNode() {
        return this.isEndingNode;
    }

    
    /**
     * Sets the 'isWord' flag of the current node to true if it is currently false.
     *
     * CTT : O(1)
     * CST : O(1)
     *
     * @return true if the 'isWord' flag was updated successfully; false if it was already set to true.
     */
    public boolean setIsEndingWord() {
    	if (this.isEndingNode) {
            return false;
        }
        this.isEndingNode = true;
        return true;
    }
    
    
    /**
     * Getter for the character (value) of a Node
     * 
     * CTT : O(1)
     * CST : O(1)
     * 
     * @return The value of the Node
     */
    public char getLetter() {
    	return this.character;
    }
    
    
    /**
     * Returns the index in the children array corresponding to the given character.
     * The index ranges from 0-25 for characters a-z, 26 for the hyphen (-), and 27 for the apostrophe (').
     * 
     * CTT : O(1)
     * CST : O(1)
     *
     * @param character The character to find the index for
     * @return The index corresponding to the given character in the children array
     */
    private int indexOf(char character) {
		if (character == '-' ) {
            return HYPHEN_INDEX;
        } else if (character == '\'') {
        	return APOSTROPHE_INDEX;
        } else {
            return character - 'a';
        }
	}
}