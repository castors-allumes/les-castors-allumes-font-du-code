package fr.ryu.followthing.service.persistence;


import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import fr.ryu.followthing.service.persistence.query.QueryParser;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.mongojack.DBCursor;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBPersistenceEngineTest {



    @Mock
    private MongoClient mongoClient;

    @Mock
    private DB dataBase;

    private String dataBaseName = "aDbName";

    @Spy
    private MongoDBPersistenceEngine mongoPersistenceEngine = new MongoDBPersistenceEngine();

    @Before
    public void setUp() throws Exception {
        mongoPersistenceEngine.setDataBaseName(dataBaseName);
        mongoPersistenceEngine.setMongoClient(mongoClient);
    }

    @Test
    public void testGetMongoClient() throws Exception {
        assertThat(mongoPersistenceEngine.getMongoClient()).isNotNull();
        assertThat(mongoPersistenceEngine.getMongoClient()).isEqualTo(mongoClient);
    }

    @Test
    public void testGetDataBaseName() throws Exception {
        assertThat(mongoPersistenceEngine.getDataBaseName()).isNotNull();
        assertThat(mongoPersistenceEngine.getDataBaseName()).isEqualTo(dataBaseName);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDataBaseNoDbName() throws Exception {
        mongoPersistenceEngine.getDataBase(null);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDataBaseBadDbName() throws Exception {
        mongoPersistenceEngine.getDataBase("");
    }

    @Test
    public void testGetDataBase() throws Exception {
        when(mongoClient.getDB(dataBaseName)).thenReturn(dataBase);

        DB result = mongoPersistenceEngine.getDataBase();

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        verify(mongoClient, atLeastOnce()).getDB(stringCaptor.capture());

        assertThat(stringCaptor.getValue()).isNotNull();
        assertThat(stringCaptor.getValue()).isEqualTo(dataBaseName);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(dataBase);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDbCollectionNullDataBase() throws Exception {
        DB dataBase = null;
        mongoPersistenceEngine.getDbCollection(dataBase, MongoObjectTest.class);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDbCollectionNullClass() throws Exception {
        mongoPersistenceEngine.getDbCollection(dataBase, MongoObjectTest.class);
    }

    @Test(expected = RuntimeException.class)
    public void testGetDbCollectionNoCollectionFound() throws Exception {
        Class<MongoObjectTest> classT = MongoObjectTest.class;
        when(dataBase.getCollection(classT.getName())).thenReturn(null);

        mongoPersistenceEngine.getDbCollection(dataBase, classT);
    }

    @Test
    public void testGetDbCollection() throws Exception {
        DBCollection objectCollection = mock(DBCollection.class);
        when(dataBase.getCollection(MongoObjectTest.class.getName())).thenReturn(objectCollection);
        when(objectCollection.getDB()).thenReturn(dataBase);

        JacksonDBCollection<MongoObjectTest, ObjectId> result = mongoPersistenceEngine.getDbCollection(dataBase, MongoObjectTest.class);

        verify(dataBase, atLeastOnce()).getCollection(MongoObjectTest.class.getName());

        assertThat(result).isNotNull();
        assertThat(result.getDB()).isEqualTo(dataBase);
        assertThat(result.getDbCollection()).isEqualTo(objectCollection);
    }

    @Test
    public void testGetDbCollectionWithDataBaseName() throws Exception {
        JacksonDBCollection<Object, Long> collection = mock(JacksonDBCollection.class);

        doReturn(collection).when(mongoPersistenceEngine).getDbCollection(dataBase, MongoObjectTest.class);
        doReturn(dataBase).when(mongoPersistenceEngine).getDataBase(dataBaseName);

        JacksonDBCollection<MongoObjectTest, ObjectId> result = mongoPersistenceEngine.getDbCollection(dataBaseName, MongoObjectTest.class);

        verify(mongoPersistenceEngine, atLeastOnce()).getDataBase(dataBaseName);
        verify(mongoPersistenceEngine, atLeastOnce()).getDbCollection(dataBase, MongoObjectTest.class);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(collection);
    }

    @Test
    public void testGetDbCollectionWithDataBaseNameDefault() throws Exception {
        JacksonDBCollection<MongoObjectTest, Long> collection = mock(JacksonDBCollection.class);

        doReturn(collection).when(mongoPersistenceEngine).getDbCollection(dataBase, MongoObjectTest.class);
        doReturn(dataBase).when(mongoPersistenceEngine).getDataBase();

        JacksonDBCollection<MongoObjectTest, ObjectId> result = mongoPersistenceEngine.getDbCollection(MongoObjectTest.class);

        verify(mongoPersistenceEngine, atLeastOnce()).getDataBase();
        verify(mongoPersistenceEngine, atLeastOnce()).getDbCollection(dataBase, MongoObjectTest.class);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(collection);
    }

    @Test
    public void testSaveOrOverride() throws Exception {
        MongoObjectTest object = new MongoObjectTest();
        JacksonDBCollection<MongoObjectTest, ObjectId> collection = mock(JacksonDBCollection.class);
        WriteResult<MongoObjectTest, ObjectId> writeResult = mock(WriteResult.class);

        doReturn(collection).when(mongoPersistenceEngine).getDbCollection(MongoObjectTest.class);
        when(collection.save(object)).thenReturn(writeResult);
        when(writeResult.getSavedObject()).thenReturn(object);

        MongoObjectTest result = mongoPersistenceEngine.saveOrOverride(object);

        verify(mongoPersistenceEngine, atLeastOnce()).getDbCollection(MongoObjectTest.class);
        verify(collection, atLeastOnce()).save(object);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(object);
    }

    @Test(expected = RuntimeException.class)
    public void testSaveOrOverrideNullObject() throws Exception {
        mongoPersistenceEngine.saveOrOverride(null);
    }


    @Test(expected = RuntimeException.class)
    public void testFindOneNullQuery() throws Exception {
        mongoPersistenceEngine.findOne(MongoObjectTest.class, null);
    }

    @Test
    public void testFindOne() throws Exception {
        DBQuery.Query query = mock(DBQuery.Query.class);
        JacksonDBCollection<Object, ObjectId> collection = mock(JacksonDBCollection.class);
        MongoObjectTest object = mock(MongoObjectTest.class);

        doReturn(collection).when(mongoPersistenceEngine).getDbCollection(MongoObjectTest.class);
        when(collection.findOne(query)).thenReturn(object);

        MongoObjectTest result = mongoPersistenceEngine.findOne(MongoObjectTest.class, query);

        verify(mongoPersistenceEngine, atLeastOnce()).getDbCollection(MongoObjectTest.class);
        verify(collection, atLeastOnce()).findOne(query);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(object);
    }

    @Test(expected = RuntimeException.class)
    public void testFindNullQuery() throws Exception {
        mongoPersistenceEngine.find(Object.class,null);
    }

    @Test
    public void testFindEmptyFind() throws Exception {
        DBQuery.Query query = mock(DBQuery.Query.class);
        DBCursor<MongoObjectTest> queryResult = mock(DBCursor.class);

        JacksonDBCollection<MongoObjectTest, ObjectId> collection = mock(JacksonDBCollection.class);
        doReturn(collection).when(mongoPersistenceEngine).getDbCollection(MongoObjectTest.class);
        when(collection.find(query)).thenReturn(queryResult);

        when(queryResult.hasNext()).thenReturn(false);

        List<MongoObjectTest> result = mongoPersistenceEngine.find(MongoObjectTest.class, query);

        verify(queryResult, atLeastOnce()).hasNext();
        verify(queryResult, atLeast(0)).next();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    public void testFindFoundSomething() throws Exception {
        DBQuery.Query query = mock(DBQuery.Query.class);
        DBCursor<MongoObjectTest> queryResult = mock(DBCursor.class);
        MongoObjectTest object1 = mock(MongoObjectTest.class);
        MongoObjectTest object2 = mock(MongoObjectTest.class);


        JacksonDBCollection<MongoObjectTest, ObjectId> collection = mock(JacksonDBCollection.class);
        doReturn(collection).when(mongoPersistenceEngine).getDbCollection(MongoObjectTest.class);
        when(collection.find(query)).thenReturn(queryResult);

        when(queryResult.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(queryResult.next()).thenReturn(object1).thenReturn(object2);

        List<MongoObjectTest> result = mongoPersistenceEngine.find(MongoObjectTest.class, query);

        verify(queryResult, atLeast(3)).hasNext();
        verify(queryResult, atLeast(2)).next();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isNotNull();
        assertThat(result.get(0)).isEqualTo(object1);
        assertThat(result.get(1)).isNotNull();
        assertThat(result.get(1)).isEqualTo(object2);
    }

}