package com.vestigo.svengotal;

import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * <p>VowelCounter class is used to count vowels and consonants contained in a specified file.
 *  Currently supports *.txt and *.xml files.</p> 
 *  
 *  <h4>Usage:</h4> <p>Initialise VowelCounter with filename then use giveVowels() and giveConsonants() respectively.</p>
 *  
 *  <h4>Warning:</h4> <p>Error message will be sent to the standard output if the given filename is either misspelled or the extension
 *  is not .txt or .xml .</p> 
 */
public class VowelCounter {

	private String fileName;
	private Integer vowelCounter = 0;
	private Integer consonantCounter = 0;
	private String patternStringXML = "<[^>]*>([^<]*)</[^>]*>";
	
	// Constructors ///////////////////////////////////////////////////////////////////////
		
	/**This is a constructor for VowelCounter class.
	 * It initialises a new class with the filename parameter.
	 * 
	 * If filename parameter is not .txt or .xml it will throw FileNotFoundException.
	 *  
	 * @param filename - Name of the file that must be either .xml or .txt.
	 * @throws FileNotFoundException
	 */
	public VowelCounter(String filename) throws FileNotFoundException{
		
		Boolean fileIsXML= fileCheckXML(filename);
		Boolean fileIsTXT = fileCheckTxt(filename);
		
		if( fileIsXML || fileIsTXT ) {
			this.fileName = filename;			
		}
		else {
			System.out.println(displayUsage() + displayHelp());
			throw new FileNotFoundException("Error: File not found or not supported.");			
		}			
	}
			
	// Private methods ///////////////////////////////////////////////////////////////////////
	
	
	/**
	 * This private method will check whether the file ends with .txt.
	 * @param filename - Name of the file to check
	 * @return - Boolean.
	 */
	private Boolean fileCheckTxt(String filename) {
		return filename.toLowerCase().endsWith(".txt");
	}
	
	/**
	 * This private method will check whether the file ends with .xml.
	 * @param filename - Name of the file to check
	 * @return - Boolean.
	 */
	private Boolean fileCheckXML(String filename) {
		return filename.toLowerCase().endsWith(".xml");
	}
	
	/**
	 * This private method will use regular expression defined in the class itself in order to isolate values and exclude tags.
	 * @param line - Line from the input file.
	 * @return - returns Vector&lt;String&gt; object with stored words isolated from tags using predefined regular expression.
	 */
	private Vector<String> lineParserXML(String line){
		
		Pattern pattern = Pattern.compile(this.patternStringXML);
		Matcher match = pattern.matcher(line);
		Vector<String> isolatedWords = new Vector<String>();
		
		Boolean matchFound = null;
		String isolatedWord = null;
		
		do {
			matchFound = match.find();
			if(matchFound) {
				isolatedWord = match.group(1);
				
				if(isolatedWord != "")
					//isolatedWord.trim(); Unnecessary action?
					isolatedWords.add(isolatedWord);
			}
			
		}while(matchFound);
		
		return isolatedWords;
	}
		
	/**
	 * This private method will parse through the given String and count the number of vowels "aeiou" in the given word.
	 * @param word - String input word that will be processed.
	 * @return Integer of how many vowels were found.
	 */
	private Integer parseVowels(String word) {
		
		Integer counter = 0;
		String lowerCaseWord = word.toLowerCase();
		
		for(int i = 0; i < word.length(); ++i)
		{
			char chr = lowerCaseWord.charAt(i);
			if(chr == 'a' || chr == 'e' || chr == 'i' || chr == 'o' || chr == 'u') {
				counter++;			
			}
		}
		
		return counter;
	}
	
	/**
	 * This private method will parse through the given String and count the number of consonants in that String.
	 * @param word - String input word that will be processed.
	 * @return Integer of how many consonants were found.
	 */
	private Integer parseConsonants(String word) {
		
		Integer counter = 0;
		String lowerCaseWord = word.toLowerCase().replaceAll("[^a-z]*", "");
		
		for(int i = 0; i < lowerCaseWord.length(); ++i) {
			char chr = lowerCaseWord.charAt(i);
			if( !(chr == 'a' || chr == 'e' || chr == 'i' || chr == 'o' || chr == 'u')) {
				counter++;
			}
		}
		
		return counter;
	}
	
