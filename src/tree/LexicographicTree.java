package tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LexicographicTree {
	
	private Node root;
	private int size = 0;
	
	/*
	 * CONSTRUCTORS
	 */
	
	/**
	 * Constructor : creates an empty lexicographic tree.
	 */
	public LexicographicTree() {
		this.root = new Node();
	}
	
	/**
	 * Constructor : creates a lexicographic tree populated with words 
	 * @param filename A text file containing the words to be inserted in the tree 
	 */
	public LexicographicTree(String filename) {
		//1. Initialize root node
		this();
		
		//2. Load the file's content
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))) {
			String line = null;
			
			//3. For each no-empty lines, insert the word into the tree.
			while ((line = reader.readLine()) != null)   {
				insertWord(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * PUBLIC METHODS
	 */
	
	/**
	 * Returns the number of words present in the lexicographic tree.
	 * @return The number of words present in the lexicographic tree
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Inserts a word in the lexicographic tree if not already present.
	 * @param word A word
	 */
	public void insertWord(String word) {
		
		Map<Character, Node> children = root.getChildren();
        Node node;

		try {
			
			//1. Iterate over every character in the word
			for(int i = 0; i < word.length(); i++) {
	            char c = word.charAt(i);
	            
	            //2. If the children of the current node contains the current letter, set the current node to that one, else, create a new node and add it to the tree
	            if(children.containsKey(c)) {
	                node = children.get(c);
	            } else { 
	                node = new Node(c);
	                children.put(c, node);
	            }
	            children = node.getChildren();

	            //3. If the current character is the last one in the word, mark the current node as a Word (final node) and increment the size of the tree
	            if(i == word.length() - 1 && !node.isWord()) {
	                node.setWord(true);
	                this.size++;
	            }
	        }
		} catch (IllegalArgumentException e) {
			System.out.print("An error has occured while inserting a word into the Lexicographic Tree : " + e);
		}    
	}
	
	/**
	 * Determines if a word is present in the lexicographic tree.
	 * @param word A word
	 * @return True if the word is present, false otherwise
	 */
	public boolean containsWord(String word) {
		
		Map<Character, Node> children = root.getChildren();
        Node node = null;
        
        //1. Iterate over each character in the word
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            
            //2. If the current node has the current character as child, step into it, else, step out of the loop as not all the characters are in the tree
            if(children.containsKey(c)) {
                node = children.get(c);
                children = node.getChildren();
            } else { 
                node = null;
                break;
            }
        }

        //3. If the node is not null, which means that its last character was found following the order, and that the last character is marked as a Word (final node) return true, else return false.
        if(node != null && node.isWord()) {
            return true;
        } else {
            return false;
        }
	}
	
	/**
	 * Returns an alphabetic list of all words starting with the supplied prefix.
	 * If 'prefix' is an empty string, all words are returned.
	 * @param prefix Expected prefix
	 * @return The list of words starting with the supplied prefix
	 */
	public List<String> getWords(String prefix) {
		List<String> wordsFound = new ArrayList<String>();
		Map<Character, Node> children = root.getChildren();

        Node node = root;
        for(int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if(children.containsKey(c)) {
                node = children.get(c);
                children = node.getChildren();
            } else { 
                node = null;
                break;
            }
        }

        if(node != null) {
            getNodeWords(node, new StringBuilder(prefix), wordsFound);
        }
		
		return wordsFound;
	}

	/**
	 * Returns an alphabetic list of all words of a given length.
	 * If 'length' is lower than or equal to zero, an empty list is returned.
	 * @param length Expected word length
	 * @return The list of words with the given length
	 */
	public List<String> getWordsOfLength(int length) {
		List<String> wordsFound = new ArrayList<String>();
        getWordsOfSize(root, new StringBuilder(), wordsFound, length);
        return wordsFound;
	}

	/*
	 * PRIVATE METHODS
	 */
	
	private void getNodeWords(Node node, StringBuilder currentWord, List<String> wordsFound) {
		if (node.isWord()) {
            wordsFound.add(currentWord.toString());
        }

        for (Character c : node.getChildren().keySet()) {
            Node child = node.getChildren().get(c);
            currentWord.append(c);
            getNodeWords(child, currentWord, wordsFound);
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
	}
	
	private void getWordsOfSize(Node node, StringBuilder currentWord, List<String> wordsFound, int size) {
		if (currentWord.length() == size) {
            if (node.isWord()) {
                wordsFound.add(currentWord.toString());
            }
            return;
        }

        for (Character c : node.getChildren().keySet()) {
            Node child = node.getChildren().get(c);
            currentWord.append(c);
            getWordsOfSize(child, currentWord, wordsFound, size);
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
	}
	
	/*
	 * TEST FUNCTIONS
	 */
		
	private static String numberToWordBreadthFirst(long number) {
		String word = "";
		int radix = 13;
		do {
			word = (char)('a' + (int)(number % radix)) + word;
			number = number / radix;
		} while(number != 0);
		return word;
	}
	
	private static void testDictionaryPerformance(String filename) {
		long startTime;
		int repeatCount = 20;
		
		// Create tree from list of words
		startTime = System.currentTimeMillis();
		System.out.println("Loading dictionary...");
		LexicographicTree dico = null;
		for (int i = 0; i < repeatCount; i++) {
			dico = new LexicographicTree(filename);
		}
		System.out.println("Load time : " + (System.currentTimeMillis() - startTime) / 1000.0);
		System.out.println("Number of words : " + dico.size());
		System.out.println();
		
		// Search existing words in dictionary
		startTime = System.currentTimeMillis();
		System.out.println("Searching existing words in dictionary...");
		File file = new File(filename);
		for (int i = 0; i < repeatCount; i++) {
			Scanner input;
			try {
				input = new Scanner(file);
				while (input.hasNextLine()) {
				    String word = input.nextLine();
				    boolean found = dico.containsWord(word);
				    if (!found) {
				    	System.out.println(word + " / " + word.length() + " -> " + found);
				    }
				}
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Search time : " + (System.currentTimeMillis() - startTime) / 1000.0);
		System.out.println();

		// Search non-existing words in dictionary
		startTime = System.currentTimeMillis();
		System.out.println("Searching non-existing words in dictionary...");
		for (int i = 0; i < repeatCount; i++) {
			Scanner input;
			try {
				input = new Scanner(file);
				while (input.hasNextLine()) {
				    String word = input.nextLine() + "xx";
				    boolean found = dico.containsWord(word);
				    if (found) {
				    	System.out.println(word + " / " + word.length() + " -> " + found);
				    }
				}
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Search time : " + (System.currentTimeMillis() - startTime) / 1000.0);
		System.out.println();

		// Search words of increasing length in dictionary
		startTime = System.currentTimeMillis();
		System.out.println("Searching for words of increasing length...");
		for (int i = 0; i < 4; i++) {
			int total = 0;
			for (int n = 0; n <= 28; n++) {
				int count = dico.getWordsOfLength(n).size();
				total += count;
			}
			if (dico.size() != total) {
				System.out.printf("Total mismatch : dict size = %d / search total = %d\n", dico.size(), total);
			}
		}
		System.out.println("Search time : " + (System.currentTimeMillis() - startTime) / 1000.0);
		System.out.println();
	}

	private static void testDictionarySize() {
		final int MB = 1024 * 1024;
		System.out.print(Runtime.getRuntime().totalMemory()/MB + " / ");
		System.out.println(Runtime.getRuntime().maxMemory()/MB);

		LexicographicTree dico = new LexicographicTree();
		long count = 0;
		while (true) {
			dico.insertWord(numberToWordBreadthFirst(count));
			count++;
			if (count % MB == 0) {
				System.out.println(count / MB + "M -> " + Runtime.getRuntime().freeMemory()/MB);
			}
		}
	}
	
	/*
	 * MAIN PROGRAM
	 */
	
	public static void main(String[] args) {
		// CTT : test de performance insertion/recherche
		testDictionaryPerformance("mots/dictionnaire_FR_sans_accents.txt");
		
		// CST : test de taille maximale si VM -Xms2048m -Xmx2048m
		testDictionarySize();
	}
}
