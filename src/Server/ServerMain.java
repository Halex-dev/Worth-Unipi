package Server;


/**
 * - la fase di registrazione viene implementata mediante RMI. FATTO
 * 
 * Il server può essere realizzato multithreaded oppure può effettuare il multiplexing dei canali mediante NIO FATTO
 * - Deve essere implementata una struttura dati separata per ciascuna lista di progetto FATTO
 * 
 * - La chat di progetto deve essere realizzata usando UDP multicast
 * 
 * @author Halex_
 *
 */

public class ServerMain {
	
    private static final int PORT_DEFAULT = 3789;

    public static void main(String[] args){
    	
        int port = PORT_DEFAULT;
        
		Server server = new Server(port);
		
        server.start();
    }
}
