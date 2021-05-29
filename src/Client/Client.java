package Client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Server.structure.EventWorthInterface;


/**
 * - La fase di login deve essere effettuata come prima operazione dopo aver instaurato una connessione TCP con il server. FATTO
 * 
 * - A seguito della login il client si registra ad un servizio di notifica del server per ricevere aggiornamenti sullo stato degli utenti registrati con RMI e 
 * Il client mantiene una struttura dati per tenere traccia della lista degli utenti registrati e il loro stato
 * 
 * -  dopo previa login effettuata con successo, l’utente interagisce, secondo il modello client-server (richieste/risposte), con il server sulla connessione TCP creata, inviando i comandi elencati in precedenza.
 * 
 * Va bene eseguire client, server e registry sullo stesso host.
 * 
 * @author Alessio Vito D'angelo
 *
 */

public class Client {
    private final int PORT_DEFAULT = 3789;
    private Registry r;
    private int MAX_LENGHT = 1024;
    private static EventWorthInterface eventWorth;
    private String adder = "::";
    private String endline = ":end:";
    
    private String nickname;
    
    public Client(){ 
    	try {
			ConnectRMI();
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error server");
		}
    }
    
	 /**
     * Ritorna l'username
     */
    public String ReturnNickname() {
    	return this.nickname;
    }
    
	 /**
     * Setta il nome utente
     * 
     * @param nick sarà l'username del client
     */
    public void setNickname(String nick) {
    	this.nickname = nick;
    }
    
    
	 /**
     * Ottiene una reference dell'event manager del server
     *
     * @throws NotBoundException se si verifica un errore di associazione nel registro passandogli un nome non esistente
     * @throws RemoteException se si verificano durante l'esecuzione della chiamata remota
     */
    public void ConnectRMI() throws RemoteException, NotBoundException {
	    //ottiene una reference per il registro
    	this.r = LocateRegistry.getRegistry(this.PORT_DEFAULT);
	    // ottiene una reference all'event manager
	     eventWorth = (EventWorthInterface) r.lookup("EVENT_CONGRESS");
    }
    
	 /**
     * Registrarsi alle callback per gli aggiornamenti degli utenti online
     *
     * @param member Interfaccia grafica per tenere conto degli utenti online
     * @throws NotBoundException se si verifica un errore di associazione nel registro passandogli un nome non esistente
     * @throws RemoteException se si verificano durante l'esecuzione della chiamata remota
     */
    public void RegisterRMICallback(MemberClient member) throws RemoteException, NotBoundException {
    	eventWorth.registerCallback(member);
    }
    
	 /**
     * Cancella la registrazione per gli aggiornamenti degli utenti online
     *
     * @param member Interfaccia grafica per tenere conto degli utenti online
     */
    public void unregisterRMICallback(MemberClient member){
    	try {
    		 member.close();
			 eventWorth.unregisterCallback(member); 
			 member = null;
        } catch(Exception e){
        	System.out.println("Error to unregister RMICallback");
        }
    }
    
	 /**
     * Ritorna la stringa che viene usata per separare diversi dati quando manda i pacchetti.
     */
    public String ReturnAdder() {
    	return this.adder;
    }
    
	 /**
     * Ritorna la stringa che viene usata per separare diverse stringhe quando manda i pacchetti.
     */
    public String RerturnEnd() {
    	return this.endline;
    }
    
	 /**
     * Usa RMI per registrarsi all'applicazione
     *
     * @param name nome usato per la registrazione
     * @param psw password usato per la registrazione
     */
    public boolean Register(String name, String psw) {
    	try {
    		return eventWorth.register(name, psw);
    	}
        catch (Exception e) {
            return false;
        }
    }
    
	 /**
     * Manda un pacchetto con il TCP per il login
     *
     * @param name nome per il login
     * @param psw password per il login
     */
    public boolean Login(String name, String psw) {
    	
    		String[] msg = {"LOGIN", name, psw};
    		
    		String response = SendMessage(msg);
    		
    		if(response.equals("LOGIN_505"))
    			return true;
    		else
    			return false;
    }
    
