package fr.ryu.followthing.dto.persistence;

import org.bson.types.ObjectId;
import org.mongojack.Id;

import java.util.List;
import java.util.Map;

public class MongoPojoSchema {

    @Id
    private ObjectId id;

    private String name;

    private String className;

    private String formSchema;

    private Map<String,String> queries;

    private Map<String,String> nativeQueries;

    public String getFormSchema() {
        return formSchema;
    }

    public void setFormSchema(String formSchema) {
        this.formSchema = formSchema;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Map<String, String> getQueries() {
        return queries;
    }

    public void setQueries(Map<String, String> queries) {
        this.queries = queries;
    }

    public Map<String, String> getNativeQueries() {
        return nativeQueries;
    }

    public void setNativeQueries(Map<String, String> nativeQueries) {
        this.nativeQueries = nativeQueries;
    }
}
