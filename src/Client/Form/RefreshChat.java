package Client.Form;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import javax.swing.JTextArea;

public class RefreshChat implements Runnable {

	private MulticastSocket chat;
	private int MSG_LENGTH;
	private JTextArea TextChat;
	
	public RefreshChat(MulticastSocket chat, Integer MSG_LENGTH, JTextArea TextChat) {
		this.chat = chat;
		this.MSG_LENGTH = MSG_LENGTH;
		this.TextChat = TextChat;
	}
	
	public void run() {
        try{
        	while(true) {
        		readChat();
        	}
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
	
	 /**
     * Operazione per visualizzare i messaggi
     *
	 * @throws IOException se si verifica un errore di I/O
     */
	public void readChat() throws IOException {
        DatagramPacket dat = new DatagramPacket(new byte[this.MSG_LENGTH], MSG_LENGTH);
        chat.receive(dat);
        
        String recive = new String(dat.getData(), dat.getOffset(), dat.getLength());
        
        String tmp = TextChat.getText();
        
        String[] str = recive.split(":", 2);
        
        tmp += str[0] + ": " + str[1] + "\n";
        TextChat.setText(tmp);
	}
}