	/**
	 * processTXTFile will use BufferedReader and FileReader classes to read the given file. Then it will process text line by line and within every line in order to count how many vowels and consonants are in the given line.
	 * @return - returns Integer array of how many vowels and consonants were found within the given text. Integer[0] is for vowels and Integer[1] is for consonants.
	 */
	private Integer[] processTXTFile() {
		Integer[] count = {0,0};
		BufferedReader br = null;
		
		try {
			FileReader fr = new FileReader(this.fileName);
			br = new BufferedReader(fr);
			
			String line= null;
			while((line = br.readLine()) != null) {
				count[0] += parseVowels(line);
				count[1] += parseConsonants(line);
			}
			
		}
		catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		catch (IllegalStateException e) {
			System.out.println(e.getMessage());
		}
		finally {
			try {
				if(br != null) 
					br.close();
			} catch (IOException e) {				
				System.out.println(e.getMessage());
			}
		}
		
		return count;
	}
	
	/**
	 * processXMLFile() uses BufferedReader and FileReader classes to process given .xml file. It will parse through the file line by line and will count consonants and extract values between tags. These words are collected and vowels are counted.
	 * @return - gives the Integer array of how many vowels were counted within the given text. Integer[0] is for vowels and Integer[1] is for consonants.
	 */
	private Integer[] processXMLFile() {
		
		Integer[] count = {0,0};
		BufferedReader br = null;
		
		try{
			
			FileReader fr = new FileReader(this.fileName);
			br = new BufferedReader(fr);
			
			String line;
			Vector<String> words;			
			
			while((line = br.readLine()) != null) {
				
				count[1] += parseConsonants(line);
				words = lineParserXML(line);	
				
				for(String str : words) {
					count[0] += parseVowels(str);					
				}
				
			}
			
			br.close();
		}
		
		catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		catch (IllegalStateException e) {
			System.out.println(e.getMessage());
		}
		finally {
			try {
				if(br != null) 
					br.close();
			} catch (IOException e) {				
				System.out.println(e.getMessage());
			}
		}
		
		
		return count;
	}
		
	/**
	 * displays correct usage of the application
	 * @return returns the message which can then be printed onto console window or somewhere else.
	 */
	private String displayUsage() {		
		return "Usage: java -jar CountVowelsApp.jar [filename]";			
	}
	
	/**
	 * displays help info.
	 * @return returns the help message which can then be displayed wherever that is required. 
	 */
	private String displayHelp() {
		return "\nCountVowelsApp supports only .xml or .txt files. Application counts vowels and consonants in given files"
				+ " and returns the number of consonants and vowels in .txt files all vowels and consonants are counted but in"
				+ " .xml files only vowels from the values within tag elements will be counted and for consonants tag elements will be"
				+ " included.\n";	
	}
	
	// Public methods ///////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Gives the number of vowels counted by the VowelCounter object.
	 * @return - number of vowels that were counted from the given file.
	 */
	public Integer giveVowels() {
		return this.vowelCounter;
	}
	
	/**
	 * Gives the number of vowels counted by the VowelCounter object.
	 * @return - number of consonants that were counted from the given file.
	 */
	public Integer giveConsonants() {
		return this.consonantCounter;
	}
	
	/**
	 * processFile() will process the input file given in the constructor and count vowels and consonants.
	 */
	public void processFile() {
		
		Integer[] found = {0,0};
		Boolean fileIsXML= fileCheckXML(this.fileName);
		Boolean fileIsTXT = fileCheckTxt(this.fileName);
		
		if(fileIsXML){
			found = processXMLFile();
			this.vowelCounter = found[0];
			this.consonantCounter = found[1];
		}
		else if(fileIsTXT) {
			found = processTXTFile();
			this.vowelCounter = found[0];
			this.consonantCounter = found[1];
		}
	}
	
}
