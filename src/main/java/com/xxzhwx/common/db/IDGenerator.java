package com.xxzhwx.common.db;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

public class IDGenerator {
    private static IDGenerator I = new IDGenerator();
    private Database database = Database.getInstance();

    private IDGenerator() {
    }

    public static IDGenerator getInstance() {
        return I;
    }

    public long getNewId(String type) {
        Bson filter = Filters.eq("type", type);
        Document update = new Document();
        update.append("$inc", new Document("id", 1L));

        ID id = database.findOneAndUpdate("identifier", filter, update, ID.BUILDER);
        return id.get();
    }

    public static class ID {
        private long id;

        private ID(long id) {
            this.id = id;
        }

        public long get() {
            return id;
        }

        public static final ResultObjectBuilder<ID> BUILDER = doc -> {
            long id = doc.getLong("id");
            return new ID(id);
        };
    }
}
