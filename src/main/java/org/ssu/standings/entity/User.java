package org.ssu.standings.entity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "pass")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "enabled")
    private Boolean enabled;
}
