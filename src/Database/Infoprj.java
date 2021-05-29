package Database;

import java.util.HashSet;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Infoprj {

	private String description;
	private String owner;
	
	private HashSet<String> access;
	
	private String DateCreate;
	private String Deadline;
	private String Address;
	
	public Infoprj(@JsonProperty("description") String description, @JsonProperty("owner") String owner, @JsonProperty("access") HashSet<String> access, @JsonProperty("DateCreate") String DateCreate, @JsonProperty("Deadline") String Deadline, @JsonProperty("Address") String Address ) {
		this.description = description;
		this.owner = owner;
		
		this.access = access;
		
		this.DateCreate = DateCreate;
		this.Deadline = Deadline;
		this.Address = Address;
	}
	
	 /**
     * Ritorna gli accessi del progetto
     */
	public HashSet<String> ReturnAccess() {
		return access;
	}
	
	 /**
     * Ritorna la descrizione del progetto
     */
	public String ReturnDesc() {
		return this.description;
	}
	
	 /**
     * Ritorna l'owner del progetto
     */
	public String ReturnOwner() {
		return this.owner;
	}
	
	/**
    * Ritorna la Data di creazione del progetto
    */
	public String DateCreate() {
		return this.DateCreate;
	}

	/**
	* Ritorna la Deadline del progetto
	*/
	public String ReturnDeadline() {
		return this.Deadline;
	}

	/**
	* Ritorna l'Address del progetto
	*/
	public String ReturnAddress() {
		return this.Address;
	}
}
