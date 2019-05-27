package com.em248.service;


import com.em248.configuration.database.DynamicDataSourceRegister;
import com.em248.controller.vm.RegisterUserVM;
import com.em248.exception.BackException;
import com.em248.model.DataSourceConfig;
import com.em248.model.User;
import com.em248.repository.DataSourceConfigRepository;
import com.em248.repository.UserRepository;
import com.em248.service.dto.InitDatabaseResult;
import com.em248.utils.database.CreateDatabaseResult;
import com.em248.utils.database.DatabaseOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UserService {


    private final UserRepository userRepository;


    private final DataSourceConfigRepository dataSourceConfigRepository;

    private final DatabaseOperator databaseOperator;

    private final PasswordEncoder passwordEncoder;

    private final DynamicDataSourceRegister dynamicDataSourceRegister;

    public UserService(UserRepository userRepository, DataSourceConfigRepository dataSourceConfigRepository, DatabaseOperator databaseOperator, PasswordEncoder passwordEncoder, DynamicDataSourceRegister dynamicDataSourceRegister) {
        this.userRepository = userRepository;
        this.dataSourceConfigRepository = dataSourceConfigRepository;
        this.databaseOperator = databaseOperator;
        this.passwordEncoder = passwordEncoder;
        this.dynamicDataSourceRegister = dynamicDataSourceRegister;
    }


    @Transactional
    public void register(RegisterUserVM registerUserVM) {
        User user = initUserInfo(registerUserVM.getEmail(), registerUserVM.getPassword());
        InitDatabaseResult initDatabaseResult = initDatabase();
        if (initDatabaseResult != null) {
            DataSourceConfig dataSourceConfig = new DataSourceConfig(initDatabaseResult.getDbName(),
                    initDatabaseResult.getUsername(),
                    initDatabaseResult.getPassword(),
                    initDatabaseResult.getHost(),
                    initDatabaseResult.getPort());
            dataSourceConfig = dataSourceConfigRepository.save(dataSourceConfig);
            user.setDataSourceConfig(dataSourceConfig);
            userRepository.saveAndFlush(user);


            Map<String, Object> dsMap = new HashMap<>();
            dsMap.put("type", "com.zaxxer.hikari.HikariDataSource");
            dsMap.put("driver-class-name", "com.mysql.jdbc.Driver");
            dsMap.put("url", dataSourceConfig.getJdbcUrl());
            dsMap.put("username", dataSourceConfig.getUsername());
            dsMap.put("password", dataSourceConfig.getPassword());
            dynamicDataSourceRegister.updateCustomerDatasource(dataSourceConfig.getId(), dynamicDataSourceRegister.buildDataSource(dsMap));

        } else {
            throw new BackException("注册用户失败");
        }

    }



    private InitDatabaseResult initDatabase() {
        String dbName = UUID.randomUUID().toString();
        String dbUsername = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String dbPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        try {
            CreateDatabaseResult createDatabaseResult = databaseOperator.createDatabase(dbName, null);
            boolean createUserResult = databaseOperator.createUser(dbName, dbUsername, dbPassword);
            boolean initTablesResult = databaseOperator.initDbTables(dbName, dbUsername, dbPassword);
            System.out.println(createUserResult);
            System.out.println(initTablesResult);
            if (createUserResult&&initTablesResult) {
                InitDatabaseResult databaseResult = InitDatabaseResult.builder()
                        .dbName(dbName)
                        .username(dbUsername)
                        .password(dbPassword)
                        .host(createDatabaseResult.getHost())
                        .port(createDatabaseResult.getPort())
                        .build();

                return databaseResult;
            }  else {
                log.info("Create database user error({})", dbName);
                databaseOperator.dropDatabase(createDatabaseResult.getDbName());
                return null;
            }
        } catch (Exception e) {
            log.info("create database error");
            log.error("create database error", e);
            return null;
        }
    }

    private User initUserInfo(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new BackException("用户已存在，请使用其他邮件");
        }
        User user = new User(email, passwordEncoder.encode(password));
        return userRepository.save(user);
    }
}
