package com.em248.service.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InitDatabaseResult {

    private String username;

    private String password;

    private String host;

    private String port;

    private String dbName;
}
