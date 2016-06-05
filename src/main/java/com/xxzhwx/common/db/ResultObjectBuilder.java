package com.xxzhwx.common.db;

import org.bson.Document;

public interface ResultObjectBuilder<T> {
    T build(Document doc);
}
