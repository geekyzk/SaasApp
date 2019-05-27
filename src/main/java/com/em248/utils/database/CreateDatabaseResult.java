package com.em248.utils.database;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDatabaseResult {

    private String host;

    private String port;

    private String dbName;
}
