package com.example.storage.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Credentials {
	
	private String oldPassword;
	private String newPassword;
	

	
	
	  public Credentials(String oldPassword, String newPassword) {
	        this.oldPassword = oldPassword;
	        this.newPassword = newPassword;
	    }

	    // Metodo per validare che entrambe le password siano presenti
	    public boolean isValid() {
	        return oldPassword != null && !oldPassword.isBlank() &&
	               newPassword != null && !newPassword.isBlank();
	    }
}
