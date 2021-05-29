package Client;

import Client.Form.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.*;

public class GUIClientMain {
	
	//Fatti manualmente
	private static LoginFrame Login;
	private static RegisterFrame Register;
	private static HomeFrame Home;
	private static NewProject NewProject;
	
	//Fatti con netbeans GUI
	private static ShowProjects ShowProjects;
	private static NewCard NewCard;
	private static ProjectCard ProjectShow;
	private static Cards Cards;
	private static Chat ChatShow;
	private static ShowCard ShowCard;
	private static ShowMembers ShowMembers;
	
	//Client e interfaccia per L'RMI
	private static Client client = new Client();
	private static MemberClient MemberClient;
	 
	 /**
     * Funzione che disabilita le GUI attive.
     */
	public static void DisableAllFrame() {
		if(Login != null) {
			Login.setVisible(false);
			Login = null;
		}
		
		if(Register != null) {
			Register.setVisible(false);
			Register = null;
		}
		
		if(Home != null) {
			Home.setVisible(false);
			Home = null;
		}
		
		if(NewProject != null) {
			NewProject.setVisible(false);
			NewProject = null;
		}
		
		if(ShowProjects != null) {
			ShowProjects.setVisible(false);
			ShowProjects = null;
		}
		
		if(NewCard != null) {
			NewCard.setVisible(false);
			NewCard = null;
		}
		
		if(ProjectShow != null) {
			ProjectShow.setVisible(false);
			ProjectShow = null;
		}
		
		if(Cards != null) {
			Cards.setVisible(false);
			Cards = null;
		}
		
		if(ShowCard != null) {
			ShowCard.setVisible(false);
			ShowCard = null;
		}
		
		if(ShowMembers != null) {
			ShowMembers.setVisible(false);
			ShowMembers = null;
		}
	}
	
	 /**
     * Main dell'intefaccia grafica del client
     */
    public static void main(String[] a) {
    	client = new Client();
    	LoginForm();
    }
    
	 /**
     * Funzione che disabilita la GUI della chat.
     */
	public static void DisableFrameChat() {
		if(ChatShow != null) {
			ChatShow.setVisible(false);
			ChatShow = null;
		}
	}
	 /**
     * Funzione che passa i parametri al client per l'add di una card
     *
     * @param cardname Nome della card che si vuole creare
     * @param description descrizione della card
     * @param namePrj Nome del progetto dove si vuole aggiungere la card
     */
	public static String addCard(String cardname, String description, String namePrj) {
		return client.addCard(cardname, description, namePrj);
	}
	
	 /**
     * Funzione che passa i parametri al client per la creazione di un progetto
     *
     * @param name Nome del progetto
     * @param description descrizione del progetto
     * @param deadline Data entro il quale bisogna finire il progetto
     * @param owner Nome del creatore del progetto
     */
    public static boolean CreateProject(String name, String description, String deadline, String owner) {
    	return client.CreateProject(name, description, deadline, owner);
    }
    
	 /**
     * Funzione che passa il nome dell'utente al client per ricevere informazioni dei progetti che può visualizzare
     */
    public static String ShowProjectsString() {
    	return client.ShowProjects(client.ReturnNickname());
    }
    
	 /**
     * Funzione che passa il nome del progetto da visualizzare
     *
     * @param name nome del progetto da visualizzare
     */
    public static String ShowPrj(String name) {
    	return client.ShowPrj(name);
    }
    
	 /**
     * Passa al client il nome del progetto di cui si vuole sapere l'address
     * 
     * @param name Nome del progetto
     */
    public static String JoinChat(String name) {
    	return client.JoinChat(name);
    }
     
	 /**
     * Passa al client le informazioni riguardante le card di un progetto che si vuole visualizzare in una determinata lista
     *
     * @param name Nome del progetto
     * @param chose lista che si vuole visualizzare
     */
    public static String GetCards(String name, Integer chose) {
    	return client.GetCards(name, chose);
    }
    
	 /**
     * Passa al client i dati per muovere la card da una lista ad un'altra
     *
     * @param name Nome del progetto dove si trova la card
     * @param cardName Nome della card che si vuole spostare
     * @param where Dove si trova la card
     * @param move Dove si vuole muovere la card
     */
    public static boolean MoveCards(String name, String cardName , Integer where, Integer move ) {
    	return client.MoveCards(name, cardName, where, move);
    }
    
