package com.hangman.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hangman.exception.BusinessException;

/**
 * 
 * @author Marcos Abreu
 *
 */

@Component
public class ExtractionUtils {

	/**
	 * Begin of the XML file's parent node
	 */
	private static final String BEGIN_PARENT_NODE = "<word_list>";
	
	/**
	 * End of the XML file's parent node
	 */
	private static final String END_PARENT_NODE = "</word_list>";
	
	/**
	 * Begin of the XML file's WORD node
	 */
	private static final String BEGIN_WORD_NODE = "<word>";
	
	/**
	 * End of the XML file's WORD node
	 */
	private static final String END_WORD_NODE = "</word>";
	
	private static final String SLASH = "\\";
	
	/**
	 *  Letters between 4 and 20 characters, including are allowed.
	 */
	private static final String ALLOWED_PATTERN = "^[a-zA-Z]{4,20}$";
	
	/**
	 * Checks if a string isn't empty and returns true if correct.
	 *  
	 * @param string
	 * @return
	 */
	public static boolean isNotEmptyString(final String string) {
		String str = string == null ? "" : string;
		return !str.equals("") && str.length() > 0;
	}
	
	/**
	 * Checks if a string is empty and returns true if correct
	 * 
	 */
	public static boolean isEmptyString(final String string) {
		String str = string == null ? "" : string;
		return str.equals("");
	}
	
	public static boolean isEmptyList(final List<String> list) {
		List<String> lst = list == null ? new ArrayList<>() : list;
		return lst.isEmpty();
	}
	
	public static String handleNullString(String str) {
		return str == null ? "" : str;
	}
	
	/**
	 * Extracts a substring from a string.
	 * 
	 * @param fullString
	 * @param beginString
	 * @param endString
	 * @return
	 */
	private String extractSubString(final String fullString, final int beginString, final int endString) throws BusinessException {
		return fullString.substring(beginString, endString);
	}
	
	/**
	 * Extracts the parent node of word's node(s) from XML file.
	 * 
	 * @param file
	 * @return
	 * @throws BusinessException 
	 */
	private String extractParentNode(final String file) throws BusinessException {
		return extractSubString(file, file.indexOf(BEGIN_PARENT_NODE) + BEGIN_PARENT_NODE.length(), file.indexOf(END_PARENT_NODE));
	}
	
	/**
	 * Splits a string in a string array.
	 * 
	 * @param string
	 * @param regex
	 * @return
	 */
	private String[]splitString(final String string, final String regex) {
		return string.split(regex);
	}
	
	/**
	 * Removes empty indexes of an String array
	 * 
	 * @param stringArray
	 * @return
	 */
	private String[] removeEmptyIndexes(final String... stringArray) {
		return Arrays.stream(stringArray).filter(index -> index != null && index.length() > 0).toArray(size -> new String[size]);
	}

	
	/**
	 * Extracts word(s) from XML file word's node.
	 * 
	 * @param file
	 * @return
	 */
	private String[] extractNodeWords(final String file) {
		final String fileHandled = file.replace(END_WORD_NODE, "");
		final String[] wordsHandled = splitString(fileHandled, BEGIN_WORD_NODE);
		return removeEmptyIndexes(wordsHandled);
	}
	
	/**
	 * Extracts all tab characters of a string.
	 * 
	 * @param file
	 * @return
	 */
	public String removeTabCharacters(final String file) {
		return file.substring(0, file.indexOf(SLASH));
	}
	
	/**
	 * Extracts word(s) of an XML file.
	 * 
	 * @param file
	 * @return
	 * @throws BusinessException 
	 * @throws Exception 
	 */
	public List<String> extractWords(final String file) throws BusinessException {
//		final String fileNoTab = removeTabCharacters(file);
		return Arrays.asList(extractNodeWords(extractParentNode(file)));
	}
	
	/**
	 * Verifies if the word has a allowed pattern (Letters between 4 and 20 characters).
	 * 
	 * @param word
	 * @return
	 */
	public boolean allowedWord(String word) {
		return word.matches(ALLOWED_PATTERN);
	}
}
