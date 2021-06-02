package com.hangman.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
