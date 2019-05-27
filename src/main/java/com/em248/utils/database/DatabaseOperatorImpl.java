package com.em248.utils.database;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.db.DbUtil;
import com.em248.configuration.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import sun.nio.ch.IOUtil;

import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Map;


@Component
@Slf4j
public class DatabaseOperatorImpl implements DatabaseOperator {


	private static final String driverClassName="com.mysql.jdbc.Driver";
	//jdbc:mysql://{host}:{port}/{databaseName}
	private static final String urlTemplet="jdbc:mysql://%s:%s/%s";
	//mysql://{username}:{password}@{host}:{port}/{databaseName}
//	private static final String uriTemplet="mysql://%s:%s@%s:%s/%s";

	private final ApplicationProperties applicationProperties;

	protected JdbcTemplate jdbcTemplate;


	public DatabaseOperatorImpl(ApplicationProperties applicationProperties, JdbcTemplate jdbcTemplate) {
		this.applicationProperties = applicationProperties;
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(String.format(urlTemplet,
				applicationProperties.getAdminDatabase().getHost(),
				applicationProperties.getAdminDatabase().getPort(),""));
		dataSource.setUsername(applicationProperties.getAdminDatabase().getUsername());
		dataSource.setPassword(applicationProperties.getAdminDatabase().getPassword());
		this.jdbcTemplate=new JdbcTemplate(dataSource);
	}

	public String getDriverClassName(){
		return driverClassName;
	}

	@Override
	public String getUrl(String databaseName){
//		return String.format(urlTemplet,dbSetting.getHost(),dbSetting.getPort(),databaseName);
		return null;
	}
	@Override
	public String getUri(String databaseName,String username,String password){
//		return String.format(uriTemplet,username, password,dbSetting.getHost(),dbSetting.getPort(),databaseName);
		return null;
	}
	@Override
	public CreateDatabaseResult createDatabase(String databaseName, Map<String, String> parameters) {
		String charset=null;
		String collate =null;
		StringBuilder sql=new StringBuilder("create database `").append(databaseName).append("`");
		if(parameters!=null){
			charset=parameters.get("character set");
			collate =parameters.get("collate");
			if(charset!=null)
				sql.append(" character set ").append(charset);
			if(collate!=null)
				sql.append(" collate ").append(collate);
		}
		try {
			jdbcTemplate.execute(sql.toString());
			return CreateDatabaseResult.builder()
					.dbName(databaseName)
					.host(applicationProperties.getAdminDatabase().getHost())
					.port(applicationProperties.getAdminDatabase().getPort())
					.build();
		} catch (Exception e) {
			log.error("Init Database error", e);
			throw new DatabaseOperatorException();
		}
	}
	
	@Override
	public boolean dropDatabase(String databaseName) {
		String sql="drop database if exists "+databaseName;
		try {
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean createUser(String databaseName,String username,String password) {
		StringBuilder sql=new StringBuilder();
		sql.append("create user '").append(username).append("'@'%' identified by '"+password).append("'");
		try {
			jdbcTemplate.execute(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		sql=new StringBuilder();
		sql.append("grant all privileges on `").append(databaseName).append("`.* to '").append(username).append("'@'%'");
		try {
			jdbcTemplate.execute(sql.toString());
		} catch (Exception e) {
			dropUser(databaseName,username);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean initDbTables(String databaseName, String username, String password) {
		ClassPathResource classPathResource = new ClassPathResource("init_db.sql");
		String initSql = FileUtil.readString(classPathResource.getFile(), Charset.forName("UTF-8"));
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(String.format(urlTemplet,
				applicationProperties.getAdminDatabase().getHost(),
				applicationProperties.getAdminDatabase().getPort(),databaseName));
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		System.out.println(initSql);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			jdbcTemplate.execute(initSql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean dropUser(String databaseName,String username) {
		String sql="drop user '"+username+"'";
		try {
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void close() {
	}
}
