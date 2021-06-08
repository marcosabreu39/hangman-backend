package com.hangman.state.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hangman.exception.BusinessException;
import com.hangman.model.Hangman;
import com.hangman.service.HangmanService;
import com.hangman.state.EndGameState;
import com.hangman.state.KeepGameRunningState;

@Component
public class GameChosenLetterLifeCycle implements KeepGameRunningState, EndGameState {

	@Autowired
	private HangmanService hangmanService;
	
	/**
	 * Clean attributes after end game
	 * 
	 * @param hangman
	 * @throws BusinessException
	 */
	private void setAttributesToEmpty(final Hangman hangman) throws BusinessException {
		hangman.setChosenLetter(null);
		hangman.setGameCounter(0);
		hangman.setDisplayedLetters(null);
		hangman.setAllChosenLetters(null);
		hangman.setPreciseChosenLetters(null);
		hangmanService.save(hangman);
	}
	
	/**
	 * When Hangman is ended with victory, sets the final message
	 * @throws BusinessException 
	 */
	@Override
	public void endWinGame(final Hangman hangman) throws BusinessException {
		hangman.setStatusGame(2);
		String[] displayLettersArr = { "*", "V", "I", "C", "T", "O", "R", "Y","!", "!", "!" };
		final List<String> displayList = Arrays.asList(displayLettersArr);
		hangman.setDisplayedLettersList(displayList);
		String[] chosenWordsArr = { "*", "M", "A", "T", "C", "H", "E", "D", "*", "W", "O", "R", "D", "S", "*" };
		final List<String> chooseList = Arrays.asList(chosenWordsArr);
		hangman.setAllChosenLettersList(chooseList);
		setAttributesToEmpty(hangman);
	}

	/**
	 * When Hangman is ended with defeat, sets the final message
	 * @throws BusinessException 
	 */
	@Override
	public void endLooseGame(final Hangman hangman) throws BusinessException {
		hangman.setStatusGame(3);
		String[] displayLettersArr = { "#", "D", "E", "F", "E", "A", "T", "!" , "!", "!", "#" };
		final List<String> displayList = Arrays.asList(displayLettersArr);
		hangman.setDisplayedLettersList(displayList);
		String[] chosenWordsArr = { "#", "E", "N", "D", "E", "D", "#", "C", "H", "A", "N", "C", "E", "S", "#" };
		final List<String> chosenList = Arrays.asList(chosenWordsArr);
		hangman.setAllChosenLettersList(chosenList);
		setAttributesToEmpty(hangman);
	}
	
	/**
	 * Updates chosen letters string
	 * 
	 * @param hangman
	 * @param keyWordLetter
	 */
	private void updatePreciseChosenLetters(final Hangman hangman, final String keyWordLetter) {
		String preciseChosenLetters = hangman.getPreciseChosenLetters() + keyWordLetter;
		hangman.setPreciseChosenLetters(preciseChosenLetters);
	}
	
	/**
	 * Verifies if Hangman game is eligible to be finished
	 * @param hangman
	 * @return
	 */
	private boolean gameEligibleToEnd(final Hangman hangman) {
		return hangman.getPreciseChosenLetters().length() >= hangman.getKeyWord().length();
	}
	
	/**
	 * Puts Hangman object in the session to be handled in the next turn
	 * @throws BusinessException 
	 */
	@Override
	public void keepRunning(final Hangman hangman) throws BusinessException {
		hangmanService.save(hangman);
	}
	
	/**
	 * Verifies if Hangman game count reached the end
	 * @param hangman
	 * @return
	 */
	private boolean gameLifeCycleNotFinished(final Hangman hangman) {
		return hangman.getGameCounter() >= 1;
	}
	
	/**
	 * Verifies if the Hangman game must be finalized or not
	 * 
	 * @param hangman
	 * @throws BusinessException 
	 */
	private void proccessGameBehavior(final Hangman hangman) throws BusinessException {
		if (gameLifeCycleNotFinished(hangman)) {
			if(gameEligibleToEnd(hangman)) {
				endWinGame(hangman);
			} else {
				keepRunning(hangman);
			}
		} else {
			endLooseGame(hangman);
		}
	}	
	
	/**
	 * 
	 * @param hangman
	 * @return
	 */
	private List<String> mountDisplayedLettersList(final Hangman hangman) {
		if (hangman.getDisplayedLettersList().isEmpty()) {
			char[] displayedLettersArray = hangman.getDisplayedLetters().toCharArray();
			List<String> displayedLettersList = new ArrayList<>();
			for (int index = 0; index < hangman.getKeyWord().length(); index++) {
				displayedLettersList.add(String.valueOf(displayedLettersArray[index]));
			}
			return displayedLettersList;
		} else {
			return hangman.getDisplayedLettersList();
		}
	}
	
