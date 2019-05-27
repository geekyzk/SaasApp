package com.em248.repository;

import com.em248.model.Book;
import com.em248.model.DataSourceConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataSourceConfigRepository extends JpaRepository<DataSourceConfig,String> {
}
