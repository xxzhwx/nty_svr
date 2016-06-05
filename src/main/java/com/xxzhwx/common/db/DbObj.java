package com.xxzhwx.common.db;

import org.bson.Document;
import org.bson.conversions.Bson;

public interface DbObj {
    Document encode();

    void decode(Document doc);

    Bson buildUpdateFilter();
}
