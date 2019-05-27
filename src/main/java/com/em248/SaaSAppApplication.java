package com.em248;

import com.em248.configuration.ApplicationProperties;
import com.em248.configuration.database.DynamicDataSource;
import com.em248.configuration.database.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
@Import({DynamicDataSourceRegister.class})
public class SaaSAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaaSAppApplication.class, args);

    }

}
