package Server.structure;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;

import Database.Infoprj;

/**
 *  Ogni progetto ha associate quattro liste che definiscono il flusso di
 *  lavoro come passaggio delle card da una lista alla successiva: TODO, INPROGRESS, TOBEREVISED, DONE.
 *
 * - Lista di card
 * - Chat di gruppo
 * 
 * @author Halex_
 *
 */

public class Project {

	private String name;
	private String description;
	private String owner;
	
	private LinkedList<Card> TODO;
	private LinkedList<Card> INPROGRESS;
	private LinkedList<Card> TOBEREVISED;
	private LinkedList<Card> DONE;
	
	private HashSet<String> access;
	
	private String DateCreate;
	private String Deadline;
	private String Address;
	
	public Project(Infoprj info , String name){
		this.name = name;
		this.description = info.ReturnDesc();
		this.owner = info.ReturnOwner();
		
		this.TODO = new LinkedList<Card>();
		this.INPROGRESS = new LinkedList<Card>();
		this.TOBEREVISED = new LinkedList<Card>();
		this.DONE = new LinkedList<Card>();
		
		this.access = info.ReturnAccess();
	
		this.DateCreate =  info.DateCreate();
		this.Deadline = info.ReturnDeadline();
		this.Address = info.ReturnAddress();
	}
	
	public Project(String name, String description, String deadline, String owner, String address){
		this.name = name;
		this.description = description;
		this.owner = owner;
		
		this.TODO = new LinkedList<Card>();
		this.INPROGRESS = new LinkedList<Card>();
		this.TOBEREVISED = new LinkedList<Card>();
		this.DONE = new LinkedList<Card>();
		
		this.access = new HashSet<String>();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		Date date = new Date();
		
		this.DateCreate =  formatter.format(date);
		this.Deadline = deadline;
		this.Address = address;
	}
	
	/**
	 * Ritorna true se il progetto può essere eliminato, false altrimenti
	*/
	public boolean ReadyToDelete() {

		if(this.TODO.size() == 0 && this.INPROGRESS.size() == 0 && this.TOBEREVISED.size() == 0)
			return true;
		
		return false;
	}
	
	/**
	 * Ritorna la Deadline
	*/
	public String ReturnDeadline() {
		return this.Deadline;
	}
	
	/**
	 * Ritorna la Data di creazione
	*/
	public String ReturnDatecreate() {
		return this.DateCreate;
	}
	
	/**
	 * Ritorna true se l'utente ha l'accesso al progetto, false altrimenti
	 * 
	 * @param name nome utente
	*/
	public boolean hasAccess(String name) {
		return this.access.contains(name);
	}
	
	/**
	 * Da l'accesso di operare sul proggetto all'utente 
	 * 
	 * @param name nome utente
	*/
	public boolean addAccess(String name) {

		return this.access.add(name);
	}
	
	/**
	 * Rimuove l'accesso di un utente dal progetto 
	 * 
	 * @param name nome utente
	*/
	public boolean removeAccess(String name) {
		return this.access.remove(name);
	}
	
	/**
	 * Ritorna gli accessi del progetto
	*/
	public HashSet<String> ReturnAccess() {
		return this.access;
	}
	
	/**
	 * Ritorna il nome del progetto
	*/
	public String ReturnName(){
		return this.name;
	}
	
	/**
	 * Ritorna l'owner del progetto
	*/
	public String ReturnOwner(){
		return this.owner;
	}
	
	/**
	 * Ritorna l'address del progetto
	*/
	public String ReturnAddress(){
		return this.Address;
	}
	
	/**
	 * Ritorna la descrizione del progetto
	*/
	public String ReturnDescription() {
		return this.description;
	}
	
	/**
	 * Ritorna la massima lunghezza fra le liste delle card
	*/
	public int MaxLenght() {
		int max = this.TODO.size();
		
		if(max < this.INPROGRESS.size())
			max = this.INPROGRESS.size();
		
		if(max < this.TOBEREVISED.size())
			max = this.TOBEREVISED.size();
		
		if(max < this.DONE.size())
			max = this.DONE.size();

		return max;
	}
	
	/**
	 * Ritorna true se esiste la card in una delle liste ,false altrimenti
	 * 
	 * @param  name
	*/
	public boolean existCard(String name) {
		boolean trov = false;
		int i = 0;
		int maxlenght = MaxLenght();

		while(!trov && i < maxlenght) {
			
			if(i < this.TODO.size()) {
				if(this.TODO.get(i).ReturnName().equals(name))
					trov = true;
			}
			
			if(i < this.INPROGRESS.size()) {
				if(this.INPROGRESS.get(i).ReturnName().equals(name))
					trov = true;
			}
			
			if(i < this.TOBEREVISED.size()) {
				if(this.TOBEREVISED.get(i).ReturnName().equals(name))
					trov = true;
			}

			if(i < this.DONE.size()) {
				if(this.DONE.get(i).ReturnName().equals(name))
					trov = true;
			}
				
			i++;
		}
		return trov;
	}
	
