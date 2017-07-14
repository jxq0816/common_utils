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

package com.inter3i.base.dbclient;

import com.inter3i.base.dbclient.config.DataSourceConfig;
import com.inter3i.base.dbclient.repository.MongoDBDatasources;
import com.inter3i.base.dbclient.repository.MongoRepository;
import com.inter3i.base.dbclient.repository.Repository;
import com.mongodb.client.MongoCollection;

/**
 * 每个factory 对应一个数据源
 */
public class MongoRepositoryFactory extends RepositoryFactory {
    private static MongoRepositoryFactory instance = null;

    public static final RepositoryFactory getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (MongoRepositoryFactory.class) {
            if (instance != null) {
                return instance;
            }
            // instance is null
            instance = new MongoRepositoryFactory();
            return instance;
        }
    }

    public static void destoryInstance() {
        if (null != instance) {
            instance.destory();
        }
    }

    public MongoRepositoryFactory() {
    }

    public Repository createRepository(DataSourceConfig dataSourceConfig, String tableName) {
        MongoCollection collection = MongoDBDatasources.getMongoCollection(dataSourceConfig, tableName);
        return new MongoRepository(collection, tableName);
    }

    public void destory() {
        MongoDBDatasources.destory();
    }
}
