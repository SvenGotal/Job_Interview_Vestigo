

package com.vestigo.svengotal;

import java.io.FileNotFoundException;

/**
 * <h1>Count Vowels application</h1>
 * <p>CountVowels application counts vowels from a given .txt or .xml files. 
 * The result is displayed to the standard output in terminal.</p>
 * 
 * @author Sven Gotal
 * @version 1.0
 * @since 18.09.2023
 */
public class CountVowels {

	public static void main(String[] args) {
		
		try {
			VowelCounter vc = new VowelCounter(args[0]);
			System.out.println("Found: " + vc.giveVowels() + " vowels.");
			System.out.println("Found: " + vc.giveConsonants() + " consonants.");
			
		} catch (FileNotFoundException e) {
			
			System.out.println(e.getMessage());
		}
	}

}
