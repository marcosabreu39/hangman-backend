package com.hangman.state;

import com.hangman.exception.BusinessException;
import com.hangman.model.Hangman;

public interface KeepGameRunningState extends RunningGameState {

	void keepRunning(Hangman hangman) throws BusinessException;
}
