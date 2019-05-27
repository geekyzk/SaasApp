package com.em248.configuration.database;

import com.em248.model.DataSourceConfig;
import com.em248.model.User;
import com.em248.repository.UserRepository;
import com.em248.security.SecurityUtils;
import com.em248.utils.thread.TheadLocalUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Order(-1)// 保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
    private UserRepository userRepository;
    private final DynamicDataSourceRegister dynamicDataSourceRegister;

    public DynamicDataSourceAspect(UserRepository userRepository, DynamicDataSourceRegister dynamicDataSourceRegister) {
        this.userRepository = userRepository;
        this.dynamicDataSourceRegister = dynamicDataSourceRegister;
    }

    @Before("@annotation(ChangeDataSource)")
    public void changeDataSource(JoinPoint point) {
        Optional<String> datasourceOptional = SecurityUtils.getCurrentUserDatasourceId();
        if (datasourceOptional.isPresent()) {
            if (dynamicDataSourceRegister.getCustomDataSources().containsKey(datasourceOptional.get())) {
                DynamicDataSourceContextHolder.setDataSourceType(datasourceOptional.get());
            } else {
                logger.info("not found datasource");
            }
        }else {
            logger.info("not found datasource");
        }
    }

    @After("@annotation(ChangeDataSource)")
    public void restoreDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
}
