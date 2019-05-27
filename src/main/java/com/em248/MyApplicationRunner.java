package com.em248;

import com.em248.configuration.database.DynamicDataSourceRegister;
import com.em248.model.DataSourceConfig;
import com.em248.repository.DataSourceConfigRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    private DataSourceConfigRepository dataSourceConfigRepository;
    private DynamicDataSourceRegister dataSourceRegister;

    public MyApplicationRunner(DataSourceConfigRepository dataSourceConfigRepository, DynamicDataSourceRegister dataSourceRegister) {
        this.dataSourceConfigRepository = dataSourceConfigRepository;
        this.dataSourceRegister = dataSourceRegister;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<DataSourceConfig> dataSourceConfigs = dataSourceConfigRepository.findAll();
        dataSourceConfigs.stream()
                .forEach(item -> {
                    Map<String, Object> dsMap = new HashMap<>();
                    dsMap.put("type", "com.zaxxer.hikari.HikariDataSource");
                    dsMap.put("driver-class-name", "com.mysql.cj.jdbc.Driver");
                    dsMap.put("url", item.getJdbcUrl());
                    dsMap.put("username", item.getUsername());
                    dsMap.put("password", item.getPassword());
                    dataSourceRegister.updateCustomerDatasource(item.getId(), dataSourceRegister.buildDataSource(dsMap));
                });
    }
}
