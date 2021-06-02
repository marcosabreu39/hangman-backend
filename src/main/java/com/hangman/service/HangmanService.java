package com.hangman.service;

import java.util.List;

import com.hangman.exception.BusinessException;
import com.hangman.model.Hangman;

/**
 * 
 * @author Marcos Abreu
 *
 */
public interface HangmanService {

	/**
	 * 
	 * @param file
	 * @throws BusinessException 
	 */
	void proccessFile(String file) throws BusinessException;

	/**
	 * 
	 * @return
	 * @throws BusinessException
	 */
	List<Hangman> getWords() throws BusinessException;

	/**
	 * 
	 * @return
	 * @throws BusinessException
	 */
	Hangman getWord() throws BusinessException;

	Hangman proccessGame(Hangman hangman) throws BusinessException;
}
