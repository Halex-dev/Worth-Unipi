package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import Server.structure.Card;
import Server.structure.EventWorth;
import Server.structure.EventWorthInterface;
import Server.structure.History;
import Server.structure.Project;

/**
 * 
 * @author Halex_
 *
 */
class Server {
	
	private int PORT;
    private int MSG_LENGTH = 1024;
    private int ActiveConnections;
    
    private EventWorth eventoWorth;
    private EventWorthInterface stub;
	
	public Server(int PORT) {
		this.PORT = PORT;
	}
	
	/**
	* Starta il server
	*/
	public void start() {		    
	    try {
	    	RMI(this.PORT);	
	    	Multiplexing(this.PORT+1);
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	* Crea lo stub e fa il rebind della classe EventWorth
	* 
	* @param PORT Porta per la RMI
	*/
	private void RMI(int PORT) {
	    try {    	
	    	eventoWorth= new EventWorth();
	        // esporta l'oggetto
	    	stub = (EventWorthInterface) UnicastRemoteObject.exportObject(eventoWorth, PORT);
	
	        // crea il registro
	    	Registry reg = startRegistry(PORT);
	
	        // binding
	    	reg.rebind("EVENT_CONGRESS", stub);
	        
	        System.out.println("RMI ready");
	        
	    }
	    catch (RemoteException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	* Metodo che verifica se c’è già un registry attivo sull’host sulla porta indicata, e se non c’è lo crea.
	* 
	* @param RMIPortNum Porta per la RMI
	*/
	private static Registry startRegistry(int RMIPortNum) throws RemoteException{
		try {
			 Registry registry = LocateRegistry.getRegistry(RMIPortNum);
			 registry.list();
			 return registry;
		 }
		 catch (RemoteException e) {
			 Registry registry = LocateRegistry.createRegistry(RMIPortNum);
			 return registry;
		}
	}
	
	/**
	* Metodo che verifica se c’è già un registry attivo sull’host sulla porta indicata, e se non c’è lo crea.
	* 
	* @param RMIPortNum Porta per la RMI
	*/
	private void Multiplexing(int PORT){
		
		 try {		 			
				// ServerSocketChannel: selectable channel for stream-oriented listening sockets
				ServerSocketChannel Server = ServerSocketChannel.open();

				// Binds the channel's socket to a local address and configures the socket to listen for connections
				Server.socket().bind(new InetSocketAddress(PORT));
		 
				// Adjusts this channel's blocking mode.
				Server.configureBlocking(false);
		
				// Selector: multiplexor of SelectableChannel objects
				Selector selector = Selector.open(); // selector is open here
				
				Server.register(selector, SelectionKey.OP_ACCEPT);
				
				System.out.println("Multiplexing ready");
				
				// Infinite loop..
				// Keep server running
				while(true){
	                if (selector.select() == 0)
	                    continue;
	                
	                // insieme delle chiavi corrispondenti a canali pronti
	                Set<SelectionKey> selectedKeys = selector.selectedKeys();
	                // iteratore dell'insieme sopra definito
	                Iterator<SelectionKey> iter = selectedKeys.iterator();
	                
	                while (iter.hasNext()) {
	                    SelectionKey key = iter.next();
	                    iter.remove();
	                    try {       // utilizzo la try-catch per gestire la terminazione improvvisa del client
	                        if (key.isAcceptable()) {               // ACCETTABLE
	                            /*
	                             * accetta una nuova connessione creando un SocketChannel per la
	                             * comunicazione con il client che la richiede
	                             */
	                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
	                            SocketChannel c_channel = server.accept();
	                            c_channel.configureBlocking(false);
	                            
	                            System.out.println("Server: new connection:" + c_channel.getRemoteAddress()+" total connections:" + ++this.ActiveConnections);
	                            this.registerRead(selector, c_channel);
	                        } else if (key.isReadable()) {                  // READABLE
	                            this.readClientMessage(selector, key);
	                            
	                        }
	                        if (key.isWritable()) {                 // WRITABLE
	                            this.SendMessageClient(key);
	                        }
	                    }
	                    catch (IOException e) {             // terminazione improvvisa del client
	                        e.printStackTrace();
	                        key.channel().close();
	                        key.cancel();
	                    }
	                }
	            }
			}
		    catch (Exception e) {
		        e.printStackTrace();
		    }		
	}
	
	 /**
     * registra l'interesse all'operazione di READ sul selettore
     *
     * @param sel selettore utilizzato dal server
     * @param c_channel socket channel relativo al client
     * @throws IOException se si verifica un errore di I/O
     */
    private void registerRead(Selector sel, SocketChannel c_channel) throws IOException {

        // crea il buffer
        ByteBuffer length = ByteBuffer.allocate(Integer.BYTES);
        ByteBuffer message = ByteBuffer.allocate(MSG_LENGTH);
        ByteBuffer[] bfs = {length, message};
        // aggiunge il canale del client al selector con l'operazione OP_READ
        // e aggiunge l'array di bytebuffer [length, message] come attachment
        c_channel.register(sel, SelectionKey.OP_READ, bfs);
    }

    /**
     * Legge il messaggio inviato dal client e registra l'interesse all'operazione di WRITE sul selettore
     *
     * @param sel selettore utilizzato dal server
     * @param key chiave di selezione
     * @throws IOException se si verifica un errore di I/O
     */
    private void readClientMessage(Selector sel, SelectionKey key) throws IOException {
        /*
         * accetta una nuova connessione creando un SocketChannel per la comunicazione con il client che
         * la richiede
         */
        SocketChannel c_channel = (SocketChannel) key.channel();
        
        // recupera l'array di bytebuffer (attachment)
        ByteBuffer[] bfs = (ByteBuffer[]) key.attachment();
        c_channel.read(bfs);
        
        //Dichiaro le stringhe che divideranno il testo in diverse parti o più linee
        String adder = "::";
        String endline = ":end:";
        
        if (!bfs[0].hasRemaining()){
            bfs[0].flip();
            int l = bfs[0].getInt();

            
            //Dopo che è arrivato il messaggio, lo divido e controllo quale comando l'utente vuole eseguire
            if (bfs[1].position() == l) {
                bfs[1].flip();
                String msg = new String(bfs[1].array()).trim();
                
                String SEND = "ERROR_404";
                String[] ms = msg.split(adder);
                String COMMAND = ms[0];
                           	
                if (COMMAND.equals("LOGIN")){

                    String name = ms[1];
                    String psw = ms[2];
                    
                    if(this.eventoWorth.Login(name, psw))
                    	SEND = "LOGIN_505";
                    else
                    	SEND = "LOGIN_404";
                }
                else if (COMMAND.equals("LOGOUT")){

                    String nickname = ms[1];
                    
                    if(this.eventoWorth.Logout(nickname))
                    	SEND = "LOGOUT_505";

                }
                else if(COMMAND.equals("CREATEPROJECT")) {
                	
                    String name = ms[1];
                    String description = ms[2];
                    String owner = ms[3];
                    String deadline = ms[4];
                    
                    if(this.eventoWorth.CreateProject(name, description, deadline, owner))
                    	SEND = "PROJECT_505";
                    else
                    	SEND = "PROJECT_404";
                }
                else if(COMMAND.equals("SHOWPROJECTS")) {

                	String nickname = ms[1];
                	LinkedList<Project> tmp = this.eventoWorth.listProjects(nickname);
                	
                	if(tmp.size() == 0 )
                		SEND = "NOTFOUND";
                	
                	else if(tmp.size() > 0 )
                		SEND = "";
                	
                    if(tmp.size() > 0 && tmp != null)
                    	for(int i= 0; i<tmp.size(); i++) {
                    		Project prj = tmp.get(i);
                    		SEND += prj.ReturnName() + adder + prj.ReturnOwner() + adder + prj.ReturnDeadline() + endline;
                    	}
                }
                else if(COMMAND.equals("SHOWPRJ")) {

                	String name = ms[1];
                	
                	Project prj = this.eventoWorth.showPrj(name);
                	
                    if(prj != null)
                    	SEND = prj.ReturnName() + adder + prj.ReturnOwner() + adder + prj.ReturnDeadline() + adder + prj.ReturnDescription() + adder + prj.ReturnDatecreate();
                    else
                    	SEND = "NOTFOUND";
                }
                else if(COMMAND.equals("ADDCARD")) {

                	String cardname = ms[1];
                	String description = ms[2];
                	String name = ms[3];             	
	
                    if(this.eventoWorth.AddCard(cardname, description, name))
                    	SEND = "CARD_505";
                    else
                    	SEND = "CARD_404";
                }
                else if(COMMAND.equals("SHOWCARDS")) {

                	String name = ms[1];
                	int chose = Integer.parseInt(ms[2]);
                	
                	LinkedList<Card> cards = this.eventoWorth.showCards(name, chose);   
                	
                	if(cards.size() == 0 )
                		SEND = "NOTFOUND";
                	else if(cards.size() > 0 )
                		SEND = "";
                	
                	for(Card card : cards) {
                		SEND += card.ReturnName() + endline;
                	}
                }
                else if(COMMAND.equals("MOVCARD")) {
                	
                	String name = ms[1];
                	String cardName = ms[2];
                	
                	int where = Integer.parseInt(ms[3]);
                	int move = Integer.parseInt(ms[4]);
                
                	if(this.eventoWorth.MoveCard(cardName, name, where, move))
                		SEND = "MOVCARD_505";
                	
                }
                else if(COMMAND.equals("SHOWCARD")) {
                	
                	String name = ms[1];
                	String cardName = ms[2];  	
                	
                	int where = Integer.parseInt(ms[3]);
                
                	Card card = this.eventoWorth.showCard(cardName, name, where);
                	LinkedList<History> history = this.eventoWorth.getCardHistory(cardName, name, where);
                	
					if(card != null) {
						SEND = card.ReturnName() + adder + card.ReturnDesc() + endline;
						
						for(History hist : history) {
							SEND += hist.ReturnWhere() + adder + hist.ReturnData() +endline;
						}
					}
                }
                else if(COMMAND.equals("GETMEMBERS")) {
                	
                	String name = ms[1];
                
                	HashSet<String> members = this.eventoWorth.showMembers(name);
                	
                	if(members != null)
                		SEND = "";
                	
                	if(members.size() == 0 )
                		SEND = "NOTFOUND";
                	
                	for(String member : members) {
                		SEND += member + endline;
                	}
                }
                else if(COMMAND.equals("ADDACCESS")) {
                	
                	String name = ms[1];
                	String user = ms[2];
                	
                	SEND = this.eventoWorth.addMember(name, user);
                }
                else if(COMMAND.equals("REMOVACC")) {
                	
                	String name = ms[1];
                	String user = ms[2];
                	
                    if(this.eventoWorth.removeAccess(name, user))
                    	SEND = "REMOVACC_505";

                }
                else if(COMMAND.equals("DELVPRJ")) {
                	
                	String name = ms[1];
                	
                    if(this.eventoWorth.cancelProject(name))
                    	SEND = "DELVPRJ_505";

                }
                else if(COMMAND.equals("JOINCHAT")) {
                	
                	String name = ms[1];
                	
                	SEND = this.eventoWorth.JoinChat(name);

                }
                
                /*
                 * aggiunge il canale del client al selector con l'operazione OP_WRITE
                 * e aggiunge il messaggio da inviare come attachment
                 */
                
                c_channel.register(sel, SelectionKey.OP_WRITE, SEND);
            }
        
        }
    }

    /**
     * scrive il buffer sul canale del client
     *
     * @param key chiave di selezione
     * @throws IOException se si verifica un errore di I/O
     */
	public void SendMessageClient(SelectionKey key) throws IOException {
		
        SocketChannel c_channel = (SocketChannel) key.channel();
        String SEND = (String) key.attachment();
        ByteBuffer bbSEND = ByteBuffer.wrap(SEND.getBytes());
        c_channel.write(bbSEND);
        
        System.out.println("Send msg to:" + c_channel.getRemoteAddress() + " and close the connection");
        if (!bbSEND.hasRemaining()) {
        	bbSEND.clear();
            c_channel.close();
            key.cancel();
            --this.ActiveConnections;
        }
	}
}