	 /**
     * Passa al client il nickname da settare.
     *
     * @param nick nome dell'utente
     */
    public static void setNickname(String nick) {
    	client.setNickname(nick);
    }
    
	 /**
     * Ritorna la stringa che viene usata per separare diversi dati quando manda i pacchetti.
     */
    public static String ReturnAdder() {
    	return client.ReturnAdder();
    }
    
	 /**
     * Ritorna la stringa che viene usata per separare diverse stringhe quando manda i pacchetti.
     */
    public static String RerturnEnd() {
    	return client.RerturnEnd();
    }
    
	 /**
     * Ritorna l'username del client
     */
    public static String ReturnNickname() {
    	return client.ReturnNickname();
    }
    
	 /**
     * Funzione che fa il logout dal server, togliendo la rmi e resettando il nome
     */
    public static void Logout(){
    	UnregisterNotify();
    	client.Logout(ReturnNickname());
    	setNickname("");
    	DisableFrameChat();
    	LoginForm();
    	
    }
    
	 /**
     * Funzione che fa registrare il client alle RMICallback e apre l'interfaccia grafica
     * 
     * @throws NotBoundException se si verifica un errore di associazione nel registro passandogli un nome non esistente
     * @throws RemoteException se si verificano durante l'esecuzione della chiamata remota
     */
    public static void RMINotify() throws RemoteException, NotBoundException {
		MemberClient = new MemberClient(ReturnNickname());
		client.RegisterRMICallback(MemberClient);
    }
    
	 /**
     * Funzione che fa cancellare il client alle RMICallback
     * 
     */
    public static void UnregisterNotify() {
    	if(MemberClient != null)
    		client.unregisterRMICallback(MemberClient); 	
    }
    
	 /**
     * Passa i paramentri al client per il login
     *
     * @param name nome per il login
     * @param psw password per il login
     */
    public static boolean Login(String name, String psw) {
    	try{
    		RMINotify();
    		if(client.Login(name, psw)) {	
    			return true;
    		}
    		else {
	    		UnregisterNotify();
	    		return false;
    		}
    	}
    	catch(Exception e) {
    		UnregisterNotify();
    		return false;
    	}
    }
    
	 /**
     * Passa i paramentri al client per la registrazione
     *
     * @param name nome per il login
     * @param psw password per il login
     */
    public static boolean Register(String name, String psw) {
    	return client.Register(name, psw);
    }
    
	 /**
     * Passa i paramentri al client per aggiungere un utente ad un progetto
     *
     * @param name nome del progetto
     * @param user nome dell'utente da aggiungere
     */
	public static String addAccess(String name, String user) {
		return client.addAccess(name, user);
	}
	
	 /**
     * Passa i paramentri al client per rimuovere un utente da un progetto
     *
     * @param name nome del progetto
     * @param user nome dell'utente da rimuovere
     */
	public static boolean removeAccess(String name, String user) {
		return client.removeAccess(name, user);
	}
	
	 /**
     * Passa i paramentri al client per visualizzare una determinata card
     *
     * @param name Nome del progetto dove si trova la card
     * @param cardName Nome della card
     * @param where Dove si trova la card
     */
	public static String getCard(String name, String cardName, Integer where) {
		return client.GetCard(name, cardName, where);
	}
    
	 /**
     * Passa il nome del proggetto al client per visualizzarne i membri
     *
     * @param name Nome del progetto
     */
	public static String GetMembers(String name) {
		return client.GetMembers(name);
	}
	
	 /**
     * Passa il nome del proggetto che si vuole eliminare al client
     *
     * @param name Nome del progetto
     */
	public static boolean DeletePrj(String name) {
		return client.DeletePrj(name);
	}
	
	 /**
     * Passa i paramentri al client per visualizzare una determinata card
     *
     * @param name Nome del progetto dove si trova la card
     * @param cardName Nome della card
     * @param where Dove si trova la card
     */
    public static void ShowCard(String name, String cardName, Integer where) {
    	DisableAllFrame(); 
    	ShowCard = new ShowCard(name, cardName, where);
    }
    
