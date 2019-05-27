package com.em248.configuration.database;

import com.em248.model.DataSourceConfig;
import com.em248.repository.DataSourceConfigRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@Order(-1)
public class DynamicDataSourceRegister
        implements ImportBeanDefinitionRegistrar, EnvironmentAware , ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    private ConversionService conversionService = new DefaultConversionService();
    private PropertyValues dataSourcePropertyValues;

    // 如配置文件中未指定数据源类型，使用该默认值
    private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";
    // 数据源
    private DataSource defaultDataSource;



    private Map<String, DataSource> customDataSources = new HashMap<>();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        // 将主数据源添加到更多数据源中
        targetDataSources.put("dataSource", defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        // 添加更多数据源

        targetDataSources.putAll(customDataSources);
        for (String key : customDataSources.keySet()) {
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        }
        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);

        logger.info("Dynamic DataSource Registry");
    }


    public void updateCustomerDatasource(String key, DataSource dataSource) {
        DynamicDataSource bean = applicationContext.getBean(DynamicDataSource.class);
        customDataSources.put(key, dataSource);
        bean.setTargetDataSource(customDataSources);
    }

    /**
     * 创建DataSource
     * @return
     * @author SHANHY
     * @create 2016年1月24日
     */
    @SuppressWarnings("unchecked")
    public DataSource buildDataSource(Map<String, Object> dsMap) {
        try {
            Object type = dsMap.get("type");
            if (type == null)
                type = DATASOURCE_TYPE_DEFAULT;// 默认DataSource

            Class<? extends DataSource> dataSourceType;
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);

            String driverClassName = dsMap.get("driver-class-name").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();

            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
                    .username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载多数据源配置
     */
    @Override
    public void setEnvironment(Environment env) {
        initDefaultDataSource(env);
    }

    /**
     * 初始化主数据源
     *
     */
    private void initDefaultDataSource(Environment env) {
        // 读取主数据源
//        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
        Map<String, Object> dsMap = new HashMap<>();
        dsMap.put("type", "com.zaxxer.hikari.HikariDataSource");
        dsMap.put("driver-class-name", "com.mysql.cj.jdbc.Driver");
        dsMap.put("url", "jdbc:mysql://10.66.222.99:23306/test");
        dsMap.put("username", "root");
        dsMap.put("password", "root");

        defaultDataSource = buildDataSource(dsMap);
    }

    public ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 为DataSource绑定更多数据
     *
     * @param dataSource
     * @param env
     * @author SHANHY
     * @create  2016年1月25日
     */
//    private void dataBinder(DataSource dataSource, Environment env){
//        RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
//        //dataBinder.setValidator(new LocalValidatorFactory().run(this.applicationContext));
//        dataBinder.setConversionService(conversionService);
//        dataBinder.setIgnoreNestedProperties(false);//false
//        dataBinder.setIgnoreInvalidFields(false);//false
//        dataBinder.setIgnoreUnknownFields(true);//true
//        if(dataSourcePropertyValues == null){
//            Map<String, Object> rpr = new RelaxedPropertyResolver(env, "spring.datasource").getSubProperties(".");
//            Map<String, Object> values = new HashMap<>(rpr);
//            // 排除已经设置的属性
//            values.remove("type");
//            values.remove("driver-class-name");
//            values.remove("url");
//            values.remove("username");
//            values.remove("password");
//            dataSourcePropertyValues = new MutablePropertyValues(values);
//        }
//        dataBinder.bind(dataSourcePropertyValues);
//    }

}
