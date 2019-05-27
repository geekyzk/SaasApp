package com.em248.utils.database;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.UUID;

/**
 * 数据库配置
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class DBSetting {
	
	@Id
	private String id = UUID.randomUUID().toString();
	
	@JsonSerialize
	@JsonProperty("host")
	private String host;
	
	@JsonSerialize
	@JsonProperty("port")
	private Integer port;
	
	@JsonSerialize
	@JsonProperty("username")
	private String username;
	
	@JsonSerialize
	@JsonProperty("password")
	private String password;
	
	@JsonSerialize
	@JsonProperty("type")
	@Column(name="type",unique=true)
	private String type;
	
	@JsonSerialize
	@JsonProperty("max_instance")
	private Integer maxInstance;
	
	@JsonProperty("databaseName")
	private String databaseName;
	
	@JsonSerialize
	@JsonProperty("isBalance")
	private Boolean isBalance=false;
	
	@Transient
	private String url;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getMaxInstance() {
		return maxInstance;
	}
	public void setMaxInstance(Integer maxInstance) {
		this.maxInstance = maxInstance;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public Boolean getIsBalance() {
		return isBalance;
	}
	public void setIsBalance(Boolean isBalance) {
		this.isBalance = isBalance;
	}
	@Override
	public String toString() {
		return "DBSetting [id=" + id + ", host=" + host + ", port=" + port + ", username=" + username + ", password="
				+ password + ", type=" + type + ", maxInstance=" + maxInstance + ", databaseName=" + databaseName
				+ ", isBalance=" + isBalance + "]";
	}

}
