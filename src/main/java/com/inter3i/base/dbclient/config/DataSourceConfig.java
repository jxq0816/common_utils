/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: Administrator
 * Created: 2017/05/12
 * Description:
 *
 */

package com.inter3i.base.dbclient.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataSourceConfig {
    private String dataSourceName;
    private String serverIp;
    private int port;
    private String userName;
    private String passWord;
    private String dbName;

    public DataSourceConfig initConfig() {
        InputStream in = getClass().getResourceAsStream("/datasource/datasource.properties");
        try {
            Properties prop = new Properties();
            prop.load(in);     ///加载属性列表
            dbName=prop.getProperty("ds.dbName.datasource1");
            serverIp=prop.getProperty("ds.serverIp.datasource1");
            port=Integer.parseInt(prop.getProperty("ds.serverPort.datasource1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
