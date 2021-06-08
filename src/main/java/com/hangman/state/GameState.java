package com.hangman.state;

import com.hangman.exception.BusinessException;
import com.hangman.model.Hangman;

public interface GameState {

	Hangman processGame(Hangman hangman) throws BusinessException;
	
}
