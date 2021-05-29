package Database;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Server.structure.Card;
import Server.structure.DataUser;
import Server.structure.Project;

@JsonPropertyOrder({
"users",
"prjs"
})

public class Database {
	
	private Map<String, DataUser> users;
	private LinkedList<Project> prjs = new LinkedList<Project>();
	
	@JsonCreator
	public Database(@JsonProperty("users") Map<String, DataUser> users, @JsonProperty("prjs") LinkedList<Project> prjs) {
		this.users = users;
		this.prjs = prjs;
	}
	
	public Database() {
		this.users = new HashMap<String, DataUser>();
		this.prjs = new LinkedList<Project>();
	}
	
	 /**
     * Converte il nome della lista in un intero
     *
     * @param name Nome della lista
     */
	
	private Integer NameList(String name) {
		if(name.equals("TO DO"))
			return 0;
		else if(name.equals("IN PROGRESS"))
			return 1;
		else if(name.equals("TO BE REVISED"))
			return 2;
		else if(name.equals("DONE"))
			return 3;	
		else
			return 0;
	}
	
	 /**
     * Converte il nome della lista in un intero
     *
     * @param name Nome della lista
     * @throws JsonParseException eccezzione quando non riesce a serializzare 
     * @throws JsonMappingException segnalare problemi irreversibili con la mappatura del contenuto o di decodifica/codifica
     * @throws IOException perazioni di I / O non riuscite o interrotte.
     */
	public void ReadDB() throws JsonParseException, JsonMappingException, IOException {
		
		String basedir = "db";
		File Directory = new File(basedir);
		
		//Se non esiste la cartella del db, la crea
		if(!Directory.exists()) {
			Directory.mkdir();
			
			String dirPrj = "db/prj";
			File DirPrj = new File(dirPrj);
			DirPrj.mkdir();
			return;
		}
		
		//Creo ObjecetMapper, la configuro e leggo i file
		ObjectMapper obj = new ObjectMapper();
		
		obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		obj.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		basedir = "db/users.json";
		Directory = new File(basedir);
		
		if(Directory.exists())
			this.users = obj.readValue(Directory, new TypeReference<Map<String, DataUser>>(){});
		
		//Inizio la lettura del db dei progetti
		basedir = "db/prj";
		Directory = new File(basedir);
		
		File[] FilesInDirectorys = Directory.listFiles();
			
		for (File dir : FilesInDirectorys) {
			//Se è una directory, sarà quella dei prj, inzio a leggere i progetti
            if (dir.isDirectory()) {  
            	String dir_name = dir.getName(); // ottengo il nome del progetto 
            	File[] cards = dir.listFiles(); // ottengo tutte le card
            	
    			String dirPrj = dir.getPath() +"/info.json";
    			File DirPrj = new File(dirPrj);
   			
            	Infoprj info = obj.readValue(DirPrj, Infoprj.class); //Leggo il file delle info del progetto (Se esiste il progetto, esiste sicuramente)
            	Project prj = new Project(info, dir_name); //Inizializzo il progetto
            	
            	//Inizio a leggere tutte le card associate al progetto
            	for(File card : cards) {
            		String card_name = card.getName();
            		
            		if(!card_name.equals("info.json")) {
	            		Card tmp = obj.readValue(card, Card.class);
	            		
	            		prj.addCardNoControll(tmp, NameList(tmp.ReturnLastMovement()));
            		}
            	}
            	
            	prjs.add(prj);
            }
        }
	}
	
	 /**
     * Restituisce tutti gli user
     */
	public Map<String, DataUser> ReturnUser(){
		return this.users;
	}
	
	 /**
     * Se i dati sono corretti, permette il login
     * 
     * @param nickname nome dell'utente
     * @param psw password dell'utente
     */
	public boolean Login(String nickname, String psw) {
		try {
			if(this.users.containsKey(nickname)) {
				
				DataUser userdate = this.users.get(nickname);
				
				if(userdate.getPsw().equals(userdate.cryptPsw(psw)) && !userdate.returnStatus()) {
					userdate.setLogin(true);
					return true;
				}
					
			}
			return false;
		}
		catch(Exception e){
			return false;
		}	
	}
	
