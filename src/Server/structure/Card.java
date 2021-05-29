package Server.structure;

import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {

	private String name;
	private String description;
	private LinkedList<History> history;
	
	@JsonCreator
	public Card(@JsonProperty("name") String name, @JsonProperty("description") String description, @JsonProperty("history") LinkedList<History> history) {
		this.name = name;
		this.description = description;
		this.history = history;
	}
	
	public Card(String name, String description) {
		this.name = name;
		this.description = description;
		this.history = new LinkedList<History>();
		History tmp = new History("TO DO");
		history.add(tmp);
	}
	
	/**
	 * Ritorna il nome della card
	*/
	public String ReturnName() {
		return this.name;
	}
	
	/**
	 * Ritorna la descrizione della card
	*/
	public String ReturnDesc() {
		return this.description;
	}
	
	/**
	 * Ritorna la cronologia dei movimenti della card
	*/
	public LinkedList<History> getHistory(){
		return this.history;
	}
	
	/**
	 * Ritorna l'ultimo movimento della cronologia
	*/
	public String ReturnLastMovement() {
		return history.getLast().ReturnWhere();
	}
	
	/**
	 * Registra un movimento alla card
	 * 
	 * @param where 
	*/
	public void addMovement(String where){
		History tmp = new History(where);
		this.history.add(tmp);
	}
}
