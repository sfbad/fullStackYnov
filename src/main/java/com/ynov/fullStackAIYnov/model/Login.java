package com.ynov.fullStackAIYnov.model;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.ai.ollama.OllamaChatModel;
@Entity
@Data
@NoArgsConstructor
public class Login {
    @Id
    private String mail;

    @OneToOne
    @JoinColumn(name= "mail",referencedColumnName = "mail")
    private User user;
    private String password;


    public Login(User user, String password) {
        this.user = user;
        this.password = encryption(password);
    }

    public Login(String mail, String password) {
        this.mail = mail;
        this.password = encryption(password);
    }



    private String encryption(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

}
