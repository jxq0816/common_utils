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

package com.inter3i.base.dbclient;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.conversions.Bson;

public class MongoAutoIncIdUtils {

    private final static String DEFAULT_IDS_TABLE_NAME = "ids";

    public static int incrIds(final MongoCollection collection, final String keyName, final String keyTableName) {
        //
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        //options.

        Bson filter = Filters.eq("name", "travel_notes1");

//        collection.findOneAndUpdate(filter,);
        return 0;
    }

    public static int incrIds(final MongoCollection collection, final String keyName) {
        return incrIds(collection, keyName, DEFAULT_IDS_TABLE_NAME);
    }
}
