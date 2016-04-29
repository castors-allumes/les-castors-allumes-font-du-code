package fr.ryu.followthing.service.persistence.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.mongojack.DBQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.el.MethodNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fr.ryu.followthing.utils.RuntimeUtils.isNotEmpty;

/**
 * Created by Ryuko on 25/12/2014.
 */
@Service
public class QueryParser {

    private final Pattern QUERY_PATTERN = Pattern.compile("([a-zA-Z0-9]+|[\\(\\)',])(.*)");

    @Autowired
    private ObjectMapper objectMapper;

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected class Query implements Iterator<String> {

        private String originalQuery;

        private String currentQuery;

        public Query(String query) {
            this.originalQuery = query;
            reset();
        }

        @Override
        public boolean hasNext() {
            Matcher queryM = QUERY_PATTERN.matcher(currentQuery);
            return queryM.matches();
        }

        @Override
        public String next() {
            Matcher queryM = QUERY_PATTERN.matcher(currentQuery);
            if (queryM.matches()) {
                currentQuery = queryM.group(2);
                return queryM.group(1);
            }
            return null;
        }

        public void reset() {
            currentQuery = originalQuery;
        }
    }

    //TODO rendre robust + test
    public DBQuery.Query parseQuery(String queryS) {
        isNotEmpty(queryS, IllegalArgumentException.class, "Query parameter can't be null or empty");

        Query query = new Query(queryS);

        Stack<Object> stack = new Stack<>();
        Object extract;
        while ((extract = query.next()) != null) {
            if (extract.equals(")")) {
                //pop all param
                List<Object> params = new ArrayList<>();
                while (!extract.equals("(")) {
                    params.add(stack.pop());
                    extract = stack.pop();
                    if (!extract.equals(",") && !extract.equals("("))
                        throw new IllegalArgumentException("Fail to extract method params expect : [,(]");
                }
                String methodName = (String) stack.pop();
                Collections.reverse(params);
                stack.push(executeMethod(methodName, params.toArray()));
            } else if (extract.equals("'")) {
                //extract String param
                String paramS = query.next();
                extract = query.next();
                if (extract == null || !extract.equals("'"))
                    throw new IllegalArgumentException("Fail to extract a String param expect : '");
                stack.push(paramS);
            } else {
                stack.push(extract);
            }
        }
        return (DBQuery.Query) stack.pop();
    }

    /**
     * Execute a methode of Class DBQuery.Query with its parameters
     *
     * @param methodName    name of method to execute
     * @param paramsOrdered arguments of method, must be in right order
     * @return a build DBQuery.Query
     */
    protected DBQuery.Query executeMethod(String methodName, Object[] paramsOrdered) {
        DBQuery.Query query = DBQuery.empty();


        Method[] methods = query.getClass().getMethods();
        Method methodToExecute = null;

        boolean ok = false;
        for (int m = 0; m < methods.length && !ok; m++) {
            Method aMethod = methods[m];
            //search for method name
            if (aMethod.getName().equals(methodName)) {
                //search for exact parameters
                if (paramsOrdered.length == aMethod.getParameterCount()) {
                    ok = true;
                    Class<?>[] methodParamsClass = aMethod.getParameterTypes();
                    for (int p = 0; p < methodParamsClass.length && ok; p++) {
                        ok &= methodParamsClass[p].isInstance(paramsOrdered[p]);
                    }
                    if (ok) {
                        methodToExecute = aMethod;
                    }
                }

                //may be a (Arrays... arrays) declaration
                if (!ok && aMethod.getParameterCount() == 1) {
                    Class<?> c = aMethod.getParameterTypes()[0];
                    if (c.isArray()) {
                        paramsOrdered = new Object[]{Arrays.copyOf(paramsOrdered, paramsOrdered.length, (Class<? extends Object[]>) c)};
                        if (c.isInstance(paramsOrdered[0])) {
                            methodToExecute = aMethod;
                        }
                    }
                }
            }
        }

        if (methodToExecute == null) {
            throw new MethodNotFoundException("No method " + methodName + " found " + paramsOrdered);
        }


        try {
            return (DBQuery.Query) methodToExecute.invoke(query, paramsOrdered);
        } catch (IllegalAccessException e) {
            new RuntimeException("Fail to execute method " + methodName, e);
        } catch (InvocationTargetException e) {
            new RuntimeException("Fail to execute method " + methodName, e);
        }
        return null;
    }

    public DBObject parseNativeQuery(String queryS) {
        isNotEmpty(queryS, IllegalArgumentException.class, "A query must be specified");

        try {
            Map query = objectMapper.readValue(queryS, Map.class);
            BasicDBObject dbObject = new BasicDBObject();
            dbObject.putAll(query);
            return dbObject;
        } catch (IOException e) {
            throw new IllegalArgumentException("Fail to parse native query ", e);
        }
    }
}
