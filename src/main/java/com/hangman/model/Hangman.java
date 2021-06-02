package com.hangman.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
@Component
@Getter
@Setter
@ToString
public class Hangman {
	
	/**
	 * Primary key of Hangan class.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hangman_id_seq")
	@SequenceGenerator(name = "hangman_id_seq", sequenceName = "hangman_id_seq", allocationSize = 1)
	private Integer keyWordId;
	
	/**
	 * Key word attribute of hangman game.
	 */
	@NotNull
	@Column
	private String keyWord;
	
	
	/**
	 *  Hangmans's game counter
	 */
	
	  @Transient
	  private int gameCounter;
	 
	
	/**
	 *  Letter choose by player
	 */
	@Transient
	private String choosenLetter;
	
	/**
	 *  String representation of all choosen letters
	 */
	@Transient
	private String allChoosenLetters = "";
	
	/**
	 *  List of letters already choosen by player
	 */
	@Transient
	@JsonDeserialize(using = ListDeserializer.class)
	private List<String>allChoosenLettersList = new ArrayList<>();
	
	/**
	 *  Letters that will displayed in the game as status
	 */
	@Transient
	@JsonDeserialize(using = ListDeserializer.class)
	private List<String>displayedLettersList = new ArrayList<>();
	
	/**
	 * String that represents the game status
	 */
	@Transient
	private String displayedLetters;
	
	/**
	 *  Word that player suppose matches keyword
	 */
	@Transient
	private String supposedWord = "";
	
	/**
	 *  Game status: 1 - initialized | 2 - player won | 3 - player loose; 
	 */
	@Transient
	private int statusGame = 1;
	
	/**
	 * If true, should update the image in the game.
	 */
	@Transient
	private int updateImage = 1;
}