	 /**
     * Permette il logout di un utente
     * 
     * @param nickname nome dell'utente
     */
	public boolean Logout(String nickname) {
		try {
			if(this.users.containsKey(nickname)) {
				
				DataUser userdate = this.users.get(nickname);
				
				userdate.setLogin(false);
				return true;	
			}
			return false;
		}
		catch(Exception e){
			return false;
		}	
	}
	
	 /**
     * Registra un utente
     * 
     * @param nickname nome dell'utente
     * @param psw password dell'utente
     */
	public boolean registerUser(String nickname, String psw) {
		try {
			if(!this.users.containsKey(nickname)) {
				
				DataUser userdate = new DataUser(psw);
				this.users.put(nickname, userdate);
				return true;
			}
			else {
				return false;
			}
		}
		catch(Exception e){
			return false;
		}		
	}
	
	 /**
     * Restituisce i dati di un utente
     * 
     * @param nickname nome dell'utente
     */
	public DataUser getUser(String nickname) {
		try {
			if(!this.users.containsKey(nickname)) {
				return users.get(nickname);
			}
			else {
				return null;
			}
		}
		catch(Exception e){
			return null;
		}
	}
	
	 /**
     * Stampa tutti gli utentei e i dati (Usata per debug)
     */
	public void PrintUser() {
		System.out.println();
		System.out.println("User:");
		
		for ( Map.Entry<String, DataUser> entry : users.entrySet() ) {
			System.out.println();
			System.out.println("Name:"+ entry.getKey());
			entry.getValue().printData();
		}
	}
	
	/**
    * Stampa tutti i progetti (Usata per debug)
    */
	public void PrintProject() {
		System.out.println();
		System.out.println("Progetti:");
		
		for (int i = 0; i<prjs.size(); i++ ) {
			Project prj = prjs.get(i);
			System.out.println();
			System.out.println("Name:" + prj.ReturnName() + " Owner:" + prj.ReturnOwner());
		}
	}
	
	/**
	 * Aggiunge un utente al progetto
	 * 
	 * @param name nome del progetto
	 * @param user membro che si vuole aggiungere
	*/
	public String addAccess(String name, String user) {
		Project prj = returnProject(name);
		
		if(!users.containsKey(user))
			return "ADDACCESS_404";
		
		if(!prj.ReturnOwner().equals(user) && prj.addAccess(user)) {
			return "ADDACCESS_505";
		}
		else {
			return "ADDACCESS_405";
		}
	}
	
	/**
	 * Rimuovere un utente al progetto
	 * 
	 * @param name nome del progetto
	 * @param user membro che si vuole rimuovere
	*/
	public boolean removeAccess(String name, String user) {
		Project prj = returnProject(name);
		
		if(!prj.ReturnOwner().equals(name))
			return prj.removeAccess(user);
		else 
			return false;
	}

	/**
	 * Funzione che dice se è presente un determinato address sui progetti
	 * 
	 * @param Address address da verificare
	*/
	private synchronized boolean ExistAddress(String Address){
		
		int j = 0;
		boolean exist = false;
		
		while(j<prjs.size() && !exist) {
			
			Project prj = prjs.get(j);
			
			if(prj.ReturnAddress().equals(Address))
				exist = true;
			
			j++;
		}
		
		return exist;
	}
	
	/**
	 * Funzione che restituisce un address che non appartiene a nessun progetto
	*/
	private synchronized String SelectAddress(){
		
		boolean trov = false;
		int i = 1;
		String addr = null;
		
		while(i<= 255 && !trov) {
			
			addr = "238.0.0." + i;
			
			if(!ExistAddress(addr))
				return addr;
				
			i++;
		}
		
		return addr;
	}
	
	 /**
     * Aggiunge un progetto
     *
     * @param name Nome del progetto
     * @param description descrizione del progetto
     * @param deadline Data entro il quale bisogna finire il progetto
     * @param owner Nome del creatore del progetto
     */
	public boolean addProject(String name, String description, String deadline, String owner) {
		
		Project prj = new Project(name, description, deadline, owner, SelectAddress());
		
		if(existProject(name))
			return false;
		
		this.prjs.add(prj);
		return true;
	}
	