	 /**
     * Manda un pacchetto con il TCP dicendo che slogga dall'applicazione
     *
     * @param name nome per il login
     * @param psw password per il login
     */
    public boolean Logout(String name) {
    	
		String[] msg = {"LOGOUT", name};
		
		String response = SendMessage(msg);
		
		if(response.equals("LOGOUT_505"))
			return true;
		else
			return false;
    }
    
	 /**
     * Manda un pacchetto con il TCP chiedendo informazioni sui progetti che può visualizzare
     *
     * @param nickname nome dell'utente che ha accesso a determinati progetit
     */
    public String ShowProjects(String nickname) {
    	
		String[] msg = {"SHOWPROJECTS", nickname};
		
		String response = SendMessage(msg);
		
		return response;
    }
    
	 /**
     * Manda un pacchetto con il TCP chiedendo informazioni su un determinato progetto
     *
     * @param name nome del progetto da visualizzare
     */
    public String ShowPrj(String name) {
    	
		String[] msg = {"SHOWPRJ", name};
		
		String response = SendMessage(msg);
		
		return response;
    }
    
	 /**
     * Manda un pacchetto con il TCP inviando i dati della card che si vuole creare
     *
     * @param cardname Nome della card che si vuole creare
     * @param description descrizione della card
     * @param namePrj Nome del progetto dove si vuole aggiungere la card
     */
	public String addCard(String cardname, String description, String namePrj) {
		
		String[] msg = {"ADDCARD", cardname, description, namePrj};
		
		String response = SendMessage(msg);
		
		return response;
	}
	
	 /**
     * Manda un pacchetto con il TCP inviando i dati del progetto che si vuole creare
     *
     * @param name Nome del progetto
     * @param description descrizione del progetto
     * @param deadline Data entro il quale bisogna finire il progetto
     * @param owner Nome del creatore del progetto
     */
    public boolean CreateProject(String name, String description, String deadline, String owner) {
    	try {
    		String[] msg = {"CREATEPROJECT", name, description, deadline, owner};
    		
    		String response = SendMessage(msg);
    		
    		if(response.equals("PROJECT_505"))
    			return true;
    		else
    			return false;
    	}
        catch (Exception e) {
            e.printStackTrace();
        }
    	return false;
    }
    
	 /**
     * Manda un pacchetto con il TCP dicendo quale card spostare e dove spostarla
     *
     * @param name Nome del progetto dove si trova la card
     * @param cardName Nome della card che si vuole spostare
     * @param chose Dove si trova la card
     * @param move Dove si vuole muovere la card
     */
	public boolean MoveCards(String name, String cardName, Integer chose, Integer move) {
    	try {
    		String c = chose.toString();
    		String m = move.toString();
    		String[] msg = {"MOVCARD", name, cardName, c, m};
    		
    		String response = SendMessage(msg);
    		
    		if(response.equals("MOVCARD_505"))
    			return true;
    		else
    			return false;
    	}
        catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}
	
