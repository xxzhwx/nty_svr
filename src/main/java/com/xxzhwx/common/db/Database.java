package com.xxzhwx.common.db;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Database {
    private static final Database I = new Database();

    private Database() {
    }

    public static Database getInstance() {
        return I;
    }

    public static final String OPT_SET = "$set";

    private MongoClient client;
    private MongoDatabase db;

    public void init(String host, int port, String dbName) {
        client = new MongoClient(host, port);
        db = client.getDatabase(dbName);
        System.out.println("mongodb db name: " + db.getName());
    }

    /**
     * Example: find("user", Filters.eq("userId", 10001), User.BUILDER);
     */
    public <T> List<T> find(String collectionName, Bson filter,
                            ResultObjectBuilder<T> builder) {
        MongoCollection<Document> coll = db.getCollection(collectionName);

        // TODO find().projection(...)
        FindIterable<Document> findResult = coll.find(filter);
        MongoCursor<Document> it = findResult.iterator();

        List<T> dataList = new ArrayList<>();
        while (it.hasNext()) {
            Document doc = it.next();
            dataList.add(builder.build(doc));
        }
        return dataList;
    }

    public <T> T findOne(String collectionName, Bson filter,
                         ResultObjectBuilder<T> builder) {
        List<T> dataList = find(collectionName, filter, builder);
        if (dataList.isEmpty()) {
            return null;
        }
        return dataList.get(0);
    }

    public <T> List<T> findAll(String collectionName,
                               ResultObjectBuilder<T> builder) {
        MongoCollection<Document> coll = db.getCollection(collectionName);

        FindIterable<Document> findResult = coll.find();
        MongoCursor<Document> it = findResult.iterator();

        List<T> dataList = new ArrayList<>();
        while (it.hasNext()) {
            Document doc = it.next();
            dataList.add(builder.build(doc));
        }
        return dataList;
    }

    public <T> T findOneAndUpdate(String collectionName, Bson filter,
                                  Bson update, ResultObjectBuilder<T> builder) {
        MongoCollection<Document> coll = db.getCollection(collectionName);

        FindOneAndUpdateOptions opts = new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)
                .upsert(true);

        Document doc = coll.findOneAndUpdate(filter, update, opts);
        return builder.build(doc);
    }

    public void insert(String collectionName, DbObj obj) {
        MongoCollection<Document> coll = db.getCollection(collectionName);
        coll.insertOne(obj.encode());
    }

    public void insert(String collectionName, List<DbObj> objList) {
        MongoCollection<Document> coll = db.getCollection(collectionName);
        coll.insertMany(objList.stream().map(DbObj::encode)
                .collect(Collectors.toList()));
    }

    public int delete(String collectionName, Bson filter) {
        MongoCollection<Document> coll = db.getCollection(collectionName);
        DeleteResult result = coll.deleteMany(filter);
        return (int) result.getDeletedCount();
    }

    public int update(String collectionName, DbObj obj) {
        MongoCollection<Document> coll = db.getCollection(collectionName);

        Bson filter = obj.buildUpdateFilter();
        Document update = new Document(OPT_SET, obj.encode());
        UpdateOptions opts = new UpdateOptions().upsert(true);

        UpdateResult result = coll.updateOne(filter, update, opts);
        return (int) result.getModifiedCount();
    }

    public int update(String collectionName, List<DbObj> objList) {
        long count = 0;
        for (DbObj obj : objList) {
            count += update(collectionName, obj);
        }
        return (int) count;
    }

    public void destroy() {
        if (client != null) {
            client.close();
        }
    }
}
