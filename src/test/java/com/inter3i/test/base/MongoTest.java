/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: Administrator
 * Created: 2017/05/15
 * Description:
 *
 */

package com.inter3i.test.base;

import com.inter3i.base.dbclient.MongoRepositoryFactory;
import com.inter3i.base.dbclient.config.DataSourceConfig;
import com.inter3i.base.dbclient.repository.MongoRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.*;

public class MongoTest {
    @Test
    public void findById() {
        DataSourceConfig config=new DataSourceConfig();
        config=config.initConfig();
        MongoRepositoryFactory factory=new MongoRepositoryFactory();
        MongoRepository repository= (MongoRepository) factory.createRepository(config,"jiang_test");
        Map rs=repository.findById("591998a049f3ef22a8dd84b2");
        System.out.println(rs);
    }

    @Test
    public void findAll() {
        DataSourceConfig config=new DataSourceConfig();
        config=config.initConfig();
        MongoRepositoryFactory factory=new MongoRepositoryFactory();
        MongoRepository repository= (MongoRepository) factory.createRepository(config,"data_01");
        Iterable<Map> rs= repository.findAll();
        Iterator<Map> iterator = rs.iterator();
        System.out.println(iterator.hasNext());
    }
    @Test
    public void insert() {
       List list=new ArrayList<>();
        Document map=new Document();
        map.put("name","jiang");
        Document m=new Document();
        m.put("age","25");
        list.add(map);
        list.add(m);
        DataSourceConfig config=new DataSourceConfig();
        config=config.initConfig();
        MongoRepositoryFactory factory=new MongoRepositoryFactory();
        MongoRepository repository= (MongoRepository) factory.createRepository(config,"jiang_test");
        repository.insert(list);
    }
    @Test
    public void save() {
        Document map=new Document();
        map.put("age","25");
        DataSourceConfig config=new DataSourceConfig();
        config=config.initConfig();
        MongoRepositoryFactory factory=new MongoRepositoryFactory();
        MongoRepository repository= (MongoRepository) factory.createRepository(config,"jiang_test");
        repository.save(map);
    }

    @Test
    public void removeById() {
        DataSourceConfig config=new DataSourceConfig();
        config=config.initConfig();
        MongoRepositoryFactory factory=new MongoRepositoryFactory();
        MongoRepository repository= (MongoRepository) factory.createRepository(config,"jiang_test");
        repository.removeById("5919987349f3ef3240bb52b7");
    }

    @Test
    public void updateById() {
        Document map=new Document();
        map.put("age","45");
        DataSourceConfig config=new DataSourceConfig();
        config=config.initConfig();
        MongoRepositoryFactory factory=new MongoRepositoryFactory();
        MongoRepository repository= (MongoRepository) factory.createRepository(config,"jiang_test");
        ObjectId id=new ObjectId("591998a049f3ef22a8dd84b2");
        repository.updateById(map,id);
    }
}
