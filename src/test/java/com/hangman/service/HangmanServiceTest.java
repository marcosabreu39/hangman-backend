package com.hangman.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hangman.exception.BusinessException;
import com.hangman.model.Hangman;

/**
 * Unit tests class of HangmanServiceImpl class
 * when a test fails.
 * 
 * @author Marcos Abreu
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HangmanServiceTest {

	@Autowired
	public transient HangmanService hangmanService;
	
	/**
	 * Test list for testGetWords method return comparison.	 
	 */
	private static final transient List<Hangman> TEST_WORDS_LIST = new ArrayList<>();
	
	@Test
	public void testGetWords() throws BusinessException {
		final List<Hangman>wordsList = hangmanService.getWords();
		assertNotEquals(wordsList, TEST_WORDS_LIST, "====== Unexpected empty words list! ======");
	}
}
