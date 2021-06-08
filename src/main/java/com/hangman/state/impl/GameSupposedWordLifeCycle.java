package com.hangman.state.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hangman.exception.BusinessException;
import com.hangman.model.Hangman;
import com.hangman.service.HangmanService;
import com.hangman.state.EndGameState;
import com.hangman.state.RunningGameState;
import com.hangman.utils.ExtractionUtils;

@Component
public class GameSupposedWordLifeCycle implements RunningGameState, EndGameState {

	@Autowired
	private HangmanService hangmanService;
	
	private void setAttributesToEmpty(Hangman hangman) throws BusinessException {
		hangman.setChosenLetter(null);
		hangman.setGameCounter(0);
		hangman.setDisplayedLetters(null);
		hangman.setAllChosenLetters(null);
		hangman.setPreciseChosenLetters(null);
		hangmanService.save(hangman);
	}
	
	@Override
	public void updateRunningStatusGame(Hangman hangman, Hangman hangmanPrior) throws BusinessException {
		if (hangman.getSupposedWord().equals(hangman.getKeyWord())) {
			endWinGame(hangman);
		} else {
			endLooseGame(hangman);
		}
	}

	@Override
	public Hangman processGame(Hangman hangman) throws BusinessException {
		if (ExtractionUtils.isNotEmptyString(hangman.getSupposedWord())) {
			updateRunningStatusGame(hangman, null);
		}
		return hangman;
	}

	@Override
	public void endWinGame(Hangman hangman) throws BusinessException {
		hangman.setStatusGame(2);
		String[] displayLettersArr = { "*", "W", "I", "N", "N", "3", "R", "!","!", "!" };
		final List<String> displayList = Arrays.asList(displayLettersArr);
		hangman.setDisplayedLettersList(displayList);
		String[] chosenWordsArr = { "*", "M", "A", "T", "C", "H", "E", "D", "*", "W", "O", "R", "D", "S", "*" };
		final List<String> chooseList = Arrays.asList(chosenWordsArr);
		hangman.setAllChosenLettersList(chooseList);
		setAttributesToEmpty(hangman);
	}

	@Override
	public void endLooseGame(Hangman hangman) throws BusinessException {
		hangman.setStatusGame(3);
		String[] displayLettersArr = { "#", "L", "0", "0", "S", "3", "R", "!" , "!", "!", "#" };
		final List<String> displayList = Arrays.asList(displayLettersArr);
		hangman.setDisplayedLettersList(displayList);
		String[] chosenWordsArr = { "#", "U", "N", "E", "Q", "U", "A", "L", "#", "W", "O", "R", "D", "S", "#" };
		final List<String> chosenList = Arrays.asList(chosenWordsArr);
		hangman.setAllChosenLettersList(chosenList);
		setAttributesToEmpty(hangman);
	}
}
