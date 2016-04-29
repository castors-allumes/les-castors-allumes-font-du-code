package fr.ryu.followthing.service.persistence.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mongojack.DBQuery;
import org.mongojack.internal.query.CompoundQueryCondition;
import org.mongojack.internal.query.QueryCondition;

import javax.el.MethodNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class QueryParserTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    private QueryParser queryParser = new QueryParser();

    private static final String NATIVE_QUERY_0 = "{}";
    private static final String NATIVE_QUERY_1 = "{\"aField\": { \"$exists\": true}}";
    private static final String NATIVE_QUERY_2 = "{\"aField\": \"aValue\"}";
    private static final String NATIVE_QUERY_3 = "{\"$and\": [{\"aField\": {\"$exists\": true}}, {\"aField\": \"aValue\"}]}";
    private static final String JACK_QUERY_1 = "exists('aField')";


    @Before
    public void setUp() throws Exception {
        queryParser.setObjectMapper(objectMapper);
        assertThat(queryParser.getObjectMapper()).isNotNull();
        assertThat(queryParser.getObjectMapper()).isEqualTo(objectMapper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNativeQueryNullQuery() throws Exception {
        queryParser.parseNativeQuery(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNativeQueryFailParse() throws Exception {
        queryParser.parseNativeQuery("fail to parse it");
    }

    @Test
    public void testParseNativeQuery0() throws Exception {
        DBObject result = queryParser.parseNativeQuery(NATIVE_QUERY_0);

        assertThat(result).isNotNull();

        Map map = result.toMap();
        assertThat(map).isEmpty();
    }

    @Test
    public void testParseNativeQuery1() throws Exception {
        DBObject result = queryParser.parseNativeQuery(NATIVE_QUERY_1);

        assertThat(result).isNotNull();

        Map map = result.toMap();
        assertThat(map).isNotEmpty();
        assertThat(map).hasSize(1);
        assertThat(map.containsKey("aField")).isTrue();
        assertThat(map.get("aField")).isInstanceOf(Map.class);

        map = (Map) map.get("aField");

        assertThat(map).isNotEmpty();
        assertThat(map).hasSize(1);
        assertThat(map.containsKey("$exists")).isTrue();
        assertThat(map.get("$exists")).isEqualTo(true);
    }

    @Test
    public void testParseNativeQuery2() throws Exception {
        DBObject result = queryParser.parseNativeQuery(NATIVE_QUERY_2);

        assertThat(result).isNotNull();

        Map map = result.toMap();
        assertThat(map).isNotEmpty();
        assertThat(map).hasSize(1);
        assertThat(map.containsKey("aField")).isTrue();
        assertThat(map.get("aField")).isEqualTo("aValue");
    }

    @Test
    public void testParseNativeQuery3() throws Exception {
        DBObject result = queryParser.parseNativeQuery(NATIVE_QUERY_3);

        assertThat(result).isNotNull();

        Map map = result.toMap();
        assertThat(map).isNotEmpty();
        assertThat(map).hasSize(1);
        assertThat(map.containsKey("$and")).isTrue();
        assertThat(map.get("$and")).isInstanceOf(List.class);

        List list = (List) map.get("$and");

        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
        assertThat(list).hasSize(2);
        assertThat(list.get(0)).isInstanceOf(Map.class);

        assertThat(list.get(1)).isInstanceOf(Map.class);
        map = (Map) list.get(1);
        assertThat(map).hasSize(1);
        assertThat(map.containsKey("aField")).isTrue();
        assertThat(map.get("aField")).isEqualTo("aValue");


        map = (Map) list.get(0);
        assertThat(map).hasSize(1);
        assertThat(map.containsKey("aField")).isTrue();
        assertThat(map.get("aField")).isInstanceOf(Map.class);

        map = (Map) map.get("aField");

        assertThat(map).isNotEmpty();
        assertThat(map).hasSize(1);
        assertThat(map.containsKey("$exists")).isTrue();
        assertThat(map.get("$exists")).isEqualTo(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseQueryNull() throws Exception {
        queryParser.parseQuery(null);
    }

    @Test(expected = MethodNotFoundException.class)
    public void testParseQueryMethodNotExist() throws Exception {
        String query = "methoddoesnotexist('truck')";
        queryParser.parseQuery(query);
    }

    @Test
    public void testParseQueryMethod1() throws Exception {
        String query = "exists('truck')";
        DBQuery.Query result = queryParser.parseQuery(query);

        assertThat(result).isNotNull();
        Set<Map.Entry<String, QueryCondition>> conditions = result.conditions();
        assertThat(conditions).isNotEmpty();
        assertThat(conditions).hasSize(1);
        Map.Entry<String, QueryCondition> condition = conditions.iterator().next();
        assertThat(condition.getKey()).isEqualTo("truck");
        CompoundQueryCondition c = (CompoundQueryCondition) (condition.getValue());
        assertThat(c.getQuery().conditions().iterator().next().getKey()).isEqualTo("$exists");
    }


}