package com.hangman.state;

import com.hangman.exception.BusinessException;
import com.hangman.model.Hangman;

public interface EndGameState extends GameState {

	void endWinGame(Hangman hangman) throws BusinessException;

	void endLooseGame(Hangman hangman) throws BusinessException;
	
}
