package com.hangman.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hangman.utils.ListDeserializer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Marcos Abreu
 *
 */
@Entity
@Table(name = "hangman")
@Component
@Getter
@Setter
@ToString
public class Hangman implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Primary key of Hangan class.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hangman_id_seq")
	@SequenceGenerator(name = "hangman_id_seq", sequenceName = "hangman_id_seq", allocationSize = 1)
	private Long keyWordId;

	/**
	 * Key word attribute of hangman game.
	 */
	@NotNull
	@Column
	private String keyWord;

	/**
	 * Hangmans's game counter
	 */
	@Column
	private int gameCounter;

	/**
	 * String that represents the game status
	 */
	@Column
	private String displayedLetters;

	/**
	 * String representation of all chosen letters
	 */
	@Column
	private String allChosenLetters;

	/**
	 * Precise letters chosen by player
	 */
	@Column
	private String preciseChosenLetters;
	
	/**
	 * Letter choose by player
	 */
	@Transient
	private String chosenLetter;

	/**
	 * List of letters already chosen by player
	 */
	@Transient
	@JsonDeserialize(using = ListDeserializer.class)
	private List<String> allChosenLettersList = new ArrayList<>();

	/**
	 * Letters that will displayed in the game as status
	 */
	@Transient
	@JsonDeserialize(using = ListDeserializer.class)
	private List<String> displayedLettersList = new ArrayList<>();

	/**
	 * Word that player suppose matches keyword
	 */
	@Transient
	private String supposedWord;

	/**
	 * Game status: 1 - initialized | 2 - player won | 3 - player loose;
	 */
	@Transient
	private int statusGame = 1;

	/**
	 * If 2, Hangman's game image should be updated, if 1, not;
	 */
	@Transient
	private int updateImage = 1;
}
