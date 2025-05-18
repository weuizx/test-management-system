package org.evilincorporated.pineapple.repository.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "client")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_seq")
    @SequenceGenerator(
            name = "client_seq",
            sequenceName = "client_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "username",
            nullable = false,
            unique = true)
    private String username;

    @Column(name = "password",
            nullable = false)
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
