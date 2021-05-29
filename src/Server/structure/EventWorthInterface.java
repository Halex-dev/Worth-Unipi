package Server.structure;

import java.rmi.Remote;
import java.rmi.RemoteException;
import Client.MemberClientInt;

/**
 * EventWorthInterface rappresenta l'interfaccia offerta al client
 *
 * @author Alessio Vito D'angelo
 */

public interface EventWorthInterface extends Remote{

    public boolean register(String nickname ,String psw) throws RemoteException, IllegalArgumentException;

	public void registerCallback(MemberClientInt member) throws RemoteException;
	public void unregisterCallback(MemberClientInt member) throws RemoteException;
}
