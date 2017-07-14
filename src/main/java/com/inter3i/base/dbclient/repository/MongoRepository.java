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

package com.inter3i.base.dbclient.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

public class MongoRepository implements Repository<Map> {

    private String collectionName;
    private MongoCollection collection;

    public MongoRepository(MongoCollection collection, String collectionName) {
        this.collectionName = collectionName;
        this.collection = collection;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public MongoCollection getCollection() {
        return collection;
    }

    public void setCollection(MongoCollection collection) {
        this.collection = collection;
    }

    public Map findById(String id) {
        ObjectId objectId=new ObjectId(id);
        Bson filter = Filters.eq("_id", objectId);
        FindIterable iterable = collection.find(filter);
        Iterator<Map> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            Map data = iterator.next();
            return data;
        }
        return null;
    }

    public Iterable<Map> findAll() {
        FindIterable iterable = collection.find();
        return iterable;
    }

    public void insert(Collection<Map> entitys) {
        List list=new ArrayList();
        list.addAll(entitys);
        collection.insertMany(list);
    }

    public void save(Map entity) {
        collection.insertOne(entity);
    }

    public void removeById(String id) {
        ObjectId objectId=new ObjectId(id);
        Bson filter = Filters.eq("_id", objectId);
        DeleteResult result=collection.deleteOne(filter);
        System.out.print(result);
    }

    @Override
    public void updateById(Document document,ObjectId id) {
        if (document.isEmpty()) {
            throw new RuntimeException("updateById for mongo exceptino:[document is empty!]");
        }
        Bson filter = Filters.eq("_id", id);
        collection.updateOne(filter, new Document("$set", document));
    }
}
