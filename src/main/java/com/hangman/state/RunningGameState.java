package com.hangman.state;

import com.hangman.exception.BusinessException;
import com.hangman.model.Hangman;

public interface RunningGameState extends GameState {

	void updateRunningStatusGame(Hangman hangman, Hangman hangmanPrior) throws BusinessException;

}
