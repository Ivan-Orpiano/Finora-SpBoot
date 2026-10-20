package org.finora.finorabudgettrackerbackend.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length =  100)
    String name;

    @Column(nullable = false, unique = true, length = 190)
    String email;

    @Column(nullable = false)
    String passwordHash;

    @Column(nullable = false, length = 3)
    String preferredCurrency = "PHP";

    @Column(nullable = false)
    int alertThreshold = 80;

    protected User(){}

    public User(String name, String email, String passwordHash){
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public void updateProfile(
        String name, String email, String currency, int threshold
    ) {
        this.name = name;
        this.email = email;
        this.preferredCurrency = currency;
        this.alertThreshold= threshold;
    }
    public Long getId() {return id;}
    public String getName(){return name;}
    public String getEmail() {return email;}
    public String getPasswordHash(){return passwordHash;}
    public  String getPreferredCurrency() {return preferredCurrency;}
    public int getAlertThreshold() {return alertThreshold;}
}
