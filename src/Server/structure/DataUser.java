package Server.structure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties({"online"})

public class DataUser {

	@JsonPropertyOrder({
		"psw",
		"Datelogin",
		"Dateregister"
	})
	
	private String psw;
	private String Datelogin;
	private String Dateregister;
	private boolean online;
	
	@JsonCreator
	public DataUser(@JsonProperty("psw") String psw, @JsonProperty("Datelogin") String Datelogin, @JsonProperty("Dateregister") String Dateregister) throws NoSuchAlgorithmException, InvalidKeySpecException {				
		this.Datelogin = Datelogin;
		this.Dateregister = Dateregister;
		this.psw = psw;
		this.online = false;
	}
	
	public DataUser(String psw) throws NoSuchAlgorithmException, InvalidKeySpecException{
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		Date date = new Date();
		
		this.psw = cryptPsw(psw);
		this.Datelogin =  formatter.format(date);
		this.Dateregister = formatter.format(date);
		this.online = false; 
	}
	
	/**
	 * Stampa i dati di un utente, usato solo per debug
	*/
	public void printData() {
		System.out.println("psw: " + this.psw);
		System.out.println("Datelogin: " + this.Datelogin);
		System.out.println("Dateregister: " + this.Dateregister);
		System.out.println("Online: " + this.online);
	}
	
	/**
	 * Ritorna lo status dell'utente (online o offline)
	*/
	public boolean returnStatus() {
		return this.online;
	}
	
	/**
	 * Setta il login dell'utente e aggiorna la data di login
	 * 
	 * @param status true o false
	*/
	public void setLogin(boolean status) {
		if(status) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
			Date date = new Date();
			this.Datelogin =  formatter.format(date);
		}
			
		online = status;
	}
	
	/**
	 * Ritorna la password
	*/
	public String getPsw() {
		return this.psw;
	}
	
	/**
	 * Funzione che cripta la password, usando SHA
	 * 
	 * @param password da criptare
	*/
	public String cryptPsw(String password) {
        String algorithm = "SHA";

        byte[] plainText = password.getBytes();

        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            md.reset();
            md.update(plainText);
            byte[] encodedPassword = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < encodedPassword.length; i++) {
                if ((encodedPassword[i] & 0xff) < 0x10) {
                    sb.append("0");
                }

                sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
