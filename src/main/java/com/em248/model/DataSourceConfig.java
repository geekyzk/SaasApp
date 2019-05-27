package com.em248.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table
@Data
@NoArgsConstructor
public class DataSourceConfig {

    @Id
    private String id = UUID.randomUUID().toString();

    private String dbName;

    private String username;

    private String password;

    private String dbHost;

    private String dbPort;


    public DataSourceConfig(String dbName, String username, String password, String dbHost, String dbPort) {
        this.dbName = dbName;
        this.username = username;
        this.password = password;
        this.dbHost = dbHost;
        this.dbPort = dbPort;
    }

    public String getJdbcUrl() {
        String temp = "jdbc:mysql://%s:%s/%s";
        return String.format(temp, this.dbHost, this.dbPort, this.dbName);
    }


}
