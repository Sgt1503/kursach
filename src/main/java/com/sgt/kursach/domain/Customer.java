package com.sgt.kursach.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity // This tells Hibernate to make a table out of this class
@Component
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    private String password;

    private String login;

    private String adress;

    private Date birthday;




    public Customer() { }

    public Customer(int id, String name, String email,
                        String password, String login,
                            String adress, Date birthday) {
        this.adress = adress;
        this.email = email;
        this.name = name;
        this.password = password;
        this.login = login;
        this.id = id;
        this.birthday = birthday;

    }
}

