package com.hangman.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hangman.exception.BusinessException;
import com.hangman.model.Hangman;
import com.hangman.service.HangmanService;
import com.hangman.utils.Utils;

/**
 * 
 * @author Marcos Abreu
 *
 */
@RestController
public class HangmanController {

	@Autowired
	private transient HangmanService hangmanService;

	private static final Logger LOGGER = LoggerFactory.getLogger(HangmanController.class);

	private static ResponseEntity<Object> response;
	
	private static ResponseEntity<List<Object>>responseList;

	
	/**
	 * Obtains Hangman game's words saved into database.
	 * @return
	 */
	@GetMapping("/api/words")
	public ResponseEntity<List<Object>> getWords() {
		try {
			final List<Hangman> hangmanList = new ArrayList<>(hangmanService.getWords());
			if (hangmanList == null || hangmanList.isEmpty()) {
				responseList = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				final List<Object> hangmanObjectList = new ArrayList<>();
				hangmanList.forEach(hangman -> {
					final Map<String, String> hangmanMap = new ConcurrentHashMap<>();
					hangmanMap.put("keyWordId", hangman.getKeyWordId().toString());
					hangmanMap.put("keyWord", hangman.getKeyWord());
					hangmanObjectList.add(hangmanMap);
				});
				responseList = new ResponseEntity<>(hangmanObjectList, HttpStatus.OK);
			}

		} catch (final BusinessException e) {
			LOGGER.error("An error occurred obtaining hangman's words.", e);
			responseList = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseList;
	}
	
	/**
	 * Process the XML file's words sent by the client side.
	 * 
	 * @param file
	 * @return
	 */
	@PostMapping("/api/words")
	public ResponseEntity<Object> save(@RequestBody final String file) {
		try {
			if (Utils.isNotEmptyString(file)) {
				hangmanService.proccessFile(file);
				response = new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (final BusinessException e) {
			LOGGER.error("Error processing the requisition.", e);
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
	/**
	 * Obtains a random word from database and populates some initial parameters.
	 * @return
	 */
	@GetMapping("/api/word")
	public ResponseEntity<Object> getWord() {
		try {
			final Hangman hangman = hangmanService.getWord();
			if (hangman == null || hangman.getKeyWord().equals("")) {
				response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				final Map<String, String> hangmanMap = new ConcurrentHashMap<>();
				hangmanMap.put("keyWord", hangman.getKeyWord());
				hangmanMap.put("allChosenLettersList", hangman.getAllChosenLettersList().toString());
				hangmanMap.put("displayedLettersList", hangman.getDisplayedLettersList().toString());
				response = new ResponseEntity<>(hangmanMap, HttpStatus.OK);
			}

		} catch (final BusinessException e) {
			LOGGER.error("An error occurred obtaining hangman's words.", e);
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
	/**
	 * Processes the Hangman game with a chosen letter by the player
	 * 
	 * @param hangman
	 * @return
	 */
	@PostMapping("/api/letter")
	public ResponseEntity<Object> proccessChosenLetter(@RequestBody final Hangman hangman) {
			if (hangman == null || hangman.getKeyWord() == null || hangman.getKeyWord().equals("")) {
				response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				Map<String, String> hangmanMap;
				try {
					final Hangman hangmanRes = hangmanService.proccessChosenLetter(hangman);
					hangmanMap = new ConcurrentHashMap<>();
					hangmanMap.put("keyWord", hangmanRes.getKeyWord());
					hangmanMap.put("allChosenLettersList", hangman.getAllChosenLettersList().toString());
					hangmanMap.put("displayedLettersList", hangman.getDisplayedLettersList().toString());
					hangmanMap.put("statusGame", String.valueOf(hangman.getStatusGame()));
					hangmanMap.put("gameCounter", String.valueOf(hangman.getGameCounter()));
					hangmanMap.put("updateImage", String.valueOf(hangman.getUpdateImage()));
					response = new ResponseEntity<>(hangmanMap, HttpStatus.OK);
				} catch (BusinessException e) {
					LOGGER.error("An error occurred obtaining hangman's words.", e);
					response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		return response;
	}
	
	/**
	 * Processes the Hangman game with a word inserted by user supposed matches the key word
	 * 
	 * @param hangman
	 * @return
	 */
	@PostMapping("/api/word")
	public ResponseEntity<Object> proccessSupposedWord(@RequestBody final Hangman hangman) {
			if (hangman == null || hangman.getKeyWord() == null || hangman.getKeyWord().equals("")) {
				response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				Map<String, String> hangmanMap;
				try {
					final Hangman hangmanRes = hangmanService.proccessSupposedWord(hangman);
					hangmanMap = new ConcurrentHashMap<>();
					hangmanMap.put("keyWord", hangmanRes.getKeyWord());
					hangmanMap.put("allChosenLettersList", hangman.getAllChosenLettersList().toString());
					hangmanMap.put("displayedLettersList", hangman.getDisplayedLettersList().toString());
					hangmanMap.put("statusGame", String.valueOf(hangman.getStatusGame()));
					hangmanMap.put("gameCounter", String.valueOf(hangman.getGameCounter()));
					hangmanMap.put("updateImage", String.valueOf(hangman.getUpdateImage()));
					response = new ResponseEntity<>(hangmanMap, HttpStatus.OK);
				} catch (BusinessException e) {
					LOGGER.error("An error occurred obtaining hangman's words.", e);
					response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		return response;
	}
	
}
