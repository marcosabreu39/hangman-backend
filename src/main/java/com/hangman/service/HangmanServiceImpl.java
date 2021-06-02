package com.hangman.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.hangman.exception.BusinessException;
import com.hangman.model.Hangman;
import com.hangman.repository.HangmanRepository;
import com.hangman.utils.Utils;

/**
 * 
 * @author Marcos Abreu
 *
 */
@Component
public class HangmanServiceImpl implements Serializable, HangmanService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private transient HangmanRepository hangmanRepository;

	@Autowired
	private transient Utils utils;

	@Autowired
	private HttpSession httpSession;

	/**
	 * Extracts all words from XML file and save them into the database.
	 * 
	 * @throws Exception
	 */
	@Override
	public void proccessFile(final String file) throws BusinessException {
		final List<String> keyWords = utils.extractWords(file);
		keyWords.forEach(keyWord -> {
			final String word = utils.removeTabCharacters(keyWord);
			if (utils.allowedWord(word)) {
				final Hangman hangman = new Hangman();
				hangman.setKeyWord(word);
				hangmanRepository.save(hangman);
			}
		});
	}

	/**
	 * Obtains all Hangman game's words inserted into the database.
	 * 
	 * @throws Exception
	 */
	@Override
	public List<Hangman> getWords() throws BusinessException {
		return hangmanRepository.getAll();
	}

	/**
	 * 
	 * @param keyWord
	 * @return
	 */
	private List<String> initializeDisplayedList(final String keyWord) throws BusinessException {
		List<String> emptyList = new ArrayList<>();
		for (int i = 0; i < keyWord.length(); i++) {
			String emptyIndex = "__";
			emptyList.add(emptyIndex);
		}
		return emptyList;
	}

	/**
	 * 
	 * @param keyWord
	 * @return
	 * @throws BusinessException
	 */
	private String initializeDisplayedLetters(final String keyWord) throws BusinessException {
		String emptyString = "";
		for (int i = 0; i < keyWord.length(); i++) {
			emptyString += "__";
		}
		return emptyString;
	}

	/**
	 * Initializes some attributes
	 * 
	 * @param hangman
	 * @throws BusinessException
	 */
	private void initializeAttributes(Hangman hangman) throws BusinessException {
		hangman.setDisplayedLettersList(initializeDisplayedList(hangman.getKeyWord()));
		hangman.setDisplayedLetters(initializeDisplayedLetters(hangman.getKeyWord()));
		hangman.setAllChoosenLettersList(initializeDisplayedList(hangman.getKeyWord()));
		hangman.setGameCounter(hangman.getKeyWord().length());
	}
	
	/**
	 * Obtains a random word inserted into the database.
	 * 
	 * @throws Exception
	 */
	@Override
	public Hangman getWord() throws BusinessException {
		final Hangman hangman = hangmanRepository.getRandomWord(PageRequest.of(0, 1));
		initializeAttributes(hangman);
		httpSession.setAttribute("hangmanSession", hangman);
		return hangman;
	}

	/**
	 * Verifies if the inserted word matches key word
	 * 
	 * @param hangman
	 */
	private void checkSupposedWord(final Hangman hangman) throws BusinessException {
		if (Utils.isNotEmptyString(hangman.getSupposedWord()) && hangman.getSupposedWord().length() > 0) {
			if (hangman.getSupposedWord().equals(hangman.getKeyWord())) {
				hangman.setStatusGame(2);
			} else {
				hangman.setStatusGame(3);
				hangman.setUpdateImage(2);
			}
		}
	}

	/**
	 * 
	 * @param hangman
	 * @param keyWordLetter
	 * @param iKeyWord
	 */
	private void updateDisplayedLetters(final Hangman hangman, final String keyWordLetter,
			final int iKeyWord) throws BusinessException {
		hangman.getDisplayedLettersList().set(iKeyWord, keyWordLetter);
		hangman.getDisplayedLettersList().forEach(displayedLetter -> {
			hangman.setDisplayedLetters(hangman.getDisplayedLetters() + displayedLetter);
		});
		hangman.setDisplayedLetters(hangman.getDisplayedLettersList().toString());
	}

	/**
	 * 
	 * @param hangman
	 * @param keyWordLetter
	 */
	
	private void updateChoosenLetters(final Hangman hangman, final Hangman hangmanSession)
			throws BusinessException {
		hangmanSession.setAllChoosenLetters(hangmanSession.getAllChoosenLetters() + hangman.getChoosenLetter());
		hangman.setAllChoosenLetters(hangmanSession.getAllChoosenLetters());
		List<Character> choosenLettersList = hangman.getAllChoosenLetters().chars().mapToObj(e -> (char) e)
				.collect(Collectors.toList());
		List<String> chsnList = new ArrayList<>();
		choosenLettersList.forEach(chsnLetter -> {
			chsnList.add(chsnLetter.toString());
		});
		hangman.setAllChoosenLettersList(chsnList);
	}
	
	/**
	 * Proccesses Hangman game if valid yet
	 * 
	 * @param hangman
	 * @param hangmanSession
	 * @param keyWordArray
	 * @throws BusinessException
	 */
	private void proccessValidGame(Hangman hangman, Hangman hangmanSession, char[] keyWordArray) throws BusinessException {
		if (hangman.getStatusGame() == 1) {

			hangman.setDisplayedLetters(hangmanSession.getDisplayedLetters());
			hangman.setDisplayedLettersList(hangmanSession.getDisplayedLettersList());
			for (int i = 0; i < keyWordArray.length; i++) {
				if (hangman.getChoosenLetter().charAt(0) == (keyWordArray[i])) {
					updateDisplayedLetters(hangman, String.valueOf(keyWordArray[i]), i);
				} 
			}
			hangman.setGameCounter(hangman.getGameCounter() - 1);
			if (hangman.getGameCounter() > 1) {
				httpSession.setAttribute("hangmanSession", hangman);
			} else {
				hangman.setStatusGame(3);
				hangman.setUpdateImage(2);
			}

		}
	}
	
	/**
	 * Proccesses the end of Hangman game
	 * 
	 * @param hangman
	 * @param hangmanSession
	 */
	private void proccessEndGame(Hangman hangman, Hangman hangmanSession) {
		if (hangman.getStatusGame() == 2) {
			String[] displayLettersArr = { "*", "W", "I", "N", "N", "3", "R", "*" };
			final List<String> displayList = Arrays.asList(displayLettersArr);
			hangman.setDisplayedLettersList(displayList);
			String[] choosenWordsArr = { "*", "P", "L", "4", "Y", "*", "4", "G", "4", "I", "N", "*" };
			final List<String> chooseList = Arrays.asList(choosenWordsArr);
			hangman.setAllChoosenLettersList(chooseList);
		} else if (hangman.getStatusGame() == 3) {
			String[] displayLettersArr = { "#", "L", "0", "0", "S", "3", "R", "#" };
			final List<String> displayList = Arrays.asList(displayLettersArr);
			hangman.setDisplayedLettersList(displayList);
			String[] choosenWordsArr = { "#", "T", "R", "Y", "#", "4", "G", "4", "I", "N", "#" };
			final List<String> choosenList = Arrays.asList(choosenWordsArr);
			hangman.setAllChoosenLettersList(choosenList);
		}
	}
	
	/**
	 * Verifies if hangman image is elegible to update
	 * 
	 * @param hangman
	 * @param hangmanSession
	 */
	private void checkUpdateImg(final Hangman hangman, final Hangman hangmanSession) {
		String lettersHangman = hangman.getDisplayedLetters().replaceAll("__", "");
		String lettersHangmanSession = hangmanSession.getDisplayedLetters().replaceAll("__", "");
		if (lettersHangman.length() > lettersHangmanSession.length()) {
			hangman.setUpdateImage(2);
		} else {
			hangman.setUpdateImage(1);
		}
	}
	
	/**
	 * Proccesses the Hangman game
	 * 
	 */
	@Override
	public Hangman proccessGame(final Hangman hangman) throws BusinessException {
		final char[] keyWordArray = hangman.getKeyWord().toCharArray();
		Hangman hangmanSession = (Hangman) httpSession.getAttribute("hangmanSession");
		hangman.setDisplayedLetters(hangmanSession.getDisplayedLetters());
		hangman.setGameCounter(hangmanSession.getGameCounter());
		checkSupposedWord(hangman);
		updateChoosenLetters(hangman, hangmanSession);
		proccessValidGame(hangman, hangmanSession, keyWordArray);
		proccessEndGame(hangman, hangmanSession);
		checkUpdateImg(hangman, hangmanSession);
		
		return hangman;
	}
}
