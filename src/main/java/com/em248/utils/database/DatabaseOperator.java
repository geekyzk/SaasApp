package com.em248.utils.database;


import java.util.Map;

public interface DatabaseOperator {
	

	
	/**
	 * 获取数据库连接URL
	 * @param databaseName 数据库名
	 * @return
	 */
	String getUrl(String databaseName);
	
	/**
	 * 获取数据库连接URI
	 * @param databaseName 数据库名
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	String getUri(String databaseName, String username, String password);

	/**
	 * 创建数据
	 * @param databaseName 数据库名
	 * @param parameters 参数
	 * @return
	 */
	CreateDatabaseResult createDatabase(String databaseName, Map<String, String> parameters);
	/**
	 * 删除数据库
	 * @param databaseName 数据库名
	 * @return
	 */
	boolean dropDatabase(String databaseName);
	/**
	 * 创建用户
	 * @param databaseName 数据库名
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	boolean createUser(String databaseName, String username, String password);

	boolean initDbTables(String databaseName, String username, String password);


	/**
	 * 删除用户
	 * @param databaseName 数据库名
	 * @param username 用户名
	 * @return
	 */
	boolean dropUser(String databaseName, String username);
	/**
	 * 关闭数据库连接
	 */
	void close();
}