	 /**
     * Eliminare un progetto
     *
     * @param name Nome del progetto
     */
	public boolean DeleteProject(String name) {
		Project prj = returnProject(name);
		
		if(prj.ReadyToDelete()) {
			return prjs.remove(prj);
		}
		return false;
	}
	
	 /**
     * Restituisce i progetti che un utente può vedere
     *
     * @param name nome dell' utente
     */
	public LinkedList<Project> showProjects(String name) {
		LinkedList<Project> tmp = new LinkedList<Project>();
		
		for(int i = 0; i<this.prjs.size(); i++) {
			Project prj = this.prjs.get(i);
			
			if(prj.hasAccess(name) || prj.ReturnOwner().equals(name))
				tmp.add(prj);
		}
		
		return tmp;
	}
	
	 /**
     * Aggiunge una card ad un progetto
     *
     * @param name Nome del progetto
     * @param card Card da inserire
     */
	public boolean AddCard(String name, Card card) {
		Project prj = returnProject(name);
		
		try {
			return prj.addCard(card, 0);
		}
		catch (Exception e) {
			return false;
		}
	}
	
	 /**
     * Restituisce un progetto
     *
     * @param name Nome del progetto
     */
	public Project returnProject(String name) {
		for(int i = 0; i<prjs.size(); i++) {
			Project prj = prjs.get(i);
			
			if(prj.ReturnName().equals(name))
				return prj;
		}
		
		return null;
	}
	
	 /**
     * Restituisce true se un progetto esiste
     *
     * @param name Nome del progetto
     */
	private boolean existProject(String name) {
		for(int i = 0; i<prjs.size(); i++) {
			Project prj = prjs.get(i);
			
			if(prj.ReturnName().equals(name))
				return true;
		}
		
		return false;
	}

	 /**
     * Restituisce una card
     *
     * @param cardname Nome della card
     * @param name Nome del progetto
     * @param where Dove è situata la card
     */
	public Card getCard(String cardname, String name, Integer where) {
		Project prj = returnProject(name);

		return prj.getCard(cardname, where);
	}
	
	 /**
     * Restituisce la lista degli utenti che possono accedere al progetto
     *
     * @param name Nome del progetto
     */
	public HashSet<String> getMembers(String name) {
		Project prj = returnProject(name);
		
		HashSet<String> tmp = prj.ReturnAccess();
		tmp.add(prj.ReturnOwner());
		return tmp;
	}
	
	 /**
     * Elimina una card da un progetto
     *
     * @param card Card da rimuovere
     * @param name Nome del progetto
     * @param list Dove si trova la card
     */
	public boolean RemoveCard(Card card, String name, Integer list) {
		Project prj = returnProject(name);
		
		switch(list) {
			case 0: 
					return	prj.removeTodo(card);
			case 1:
					return	prj.removeProgess(card);
			case 2:
					return prj.removeRevised(card);
			case 3:
					return prj.removeDone(card);
		}
		return false;
	}

	 /**
     * Converte un intero nella lista di card selezionata (per le selezioni grafiche)
     *
     * @param where Card da rimuovere
     */
	private String NameList(Integer where) {
		String [] name = {"TO DO", "IN PROGRESS","TO BE REVISED", "DONE"};
			
		return name[where];
	}
	 
	 /**
     * Sposta una card in un'altra lista
     *
     * @param cardName Nome della card che si vuole spostare
     * @param name Nome del progetto dove si trova la card
     * @param list Dove si trova la card
     * @param move Dove si vuole muovere la card
     */
	public Card MoveCard(String cardname, String name, Integer list, Integer move) { //edit
		Project prj = returnProject(name);
		
		Card card = prj.getCard(cardname, list);
		
		String moveStr = NameList(move);
		card.addMovement(moveStr);
		
		if(RemoveCard(card, name, list)) {
			if(prj.addCard(card, move)) {
				prj.sendMessage(card, moveStr);
				return card;
			}
			else
				return null;
		}
		else
			return null;
	}

}
