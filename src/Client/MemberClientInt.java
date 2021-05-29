package Client;

import java.rmi.RemoteException;
import javax.swing.DefaultListModel;


public interface MemberClientInt extends java.rmi.Remote{
	
	public String name() throws RemoteException;
	public void callback(DefaultListModel<String> model) throws RemoteException;
}
