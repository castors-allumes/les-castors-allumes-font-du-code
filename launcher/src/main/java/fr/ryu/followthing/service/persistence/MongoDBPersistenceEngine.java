package fr.ryu.followthing.service.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import fr.ryu.followthing.dto.persistence.MongoPojoSchema;
import fr.ryu.followthing.service.persistence.query.QueryParser;
import org.bson.types.ObjectId;
import org.mongojack.DBCursor;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.ryu.followthing.utils.RuntimeUtils.isNotEmpty;
import static fr.ryu.followthing.utils.RuntimeUtils.isNotNull;
import static java.lang.String.format;

//TODO query dans la BDD/FILE MongoQuery {mongoObjectClass: "", queries: []}

/**
 * Created by Ryuko on 21/12/2014.
 */
@Service
public class MongoDBPersistenceEngine {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QueryParser queryParser;

    @Autowired
    private MongoClient mongoClient;

    @Value("${ryu.persistence.mongodb.db.name}")
    private String dataBaseName;

    /**
     * Save or override a Pojo
     *
     * @param pojo pojo to save
     * @param <T>  Class of the pojo
     * @return the saved pojo
     */
    public <T> T saveOrOverride(T pojo) {
        isNotNull(pojo);

        JacksonDBCollection<T, ObjectId> dbCollection = getDbCollection((Class<T>) pojo.getClass());
        WriteResult<T, ObjectId> saveResult = dbCollection.save(pojo);
        return saveResult.getSavedObject();
    }


    public <T> T findOne(Class<T> pojoClass, DBQuery.Query query) {
        isNotNull(query);

        JacksonDBCollection<T, ObjectId> dbCollection = getDbCollection(pojoClass);

        return dbCollection.findOne(query);
    }

    public <T> List<T> find(Class<T> pojoClass, DBQuery.Query query) {
        isNotNull(query);

        JacksonDBCollection<T, ObjectId> dbCollection = getDbCollection(pojoClass);

        DBCursor<T> pojoCursor = dbCollection.find(query);

        List<T> pojoList = new ArrayList<>();
        while (pojoCursor.hasNext()) {
            pojoList.add(pojoCursor.next());
        }
        return pojoList;
    }

    public <T> List<T> findByQueryName(Class<T> pojoClass, String queryName, Map<String, Object> parameters) {
        return null;
    }

    public <T> T findOneByQueryName(Class<T> pojoClass, String queryName, Map<String, Object> parameters) {
        return null;
    }

    //TODO test
    protected <T> DBQuery.Query retrieveQuery(Class<T> pojoClass, String queryName, Map<String, Object> parameters){
        try {
            Resource dataSchemaResource = resourceLoader.getResource(format("classpath:persistence/%s.json",pojoClass.getName()));

            MongoPojoSchema mongoPojoSchema = null;
            if(dataSchemaResource.exists()){
                mongoPojoSchema = objectMapper.readValue(dataSchemaResource.getFile(), MongoPojoSchema.class);
            } else {
                Map<String,Object> param = new HashMap<>();
                param.put("className", pojoClass.getName());
                mongoPojoSchema = findOneByQueryName(MongoPojoSchema.class, "findByClassName", param);
            }

            String query = mongoPojoSchema.getQueries().get(queryName);
            isNotEmpty(query);

            return queryParser.parseQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO to test and complete
    //    public <T> T remove(Class<T> pojoClass, DBQuery.Query query) {
    //        JacksonDBCollection<T, ObjectId> dbCollection = getDbCollection(pojoClass);
    //        WriteResult<T, ObjectId> removeResult = dbCollection.remove(query);
    //        return removeResult.getSavedObject();
    //    }

    /**
     * Get mongo db Client
     *
     * @return a singleton of mongo db client
     */
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    /**
     * get data base name config by default with property ${ryu.persistence.mongodb.db.name}
     *
     * @return data base name
     */
    public String getDataBaseName() {
        return dataBaseName;
    }

    /**
     * retrieve mongodb DB with specific name
     * the name of database is ${ryu.persistence.mongodb.db.name}
     *
     * @return the DB with name ${ryu.persistence.mongodb.db.name} (can't be null)
     */
    public DB getDataBase() {
        return getDataBase(getDataBaseName());
    }

    /**
     * retrieve mongodb DB with specific name
     *
     * @param dbName the name of database to retrieve or create
     * @return the DB with name dataBaseName (can't be null)
     */
    protected DB getDataBase(String dbName) {
        isNotEmpty(dbName);

        DB dataBase = getMongoClient().getDB(getDataBaseName());
        isNotNull(dataBase);

        return dataBase;
    }

    /**
     * Create or retrieve a DBcollection for class in database config by default
     *
     * @param thingClass class to map with collection
     * @param <T>        Type of class to map with collection
     * @return a jackson db collection wrapper
     */
    public <T> JacksonDBCollection<T, ObjectId> getDbCollection(Class<T> thingClass) {
        return getDbCollection(getDataBase(), thingClass);
    }

    /**
     * Create or retrieve a DBcollection for class in specific database name
     *
     * @param dataBaseName mongo database name
     * @param thingClass   class to map with collection
     * @param <T>          Type of class to map with collection
     * @return a jackson db collection wrapper
     */
    protected <T> JacksonDBCollection<T, ObjectId> getDbCollection(String dataBaseName, Class<T> thingClass) {
        return getDbCollection(getDataBase(dataBaseName), thingClass);
    }

    /**
     * Create or retrieve a DBcollection for class in database
     *
     * @param dataBase   mongo database
     * @param thingClass class to map with collection
     * @param <T>        Type of class to map with collection
     * @return a jackson db collection wrapper
     */
    protected <T> JacksonDBCollection<T, ObjectId> getDbCollection(DB dataBase, Class<T> thingClass) {
        isNotNull(dataBase);
        isNotNull(thingClass);

        DBCollection thingCollection = dataBase.getCollection(thingClass.getName());

        isNotNull(thingCollection);

        return JacksonDBCollection.wrap(thingCollection, thingClass, ObjectId.class);
    }

    protected void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    protected void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }


}