	 /**
     * Manda un pacchetto con il TCP chiedendo informazioni riguardante una card
     *
     * @param name Nome del progetto dove si trova la card
     * @param cardName Nome della card
     * @param where Dove si trova la card
     */
	public String GetCard(String name, String CardName, Integer where) {
		try {
			String w = where.toString();
			
    		String[] msg = {"SHOWCARD", name, CardName, w};
    		
    		String response = SendMessage(msg);
    		
    		return response;
    	}
        catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	 /**
     * Manda un pacchetto con il TCP chiedendo informazioni riguardante le card di un progetto in una determinata lista
     *
     * @param name Nome del progetto
     * @param chose lista che si vuole visualizzare
     */
	public String GetCards(String name, Integer chose) {
		try {
			String c = chose.toString();
			
    		String[] msg = {"SHOWCARDS", name, c};
    		
    		String response = SendMessage(msg);
    		
    		return response;
    	}
        catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	 /**
     * Manda un pacchetto con il TCP chiedendo informazioni sui membri di un progetto
     *
     * @param name Nome del progetto
     */
	public String GetMembers(String name) {
		try {
			
    		String[] msg = {"GETMEMBERS", name};
    		
    		String response = SendMessage(msg);
    		
    		return response;
    	}
        catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	 /**
     * Manda un pacchetto con il TCP inviando le informazioni dell'utente che si vuole aggiungere al progetto
     *
     * @param name Nome del progetto
     * @param user nome dell'utente (deve esistere)
     */
	public String addAccess(String name, String user) {
		try {
			
    		String[] msg = {"ADDACCESS", name, user};
    		
    		String response = SendMessage(msg);
    		
    		return response;
    	}
        catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}

	 /**
     * Manda un pacchetto con il TCP inviando le informazioni dell'utente che si vuole eliminare dal progetto
     *
     * @param name Nome del progetto
     * @param user nome dell'utente (deve esistere)
     */
	public boolean removeAccess(String name, String user) {
		try {
			
    		String[] msg = {"REMOVACC", name, user};
    		
    		String response = SendMessage(msg);
    		
    		if(response.equals("REMOVACC_505"))
    			return true;
    		else
    			return false;
    	}
        catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}
	
	 /**
     * Manda un pacchetto con il TCP inviando le informazioni del progetto che si vuole eliminare
     * 
     * @param name Nome del progetto
     */
	public boolean DeletePrj(String name) {
		try {
			
    		String[] msg = {"DELVPRJ", name};
    		
    		String response = SendMessage(msg);
    		
    		if(response.equals("DELVPRJ_505"))
    			return true;
    		else
    			return false;
    	}
        catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}

	 /**
     * Manda un pacchetto con il TCP richidendo al server l'address di un progetto
     * 
     * @param name Nome del progetto
     */
	public String JoinChat(String name) {
		try {
			
    		String[] msg = {"JOINCHAT", name};
    		
    		String response = SendMessage(msg);
    		
    		return response;
    	}
        catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	 /**
     * Manda un pacchetto con il TCP.
     * 
     * @param msg Lista dei messaggi che si vuole inviare
     */
    public String SendMessage(String[] msg) {
      	 
    	try {
    		SocketChannel ServerConnected = SocketChannel.open(new InetSocketAddress(PORT_DEFAULT+1));

            //Send lenght of message
            ByteBuffer length = ByteBuffer.allocate(Integer.BYTES);
            int size = 0;
            
            for(int i = 0; i<msg.length; i++)
            	if(i != msg.length-1)
            		size += msg[i].length() + this.adder.length();
            	else
            		size += msg[i].length();
            
            length.putInt(size);
            length.flip();
            ServerConnected.write(length);
            length.clear();
            
            //Send the real message separate with ":"
            String send = "";
            for(int i = 0; i<msg.length; i++) {
            	if(i != msg.length-1)
            		send += msg[i] + adder;
            	else
            	send += msg[i];
            }
            
            ByteBuffer readBuffer = ByteBuffer.wrap(send.getBytes());
            ServerConnected.write(readBuffer);
            readBuffer.clear();

    		String response = ReadMessage(ServerConnected);
    		ServerConnected.close();
    		return response;
    	}
        catch (Exception e) {
        	return "ERROR_404";
        }
	}
    
	 /**
     * Aspetta una risposta dal server e la restituisce (timeout di 1000 ms)
     * 
     * @param ServerConnected Channel del del server a cui siamo connessi
     */
    public String ReadMessage(SocketChannel ServerConnected) throws IOException {
    	
    	ServerConnected.socket().setSoTimeout(1000);
		ByteBuffer buffer = ByteBuffer.allocate(MAX_LENGHT);
		int ByteRead = ServerConnected.read(buffer);

		//Buffer flip, rewind and set limit a to the end of String
		buffer.flip();
        byte[] msgBuf = new byte[ByteRead];
        
        //Get byte and convert it to string
        buffer.get(msgBuf);
        String arrived = new String(msgBuf, StandardCharsets.UTF_8);

		ServerConnected.close();
		return arrived;
    }
}
