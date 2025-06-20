package at.boot.models;

import at.boot.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "app_user")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_seq")
    @SequenceGenerator(name = "app_user_seq", sequenceName = "app_user_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private String firstname;

    private String lastname;

    private Integer age;

    @Column(name = "birthdate")
    private LocalDate dateOfBirth;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "confirmed_mail")
    private boolean confirmed;

    @Column(name = "time_stamp")
    private LocalDateTime tstamp;
}
