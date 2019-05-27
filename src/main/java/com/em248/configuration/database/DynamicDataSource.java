package com.em248.configuration.database;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private ConcurrentHashMap<String, DataSource> backupTargetDataSources = new ConcurrentHashMap<>();


    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }




    public void setTargetDataSource(Map targetDataSource){
        super.setTargetDataSources(targetDataSource);
        this.afterPropertiesSet();
    }
}
