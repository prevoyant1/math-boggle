package tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class LexicographicTree {
	
	private static final Set<Character> ALPHABET = new HashSet<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '-', '\''));
	private final Node root;
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
		
		//2. Load data from file
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8)) {

			String line = null;
			
			//3. Iterate over each line (word) and insert the word into the tree
			while ((line = reader.readLine()) != null)   {
				insertWord(line);
			}
			
		} catch (IOException e) {
			System.out.printf("An error has occured while loading words from file : %s\n%s", filename, e);
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
				
		//1. Filter if word is invalid
		if (word.isBlank() && !word.chars().allMatch(c -> ALPHABET.contains((char) c))) {
			return;
		}
		
        Node node = root;
        
        //2. Iterate through each character in the word and add it as a child node to the tree.
        for (int i = 0; i < word.length(); i++) {
            node = node.addChildNode(word.charAt(i));
        }
		
        //3. Set the current node as the end of the word and increment the trie's size if successful.
        if (node.setIsEndingWord()) {
        	this.size++;
        }
	}

	
	/**
	 * Determines if a word is present in the lexicographic tree.
	 * @param word A word
	 * @return True if the word is present, false otherwise
	 */
	public boolean containsWord(String word) {
		
		Node node = root;
		
		//1. Iterate through each character in the word and traverse the tree. If a character is not found, return false.
		for (int i = 0; i < word.length(); i++) {
		    char letter = word.charAt(i);
		    if (node.containsChild(letter)) {
		        node = node.getChild(letter);
		    } else {
		        return false;
		    }
		}
		
		//2. If the last node found is not null and an ending node, returns true, otherwise false 
		return node != null && node.isEndingNode();
	}
	
	
	/**
	 * Returns an alphabetic list of all words starting with the supplied prefix.
	 * If 'prefix' is an empty string, all words are returned.
	 * @param prefix Expected prefix
	 * @return The list of words starting with the supplied prefix
	 */
	public List<String> getWords(String prefix) {
		
		//1. Create a LinkedList to store the words found, as it is faster for insertion and we don't need access
		List<String> wordsFound = new LinkedList<String>();
		Node node = root;
		int length = prefix.length();

		//2. Traverse the trie for the given prefix; if the prefix is not found, set the node to null and break the loop
		for (int i = 0; i < length; i++) {
		    char letter = prefix.charAt(i);
		    if (node.containsChild(letter)) {
		        node = node.getChild(letter);
		    } else {
		        node = null;
		        break;
		    }
		}

		//3. If the node is not null, meaning the prefix exists in the trie, collect the words starting with the prefix
		if (node != null) {
		    getNodeWords(node, new StringBuilder(prefix), wordsFound);
		}

		//4. Return the list of words found starting with the prefix
		return wordsFound;
	}

	
	/**
	 * Returns an alphabetic list of all words of a given length.
	 * If 'length' is lower than or equal to zero, an empty list is returned.
	 * @param length Expected word length
	 * @return The list of words with the given length
	 */
	public List<String> getWordsOfLength(int length) {
		
		//1. Create a LinkedList to store the words found, as it is faster for insertion and we don't need access
		List<String> wordsFound = new LinkedList<String>();
	    //2. Traverse the trie and collect words of the specified length
        getWordsOfSize(root, new StringBuilder(), wordsFound, length);
        //3. Return the list of words found with the specified length
        return wordsFound;
	}

	/*
	 * PRIVATE METHODS
	 */
	
	private void getNodeWords(Node node, StringBuilder currentWord, List<String> wordsFound) {
	    
		//1. If the current node represents a complete word, add it to the wordsFound list (breakpoint of the recursive calls)
		if (node.isEndingNode()) {
		    wordsFound.add(currentWord.toString());
		}

		//2. Iterate through the children of the current node, if not null
		if (node.children != null) {
			for (Node childNode : node.children) {
			    if (childNode != null) {
		            //3. Append the child node's letter, traverse the tree, and remove the letter before backtracking
			        currentWord.append(childNode.getLetter());
			        getNodeWords(childNode, currentWord, wordsFound);
			        currentWord.deleteCharAt(currentWord.length() - 1);
			    }
			}
		}
	}
	
	private void getWordsOfSize(Node node, StringBuilder currentWord, List<String> wordsFound, int size) {
		
		//1. If the current word has reached the desired size and the node represents a complete word, add it to the wordsFound list (breakpoint of the recursive calls)
		if (currentWord.length() == size && node.isEndingNode()) {
		    wordsFound.add(currentWord.toString());
		    
		//2. If the current word length is less than the desired size and the node has children, continue
		} else if (currentWord.length() < size && node.children != null) {
		    for (Node childNode : node.children) {
		        if (childNode != null) {
		            //3. Append the child node's letter, traverse the trie, and remove the letter before backtracking
		            currentWord.append(childNode.getLetter());
		            getWordsOfSize(childNode, currentWord, wordsFound, size);
		            currentWord.deleteCharAt(currentWord.length() - 1);
		        }
		    }
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