	/**
	 * 
	 * @param list
	 * @param hangman
	 * @return
	 */
	private void mountDisplayedLetters(final List<String>list, final Hangman hangman) {
		list.forEach(index -> {
			hangman.setDisplayedLetters(hangman.getDisplayedLetters() + index);
		});
	}
	
	/**
	 * 
	 * @param hangman
	 * @param keyWordLetter
	 * @param iKeyWord
	 * @throws BusinessException
	 */
	private void updateDisplayedLettersList(final Hangman hangman, final String keyWordLetter, final int iKeyWord) throws BusinessException {
		List<String>displayedList = new ArrayList<>(mountDisplayedLettersList(hangman));
		displayedList.set(iKeyWord, keyWordLetter);
		hangman.setDisplayedLettersList(displayedList);
		hangman.setDisplayedLetters("");
		mountDisplayedLetters(displayedList, hangman);
	}
	
	/**
	 * Verifies if Hangman game should continue
	 * 
	 * @param hangman
	 * @return
	 */
	private boolean gameNotEnded(final Hangman hangman) {
		return hangman.getStatusGame() == 1;
	}
	
	/**
	 * Processes the Hangman game if valid yet
	 * 
	 * @param hangman
	 * @param hangmanPrior
	 * @param keyWordArray
	 * @throws BusinessException
	 */
	private void proccessRunningGame(final Hangman hangman, final Hangman hangmanPrior, final char[] keyWordArray) throws BusinessException {
		if (gameNotEnded(hangman)) {
			hangman.setDisplayedLetters(hangmanPrior.getDisplayedLetters());
			hangman.setDisplayedLettersList(hangmanPrior.getDisplayedLettersList());
			hangman.setPreciseChosenLetters(hangmanPrior.getPreciseChosenLetters());
			for (int i = 0; i < keyWordArray.length; i++) {
				if (hangman.getChosenLetter().charAt(0) == (keyWordArray[i])) {
					updatePreciseChosenLetters(hangman, String.valueOf(keyWordArray[i]));
					updateDisplayedLettersList(hangman, String.valueOf(keyWordArray[i]), i);
				} 
			}
			updateRunningStatusGame(hangman, hangmanPrior);
			proccessGameBehavior(hangman);
		}
	}
	
	/**
	 * Verifies if Hangman game status should be updated
	 * 
	 * @param hangman
	 * @param hangmanPrior
	 * @return
	 */
	private boolean statusGameShouldBeUpdated(final Hangman hangman, final Hangman hangmanPrior) {
		return hangman.getPreciseChosenLetters().length() > hangmanPrior.getPreciseChosenLetters().length();
	}
	
	/**
	 * Updates Hangman game status if needed
	 */
	@Override
	public void updateRunningStatusGame(final Hangman hangman, final Hangman hangmanPrior) {
		if (statusGameShouldBeUpdated(hangman, hangmanPrior)) {
			hangman.setUpdateImage(1);
		} else {
			hangman.setUpdateImage(2);
			hangman.setGameCounter(hangman.getGameCounter() - 1);
		}
	}
	
	/**
	 * 
	 * @param hangman
	 * @param keyWordLetter
	 */
	private void updateChosenLetters(final Hangman hangman, final Hangman hangmanPrior) throws BusinessException {
		hangmanPrior.setAllChosenLetters(hangmanPrior.getAllChosenLetters() + hangman.getChosenLetter());
		hangman.setAllChosenLetters(hangmanPrior.getAllChosenLetters());
		List<Character> chosenLettersList = hangman.getAllChosenLetters().chars().mapToObj(e -> (char) e)
				.collect(Collectors.toList());
		List<String> chsnList = new ArrayList<>();
		chosenLettersList.forEach(chsnLetter -> {
			chsnList.add(chsnLetter.toString());
		});
		hangman.setAllChosenLettersList(chsnList);
	}
	
	/**
	 * Processes Hangman game when the player puts a letter
	 */
	@Override
	public Hangman processGame(final Hangman hangman) throws BusinessException  {
		final char[] keyWordArray = hangman.getKeyWord().toCharArray();
		final Hangman hangmanPrior = hangmanService.getPriorStatusGame(hangman);
		hangman.setGameCounter(hangmanPrior.getGameCounter());
		updateChosenLetters(hangman, hangmanPrior);
		hangman.setDisplayedLettersList(mountDisplayedLettersList(hangmanPrior));
		proccessRunningGame(hangman, hangmanPrior, keyWordArray);
		
		return hangman;
	}
}
