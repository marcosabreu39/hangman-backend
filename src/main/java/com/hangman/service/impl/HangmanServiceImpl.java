package com.hangman.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.hangman.exception.BusinessException;
import com.hangman.model.Hangman;
import com.hangman.repository.HangmanRepository;
import com.hangman.service.HangmanService;
import com.hangman.state.impl.GameChosenLetterLifeCycle;
import com.hangman.state.impl.GameSupposedWordLifeCycle;
import com.hangman.utils.ExtractionUtils;

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
	private transient ExtractionUtils utils;

	@Autowired
	private transient GameSupposedWordLifeCycle supposedWordLifeCycle;

	@Autowired
	private transient GameChosenLetterLifeCycle chosenLetterLifeCycle;
	
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
	 * 
	 * @param hangman
	 * @return
	 */
	private List<String> mountPriorDisplayedLettersList(final Hangman hangman) {
		char[] displayedLettersArray = hangman.getDisplayedLetters().toCharArray();
		List<String>displayedLettersList = new ArrayList<>();
		for (int index = 0; index < hangman.getKeyWord().length(); index++) {
			displayedLettersList.add(String.valueOf(displayedLettersArray[index]));
		}
		return displayedLettersList;
	}
	
	/**
	 * Initializes some attributes
	 * 
	 * @param hangman
	 * @throws BusinessException
	 */
	private void initializeAttributes(final Hangman hangman) throws BusinessException {
		hangman.setAllChosenLetters(ExtractionUtils.handleNullString(hangman.getAllChosenLetters()));
		
		hangman.setChosenLetter(ExtractionUtils.handleNullString(hangman.getChosenLetter()));
		
		hangman.setDisplayedLetters(ExtractionUtils.handleNullString(hangman.getDisplayedLetters()));
		
		hangman.setPreciseChosenLetters(ExtractionUtils.handleNullString(hangman.getPreciseChosenLetters()));
		
		hangman.setSupposedWord(ExtractionUtils.handleNullString(hangman.getSupposedWord()));
		
		if (ExtractionUtils.isEmptyString(hangman.getDisplayedLetters())) {
			hangman.setDisplayedLetters(initializeDisplayedLetters(hangman.getKeyWord()));
		}

		if (ExtractionUtils.isEmptyList(hangman.getDisplayedLettersList())) {
			if (!hangman.getDisplayedLetters().equals("")) {
				hangman.setDisplayedLettersList(mountPriorDisplayedLettersList(hangman));
			} else {
				hangman.setDisplayedLettersList(initializeDisplayedList(hangman.getKeyWord()));
			}
		}

		if (ExtractionUtils.isEmptyList(hangman.getAllChosenLettersList())) {
			hangman.setAllChosenLettersList(initializeDisplayedList(hangman.getKeyWord()));
		}

		if (hangman.getGameCounter() <= 0) {
			hangman.setGameCounter(hangman.getKeyWord().length());
		}
	}

	/**
	 * Obtains a random word inserted into the database.
	 * 
	 * @throws Exception
	 */
	@Override
	public Hangman getWord() throws BusinessException {
		hangmanRepository.resetData();
		final Hangman hangman = hangmanRepository.getRandomWord(PageRequest.of(0, 1));
		initializeAttributes(hangman);
		save(hangman);
		return hangman;
	}

	/**
	 * Processes the Hangman game with a word inserted by the user and verifies if
	 * match the key word
	 * 
	 * @param hangman
	 */
	@Override
	public Hangman proccessSupposedWord(Hangman hangman) throws BusinessException {
		return supposedWordLifeCycle.processGame(hangman);
	}

	/**
	 * Obtains the prior status of Hangman game
	 * 
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public Hangman getPriorStatusGame(Hangman hangman) throws BusinessException {
		Hangman hangmanPrior = hangmanRepository.findById(hangman.getKeyWordId()).get();
		initializeAttributes(hangmanPrior);
		return hangmanPrior;
	}
	
	/**
	 * Saves current game status to be used in the next turn
	 */
	@Override
	public void save(Hangman hangman) throws BusinessException {
		hangmanRepository.save(hangman);
	}
	
	/**
	 * Processes the Hangman game with a letter is inserted by the user and verifies if
	 * match with one of letters of key word
	 * 
	 * @param hangman
	 */
	@Override
	public Hangman proccessChosenLetter(Hangman hangman) throws BusinessException {
		return chosenLetterLifeCycle.processGame(hangman);
	}
}
