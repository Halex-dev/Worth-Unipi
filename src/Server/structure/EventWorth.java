package Server.structure;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultListModel;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import Client.MemberClientInt;
import Database.Database;
import Database.Infoprj;

public class EventWorth extends RemoteServer implements EventWorthInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2833711015223961086L;
	
	//Client per la callback
	@SuppressWarnings("rawtypes")
	private Vector clientList;
	private Database db = new Database();
	
	@SuppressWarnings("rawtypes")
	public EventWorth() {
		try {
			this.db.ReadDB();
			this.clientList = new Vector();
			System.out.println("DB ready");
		}
		catch(Exception e){
	    	System.out.println("Error exception: \n"+ e);
	    }
	}

	/**
	 * Restituisce l'address di un progetto
	 * 
	 * @param name Nome del progetto
	*/
	public String JoinChat(String name){
		
		Project prj = db.returnProject(name);
		
		String address = prj.ReturnAddress();
		return address;
	}
	
	/**
	 * Fa la Callback ai client registrati
	 * 
	 * @throws RemoteException se si verificano durante l'esecuzione della chiamata remota
	*/
	private void listUsers() throws RemoteException{	 
		 DefaultListModel<String> model = new DefaultListModel<String>();
		 
		 Map<String, DataUser> users = db.ReturnUser();
		 
         if(users.size() > 0) {
	         for(Map.Entry<String, DataUser> entry : users.entrySet()) {
	        	    String key = entry.getKey();
	        	    DataUser value = entry.getValue();
	        	    String str;
	        	    
	        	    if(value.returnStatus())
	        	    	str = "online";
	        	    else
	        	    	str = "offline";
	        	    	
	        	    model.addElement(key + " (" + str +")");
	         }
         } 
         
		 for (int i = 0; i < clientList.size(); i++){
			 try {
				 MemberClientInt nextClient = (MemberClientInt) clientList.elementAt(i);     
			 	 nextClient.callback(model);
			 }
			 catch(Exception e) {
				 clientList.removeElementAt(i);
			 }
		 }
	}
	
	/**
	 * Recupera gli utenti online
	 * 
	 * @throws RemoteException se si verificano durante l'esecuzione della chiamata remota
	*/
	@SuppressWarnings("unused")
	private DefaultListModel<String> listOnlineusers() throws RemoteException{
		 
		DefaultListModel<String> model = new DefaultListModel<String>();
		 
		Map<String, DataUser> users = db.ReturnUser();
		 
        if(users.size() > 0) {
	         for(Map.Entry<String, DataUser> entry : users.entrySet()) {
	        	    String key = entry.getKey();
	        	    DataUser value = entry.getValue();
	        	    
	        	    if(value.returnStatus()) {
	        	    	model.addElement(key);
	        	    }	    		        	    
	         }
        } 
        
       return model;
	}
	
	/**
	 * Cancella la registrazione di un client alla callback
	 * 
	 * @throws RemoteException se si verificano durante l'esecuzione della chiamata remota
	*/
	public void unregisterCallback(MemberClientInt client_ref) throws RemoteException{
		 
		if(clientList.removeElement(client_ref)) {
			System.out.println("Unregistered client for RMI");
		} 
		else {
			System.out.println("unregister: client wasn't registered.");
		}
	}
	
	/**
    * Registra un utente alla Callback
    *
    * @throws RemoteException se si verificano durante l'esecuzione della chiamata remota
    */
	@SuppressWarnings("unchecked")
	public void registerCallback(MemberClientInt client) throws RemoteException{
		if (!clientList.contains(client)) {
			clientList.addElement(client);
			System.out.println("Registered client for RMI");
		}
	}
	
    /**
    * Registra l'utente
    *
    * @param nickname Nome dell'utente
    * @param psw Password dell'utente
    * @throws RemoteException se si verificano durante l'esecuzione della chiamata remota
    * @throws IllegalArgumentException se l'username o la password non è valida
    */
    public boolean register(String nickname ,String psw) throws IllegalArgumentException, RemoteException {
	     
        if (nickname.length() < 4)
            throw new IllegalArgumentException("Nome non valido");
        
        if (psw.length() < 4)
            throw new IllegalArgumentException("Password non valida");
        
        if(db.registerUser(nickname, psw)) {
        	saveUser();
        	listUsers();
        	return true;
        }
        return false;
    }

    /**
    * Restituisce true se le credenziali dell'utente sono giuste per il login, false altrimenti
    *
    * @param nickname Nome dell'utente
    * @param psw Password dell'utente
    */
	public boolean Login(String nickname ,String psw){
     
        if(db.Login(nickname, psw)) {
        	try {
        		listUsers();
				saveUser();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	return true;
        }
        else
        	return false;
    }
    
    /**
    * Esegue le callback aggiornando gli utenti online e disconnettendo l'utente
    *
    * @param nickname Nome dell'utente
    */
	public boolean Logout(String nickname) {
		if(db.Logout(nickname)) {
			try {
				listUsers();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return true;
		}
    	return false;
	}
    
	 /**
     * Crea un nuovo progetto
     *
     * @param name Nome del progetto
     * @param description descrizione del progetto
     * @param deadline Data entro il quale bisogna finire il progetto
     * @param owner Nome del creatore del progetto
     */
    public boolean CreateProject(String name ,String description, String deadline, String owner){
	            
        if(db.addProject(name, description, deadline, owner)) {
        	savePrj(name);
        	return true;
        }
        else
        	return false;
    }

	 /**
     * Elimina un progetto
     *
     * @param name Nome del progetto
     */
    public boolean cancelProject(String name){
        
        if(db.DeleteProject(name)) {
        	cancelProjectFolder(name);
        	return true;
        }
        else
        	return false;
    }

	 /**
     * Restituisce i progetti che un utente può vedere
     *
     * @param nickname Nome dell'utente
     */
	public LinkedList<Project> listProjects(String nickname){	           
        return db.showProjects(nickname);
    }
    
	 /**
     * Crea una card su un progetto inserendola su TODO
     *
     * @param cardname Nome della card che si vuole creare
     * @param description descrizione della card
     * @param namePrj Nome del progetto dove si vuole aggiungere la card
     */
	public boolean AddCard(String cardname, String description, String namePrj) {
		Card card = new Card(cardname, description);
		
		if(db.AddCard(namePrj, card)) {
			savePrjCard(namePrj, card);
			return true;
		}		
		else {
			return false;
		}
	}
	
	 /**
     * Restituisce una determinata card
     *
     * @param name Nome del progetto dove si trova la card
     * @param cardName Nome della card
     * @param where Dove si trova la card
     */
	public Card showCard(String cardname, String name, Integer where) {
		return db.getCard(cardname, name, where);
	}
	
	 /**
     * Restituisce le card di un progetto su una lista
     *
     * @param namePrj Nome del progetto
     * @param chose Lista delle card da vedere
     */
	public LinkedList<Card> showCards(String namePrj, Integer chose) {
		Project prj = this.showPrj(namePrj);
		
		return prj.getCards(chose);
	}
  
	 /**
     * Restituisce la history di una card
     *
     * @param namePrj Nome del progetto
     * @param chose Lista delle card da vedere
     */
	public LinkedList<History> getCardHistory(String cardName, String namePrj,Integer where) {
  
    	Card card = this.showCard(cardName, namePrj, where);
    	
    	return card.getHistory();
	}

	 /**
     * Elimina una card da un progetto
     *
     * @param description della card
     * @param cardName Nome della card
     * @param namePrj Nome del progetto dove si trova la card
     * @param list Dove si trova la card
     */
	public boolean RemoveCard(String cardname, String description, String namePrj, Integer list) {
		Card card = new Card(cardname, description);
		
		return db.RemoveCard(card, namePrj, list);
	}
	
	 /**
     * Sposta una card da una lista ad un altra
     *
     * @param card Card da rimuovere
     * @param name Nome del progetto
     * @param list Dove si trova la card
     */
	public boolean MoveCard(String cardname, String namePrj, Integer list, Integer move) {	
		Card card = db.MoveCard(cardname, namePrj, list, move);
		
		if(card != null) {
			savePrjCard(namePrj, card);
			return true;
		}		
		else {
			return false;
		}
	}
	
	
	/**
    * Restituisce la lista degli utenti che possono accedere al progetto
    *
    * @param name Nome del progetto
    */
	public HashSet<String> showMembers(String name) {
		return db.getMembers(name);
	}
	
	 /**
     * Restituisce un progetto
     *
     * @param name Nome del progetto
     */
    public Project showPrj(String name){    
        return db.returnProject(name);
    }
    
	/**
	 * Aggiunge un utente al progetto
	 * 
	 * @param name nome del progetto
	 * @param user membro che si vuole aggiungere
	*/
	public String addMember(String namePrj, String user) {
		String response = db.addAccess(namePrj, user);
		
		if(response.equals("ADDACCESS_505"))
			savePrj(namePrj);
		
		return response;
	}

	/**
	 * Rimuovere un utente al progetto
	 * 
	 * @param name nome del progetto
	 * @param user membro che si vuole rimuovere
	*/
	public boolean removeAccess(String name, String user) {
		return db.removeAccess(name, user);
	}

	/**
	 * Salva gli utenti
	*/
    private void saveUser() {
		try{
			
			String userFile = "db/users.json";
			File UserFile = new File(userFile);		
			ObjectMapper map = new ObjectMapper();
			
			map.configure(SerializationFeature.INDENT_OUTPUT, true);
			map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			map.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			
			if(!UserFile.exists())
				UserFile.createNewFile();
			
			map.writeValue(UserFile, db.ReturnUser()); 
		} catch (Exception e) {
			System.out.println("Error exception: \n"+ e);
		}
	}
    
	/**
	 * Salva un determinato progetto
	 * 
	 * @param name Nome del progetto
	*/
    private void savePrj(String name) {
		try{
			
			String basedir = "db/prj/" + name;
			File Directory = new File(basedir);	
			
			String infoprj = "db/prj/" + name +"/info.json";
			File infoFile = new File(infoprj);	
			ObjectMapper map = new ObjectMapper();
			
			map.configure(SerializationFeature.INDENT_OUTPUT, true);
			map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			map.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			
			if(!Directory.exists())
				Directory.mkdir();
			
			Project tmp = db.returnProject(name);
			
			Infoprj info = new Infoprj(tmp.ReturnDescription(), tmp.ReturnOwner(), tmp.ReturnAccess(), tmp.ReturnDatecreate(), tmp.ReturnDeadline(), tmp.ReturnAddress());
			

			map.writeValue(infoFile, info); 
		} catch (Exception e) {
			System.out.println("Error exception: \n"+ e);
		}
	}
    
	/**
	 * Elimina un progetto
	 * 
	 * @param name Nome del progetto
	*/
	private void cancelProjectFolder(String name) {
	
		String basedir = "db/prj/" + name;
		
		System.out.println(basedir);
		
		File prj = new File(basedir);
		
		//Elimino prima tutti i dati, dopo la cartella
		String[]entries = prj.list();
		for(String s: entries){
		    File currentFile = new File(prj.getPath(),s);
		    currentFile.delete();
		}
		
		prj.delete();
	}
	
	/**
	 * Salva una card del progetto
	 * 
	 * @param namePrj Nome del progetto
	 * @param card Card da salvare
	*/
    private void savePrjCard(String namePrj, Card card) {
		try{
			
			String basedir = "db/prj/" + namePrj;		
			String cardPath = basedir +"/" + card.ReturnName() + ".json";
			
			File cardFile = new File(cardPath);	
			ObjectMapper map = new ObjectMapper();
			
			map.configure(SerializationFeature.INDENT_OUTPUT, true);
			map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			map.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			
			if(!cardFile.exists())
				cardFile.createNewFile();
			
			map.writeValue(cardFile, card); 
			
		} catch (Exception e) {
			System.out.println("Error exception: \n"+ e);
		}
	}
}
