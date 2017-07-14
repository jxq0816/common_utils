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

package com.inter3i.base.dbclient.repository;

import com.inter3i.base.dbclient.config.DataSourceConfig;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.*;

public class MongoDBDatasources {

    // mongodatasource name -------> MongoClientHoder
    private static Map<String, MongoClientHoder> allClients = new HashMap<>(16);

    public static void destory() {
        synchronized (allClients) {
            Iterator<String> allDataSourceNames = allClients.keySet().iterator();
            String dtSourceName = null;
            MongoClientHoder hoder = null;
            while (allDataSourceNames.hasNext()) {
                dtSourceName = allDataSourceNames.next();
                hoder = allClients.get(dtSourceName);
                hoder.destory();
            }
        }
    }

    public static MongoCollection getMongoCollection(final DataSourceConfig dataSourceConfig, final String tableName) {
        MongoClientHoder hoder = null;
        if (allClients.containsKey(dataSourceConfig.getDataSourceName())) {
            hoder = allClients.get(dataSourceConfig.getDataSourceName());
        }

        synchronized (allClients) {
            if (allClients.containsKey(dataSourceConfig.getDataSourceName())) {
                hoder = allClients.get(dataSourceConfig.getDataSourceName());
            } else {
                hoder = new MongoClientHoder();
                MongoClient client = createMongoCollection(dataSourceConfig);

                hoder.client = client;
                hoder.dataSourceConfig = dataSourceConfig;
                hoder.mongoDatabase = client.getDatabase(dataSourceConfig.getDbName());
            }
        }
        MongoCollection result = hoder.getCollection(tableName);
        return result;
    }


    private static MongoClient createMongoCollection(DataSourceConfig dataSourceConfig) {
        String dbName = dataSourceConfig.getDbName();
        String userName = dataSourceConfig.getUserName();
        String password = dataSourceConfig.getPassWord();
        String serverAdds = dataSourceConfig.getServerIp();
        int port = dataSourceConfig.getPort();

        ServerAddress ssAddress = new ServerAddress(serverAdds, port);
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).
                threadsAllowedToBlockForConnectionMultiplier(50).build();
        //
        List<ServerAddress> seeds = new ArrayList<ServerAddress>(1);
        seeds.add(ssAddress);

        MongoClient mongoClient = null;

        if (null != userName && 0 < userName.length() && null != password && 0 < password.length()) {
            //创建用户名密码
            List<MongoCredential> credentials = new ArrayList<MongoCredential>(1);
            //MongoCredential.createMongoCRCredential(userName, dbName, password.toCharArray());
            MongoCredential credential = MongoCredential.createCredential(userName, dbName, password.toCharArray());
            credentials.add(credential);
            //创建安全连接
            mongoClient = new MongoClient(seeds, credentials, options);
        } else {
            mongoClient = new MongoClient(ssAddress, options);
        }
        // 连接到数据库
//        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
//        MongoCollection collection = mongoDatabase.getCollection(tableName);
        return mongoClient;
    }

    private static final class MongoClientHoder {
        MongoClient client;
        MongoDatabase mongoDatabase;
        DataSourceConfig dataSourceConfig;

        //dbname -------------------> MongoCollection
        //Map<String, MongoCollection> mogoClientsMap = new LinkedHashMap<String, MongoCollection>(8);

        // tableName ------> MongoCollection
        Map<String, MongoCollection> mogoCollectMap = new HashMap<String, MongoCollection>(8);

        private void destory() {
            synchronized (this) {
                //清除所有的连接
                mogoCollectMap.clear();
                client = null;
                mongoDatabase = null;
                //关闭该数据源的
                if (client != null) {
                    client.close();
                }
            }
        }

        private MongoCollection getCollection(final String tableName) {
            if (mogoCollectMap.containsKey(tableName)) {
                return mogoCollectMap.get(tableName);
            }
            MongoCollection result = null;
            synchronized (this) {
                if (mogoCollectMap.containsKey(tableName)) {
                    return mogoCollectMap.get(tableName);
                }
                result = mongoDatabase.getCollection(tableName);
                mogoCollectMap.put(tableName, result);
            }
            return result;
        }
    }
}
