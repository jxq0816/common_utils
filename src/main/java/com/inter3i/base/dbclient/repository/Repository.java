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

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Collection;

public interface Repository<E> {
    public E findById(String id);

    public Iterable<E> findAll();

    public void insert(Collection<E> entitys);

    public void save(E entity);

    public void removeById(String id);

    public void updateById(Document document,ObjectId id);

}
