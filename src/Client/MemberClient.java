package Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.DefaultListModel;
import Client.Form.WorthMember;

public class MemberClient extends UnicastRemoteObject implements MemberClientInt{

	private static final long serialVersionUID = 1L;
	private String nickname;
	
	private static WorthMember WorthMember;
	
	 /**
     * Costruttore della classe
     *
     * @param name nickname dell'utente
     * @throws RemoteException se si verificano durante l'esecuzione della chiamata remota
     */
	public MemberClient(String name) throws RemoteException{
		nickname=name;
	}

	 /**
	  * Restituisce il nickname
     */
	public String name(){ 
		return nickname;
	}
 
	 /**
     * Aggiorna la lista degli utenti online/offline
     *
     * @param model DefaultList degli utenti
     */
    public static void Member(DefaultListModel<String> model) {
		if(WorthMember != null)
			WorthMember.setVisible(false);
		
		WorthMember = new WorthMember(model);
    }
    
	 /**
     * Callback della RMI
     *
     * @param model DefaultList degli utenti
     * @throws RemoteException se si verificano durante l'esecuzione della chiamata remota
     */
	public void callback(DefaultListModel<String> model) throws java.rmi.RemoteException{
		if(WorthMember != null) {
			Member(model);
		}
		else
			Member(model);
	}

	 /**
     * Eliminazione dell'interfaccia
     */
	public void close() {
		if(WorthMember != null) {
			WorthMember.setVisible(false);
		}
	}

}