	/**
	 * Aggiunge la card nella lista selezionata
	 * 
	 * @param cards Carda da aggiungere
	 * @param chose lista selezionata
	*/
	public boolean addCard(Card cards, int chose){
		
		if(existCard(cards.ReturnName()))
			return false;
		
		if(chose == 0)
			return TODO.add(cards);
		else if(chose == 1)
			return INPROGRESS.add(cards);
		else if(chose == 2)
			return TOBEREVISED.add(cards);
		else if(chose == 3)
			return DONE.add(cards);
		else
			return false;
	}
	
	/**
	 * Aggiunge la card nella lista selezionata senza controllare se esiste già, usata per la lettura del db
	 * 
	 * @param cards Carda da aggiungere
	 * @param chose lista selezionata
	*/
	public boolean addCardNoControll(Card cards, int chose){
	
		if(chose == 0)
			return TODO.add(cards);
		else if(chose == 1)
			return INPROGRESS.add(cards);
		else if(chose == 2)
			return TOBEREVISED.add(cards);
		else if(chose == 3)
			return DONE.add(cards);
		else
			return false;
	}
	
	/**
	 * Restituisce una determinata card in una lista.
	 * 
	 * @param name nome della card
	 * @param chose lista selezionata
	*/
	public Card getCard(String name, int chose){
		if(chose == 0) {
			for(int i = 0; i<TODO.size(); i++) {
				Card tmp = TODO.get(i);
				if(tmp.ReturnName().equals(name))
					return tmp;
			}
		}
		else if(chose == 1) {
			for(int i = 0; i<INPROGRESS.size(); i++) {
				Card tmp = INPROGRESS.get(i);
				if(tmp.ReturnName().equals(name))
					return tmp;
			}
		}
		else if(chose == 2) {
			for(int i = 0; i<TOBEREVISED.size(); i++) {
				Card tmp = TOBEREVISED.get(i);
				if(tmp.ReturnName().equals(name))
					return tmp;
			}
		}
		else if(chose == 3) {
			for(int i = 0; i<DONE.size(); i++) {
				Card tmp = DONE.get(i);
				if(tmp.ReturnName().equals(name))
					return tmp;
			}
		}
		
		return null;
	}
	
	/**
	 * Restituisce le card di una lista
	 * 
	 * @param chose Lista selezionata
	*/
	public LinkedList<Card> getCards(int chose){

		switch(chose) {
		
		case 0:
			return this.TODO;
		case 1:
			return this.INPROGRESS;
		case 2:
			return this.TOBEREVISED;
		case 3:
			return this.DONE;
		}
		
		return null;
	}
	
	/**
	 * Rimuove la card passata dalla lista TODO
	 * 
	 * @param cards Card da rimuovere
	*/
	public boolean removeTodo(Card cards){
		Card tmp = getCard(cards.ReturnName(),0);
		return TODO.remove(tmp);
	}
	
	/**
	 * Rimuove la card passata dalla lista INPROGRESS
	 * 
	 * @param cards Card da rimuovere
	*/
	public boolean removeProgess(Card cards){
		Card tmp = getCard(cards.ReturnName(),1);
		return INPROGRESS.remove(tmp);
	}
	
	/**
	 * Rimuove la card passata dalla lista REVISED
	 * 
	 * @param cards Card da rimuovere
	*/
	public boolean removeRevised(Card cards){
		Card tmp = getCard(cards.ReturnName(),2);
		return TOBEREVISED.remove(tmp);
	}
	
	/**
	 * Rimuove la card passata dalla lista DONE
	 * 
	 * @param cards Card da rimuovere
	*/
	public boolean removeDone(Card cards){
		Card tmp = getCard(cards.ReturnName(),3);
		return DONE.remove(tmp);
	}

	@SuppressWarnings("deprecation")
	public void sendMessage(Card card, String moved) {
		
		MulticastSocket multicastChat;
		int port = 4079;

    	try {
    		InetAddress address = InetAddress.getByName(this.Address);
			multicastChat = new MulticastSocket(4079);
			multicastChat.joinGroup(address);
			
			String MSG = "System: card \""+ card.ReturnName() + "\" moved to " + moved ;
            DatagramPacket dat = new DatagramPacket(
                    MSG.getBytes(),
                    MSG.length(),
                    address,
                    port
            );
            
            multicastChat.send(dat);
            multicastChat.leaveGroup(address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
