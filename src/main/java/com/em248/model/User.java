package com.em248.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Table
@Entity
@NoArgsConstructor
public class User {

    @Id
    private String id = UUID.randomUUID().toString();


    private String username;

    private String password;

    private String idCard;

    private String phone;

    private String email;

    @OneToOne
    private DataSourceConfig dataSourceConfig;


    public User(String email , String password) {
        this.password = password;
        this.email = email;
    }
}
