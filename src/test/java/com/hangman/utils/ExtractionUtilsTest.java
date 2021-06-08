package com.hangman.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

import com.hangman.exception.BusinessException;

/**
 * Unit tests class of Utils class, all methods should print an error message
 * when a test fails.
 * 
 * @author Marcos Abreu
 *
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ExtractionUtilsTest {

	/**
	 * Test variable for testIsNotEmptyString method.
	 */
	private static final transient String TEST_STRING = "valid!";

	/**
	 * Test variable for testExtractWords method 'extractWords method argument'.
	 */
	private static final transient String TEST_FILE_STRING = "<hangman><word_list><word>test</word><word>test2</word><word>test3</word></word_list></hangman>";

	/**
	 * Test list for testExtractWords method 'comparison with return list of extract
	 * words method'.
	 */
	private static final transient List<String> TEST_WORDS_LIST = ImmutableList.of("test1", "test2", "test3");

	/**
	 * Test method for isNotEmptyString method.
	 */

	private transient final ExtractionUtils utils;

	public ExtractionUtilsTest() {
		utils = new ExtractionUtils();
	}

	@Test
	public void testIsNotEmptyString() {
		assertTrue(ExtractionUtils.isNotEmptyString(TEST_STRING), "====== Unexpected null or empty string! =======");
	}

	@Test
	public void testExtractWords() throws BusinessException {
		final List<String> wordsList = utils.extractWords(TEST_FILE_STRING);
		assertEquals(wordsList, TEST_WORDS_LIST, "====== Unexpected unequality between indexes of these lists! ======");
	}
}
