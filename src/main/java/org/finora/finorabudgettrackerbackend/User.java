package org.finora.finorabudgettrackerbackend;

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

}
