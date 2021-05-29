package Server.structure;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"where",
	"data"
})

public class History {

	private String where;
	private String data;
	
	public History(String where) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		Date date = new Date();
		
		this.where = where;
		this.data =  formatter.format(date);
	}
	
	@JsonCreator
	public History(@JsonProperty("where") String where, @JsonProperty("data") String data) {
		this.where = where;
		this.data = data;
	}
	
	/**
	 * Ritorna dov'è situata la card attualmente
	*/
	public String ReturnWhere() {
		return this.where;
	}
	
	/**
	 * Ritorna dov'è situata la card attualmente
	*/
	public String ReturnData() {
		return this.data;
	}
}
