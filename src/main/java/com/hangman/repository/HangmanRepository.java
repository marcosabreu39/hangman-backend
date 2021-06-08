package com.hangman.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.hangman.model.Hangman;

/**
 * 
 * @author Marcos Abreu
 *
 */
public interface HangmanRepository extends JpaRepository<Hangman, Long>, Serializable {

	/**
	 * 
	 * @return
	 */
	@Query("SELECT h FROM Hangman h")
	List<Hangman> getAll();

	/**
	 * 
	 * @return
	 */
	@Query("SELECT h FROM Hangman h ORDER BY RAND()")
	Hangman getRandomWord(PageRequest pageRequest);
	
	/**
	 * @void
	 */
	@Transactional
	@Modifying
	@Query("update Hangman h set h.displayedLetters = null, h.allChosenLetters = null, h.preciseChosenLetters = null, h.gameCounter = 0 where h.keyWord in (select h.keyWord from Hangman h where h.displayedLetters != 'null' or h.allChosenLetters != 'null' or h.preciseChosenLetters != 'null')")
	void resetData(); 
}
