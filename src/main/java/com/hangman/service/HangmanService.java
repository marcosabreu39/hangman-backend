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

	/**
	 * 
	 * @param hangman
	 * @return
	 * @throws BusinessException
	 */
	Hangman proccessChosenLetter(Hangman hangman) throws BusinessException;
	
	/**
	 * 
	 * @param hangman
	 * @return
	 * @throws BusinessException
	 */
	Hangman proccessSupposedWord(Hangman hangman) throws BusinessException;

	/**
	 * 
	 * @param hangman
	 * @return
	 * @throws BusinessException
	 */
	Hangman getPriorStatusGame(Hangman hangman) throws BusinessException;

	/**
	 * 
	 * @param hangman
	 * @throws BusinessException
	 */
	void save(Hangman hangman) throws BusinessException;
	
}