	 /**
     * Passa i paramentri al client per visualizzare le card di un progetto
     *
     * @param name Nome del progetto dove si trova la card
     * @param chosen lista che si vuole visualizzare
     */
    public static void ShowCards(String name, Integer chosen) {
    	DisableAllFrame(); 
    	Cards = new Cards(name, chosen);
    }
    
	 /**
     * Inizializza l'interffaccia grafica dei membri
     *
     * @param name Nome del progetto
     */
	public static void ShowMember(String name) {
		DisableAllFrame();
		ShowMembers = new ShowMembers(name);
	}
	
	 /**
     * Inizializza l'interffaccia grafica dei membri
     *
     * @param name Nome del progetto
     */
    public static void ProjectCard(String name) {
    	DisableAllFrame(); 
    	ProjectShow = new ProjectCard(name);
    	ProjectShow.setTitle("Project: " + name );
    	ProjectShow.setVisible(true);
    	//card.setBounds(10, 10, 600, 400);
    	ProjectShow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	ProjectShow.setResizable(false);
    	ProjectShow.setLocationRelativeTo(null);
    }
      
	 /**
     * Inizializza l'interffaccia grafica della chat
     *
     * @param name Nome del progetto
     */
    public static void ChatShow(String name) {
    	//DisableAllFrame();
    	DisableFrameChat();
    	ChatShow = new Chat(name);
    	ChatShow.setTitle("Project " + name + " - Chat");
    	ChatShow.setVisible(true);
    	//card.setBounds(10, 10, 600, 400);
    	ChatShow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    	ChatShow.setResizable(false);
    	ChatShow.setLocationRelativeTo(null);
    }  
    
	 /**
     * Inizializza l'interffaccia per la creazione di una nuova card
     *
     * @param name Nome del progetto
     */
    public static void NewCard(String name) {
    	DisableAllFrame(); 
    	NewCard = new NewCard(name);
    	NewCard.setTitle(name + " - New card" );
    	NewCard.setVisible(true);
    	//card.setBounds(10, 10, 600, 400);
    	NewCard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	NewCard.setResizable(false);
    	NewCard.setLocationRelativeTo(null);
    }
    
	 /**
     * Inizializza l'interffaccia per la visualizzazione dei progetti
     */
    public static void ShowProjects(){
    	DisableAllFrame();  	
        ShowProjects = new ShowProjects();
    	ShowProjects.setTitle("Worth - Show project");
    	ShowProjects.setVisible(true);
    	ShowProjects.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	ShowProjects.setResizable(false);
    	ShowProjects.setLocationRelativeTo(null);
    }
    
	 /**
     * Inizializza l'interffaccia della home
     */
    public static void Home(){
    	DisableAllFrame();
    	Home = new HomeFrame(ReturnNickname());
    	Home.setTitle("Home - " + ReturnNickname());
    	Home.setVisible(true);
    	Home.setBounds(10, 10, 600, 350);
    	Home.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	Home.setResizable(false);
    	Home.setLocationRelativeTo(null);
    }
    
	 /**
     * Inizializza l'interffaccia del login
     */
    public static void LoginForm(){
    	DisableAllFrame();
    	Login = new LoginFrame();
    	Login.setTitle("Worth - Login");
    	Login.setVisible(true);
    	Login.setBounds(10, 10, 400, 400);
    	Login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	Login.setResizable(false);
    	Login.setLocationRelativeTo(null);
    }
    
	 /**
     * Inizializza l'interffaccia della Registrazione
     */
    public static void RegisterForm(){
    	DisableAllFrame();
    	Register = new RegisterFrame();
    	Register.setTitle("Worth - Register");
    	Register.setVisible(true);
    	Register.setBounds(10, 10, 400, 400);
    	Register.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	Register.setResizable(false);
    	Register.setLocationRelativeTo(null);
    }
    
	 /**
     * Inizializza l'interffaccia di un nuovo progetto
     */
    public static void NewProject(){
    	DisableAllFrame();
    	NewProject = new NewProject();
    	NewProject.setTitle("Worth - New project");
    	NewProject.setVisible(true);
    	NewProject.setBounds(10, 10, 600, 500);
    	NewProject.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	NewProject.setResizable(false);
    	NewProject.setLocationRelativeTo(null);
    }
    
}
