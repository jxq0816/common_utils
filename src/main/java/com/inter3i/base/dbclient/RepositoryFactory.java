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
import com.inter3i.base.dbclient.exception.NonSupportException;
import com.inter3i.base.dbclient.repository.Repository;

public abstract class RepositoryFactory {

    public static RepositoryFactory get(RepositoryType repositoryType, DataSourceConfig connConfig) {
        switch (repositoryType) {
            case MONGODB:
                return MongoRepositoryFactory.getInstance();
            default:
                throw new NonSupportException("no supported repository type");
        }
    }

    public abstract Repository createRepository(DataSourceConfig connConfig, String tableName);

    public abstract void destory();
}
