package tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
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
        Node node = root;
        
        //Retrieving the length of the word to avoid calling word.length() at each iteration of the loop
        int length = word.length();
        
        for (int i = 0; i < length; i++) {
            char letter = word.charAt(i);
            node = node.addChildNode(letter);
        }
		
        if (node.setWord(true)) {
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
		int length = word.length();

		for (int i = 0; i < length; i++) {
		    char letter = word.charAt(i);
		    if (node.containsChild(letter)) {
		        node = node.getChild(letter);
		    } else {
		        return false;
		    }
		}

		return node != null && node.isWord();
	}
	
	/**
	 * Returns an alphabetic list of all words starting with the supplied prefix.
	 * If 'prefix' is an empty string, all words are returned.
	 * @param prefix Expected prefix
	 * @return The list of words starting with the supplied prefix
	 */
	public List<String> getWords(String prefix) {
		
		// LinkedList because faster for insertion and we don't need to access specified elements
		List<String> wordsFound = new LinkedList<String>();

		Node node = root;
		int len = prefix.length();

		for (int i = 0; i < len; i++) {
		    char letter = prefix.charAt(i);
		    if (node.containsChild(letter)) {
		        node = node.getChild(letter);
		    } else {
		        node = null;
		        break;
		    }
		}

		if (node != null) {
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
		List<String> wordsFound = new LinkedList<String>();
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

		for (Node childNode : node.children) {
		    if (childNode != null) {
		        currentWord.append(childNode.getLetter());
		        getNodeWords(childNode, currentWord, wordsFound);
		        currentWord.deleteCharAt(currentWord.length() - 1);
		    }
		}
	}
	
	private void getWordsOfSize(Node node, StringBuilder currentWord, List<String> wordsFound, int size) {

		if (currentWord.length() == size && node.isWord()) {
		    wordsFound.add(currentWord.toString());
		} else if (currentWord.length() < size) {
		    for (Node childNode : node.children) {
		        if (childNode != null) {
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
