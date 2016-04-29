package fr.ryu.followthing.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ryu.followthing.dto.persistence.MongoPojoSchema;
import fr.ryu.followthing.service.persistence.MongoDBPersistenceEngine;
import org.mongojack.DBQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.websocket.server.PathParam;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryuko on 20/12/2014.
 */

@RestController
@RequestMapping("chest")
public class ChestRestService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoDBPersistenceEngine persistenceEngine;

    @PostConstruct
    public void init() throws IOException {
        try {
            MongoPojoSchema mongoPojoSchema = persistenceEngine.findOne(MongoPojoSchema.class, DBQuery.is("name", "MongoPojoSchema"));

            if(mongoPojoSchema == null){
                Resource dataSchemaResource = resourceLoader.getResource("classpath:persistence/fr.ryu.followthing.dto.persistence.MongoPojoSchema.json");
                mongoPojoSchema = objectMapper.readValue(dataSchemaResource.getFile(), MongoPojoSchema.class);
                persistenceEngine.saveOrOverride(mongoPojoSchema);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/find/{type}")
    public List<Object> findThing(@PathParam("type") String type) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<Object> series = new ArrayList<Object>();
        return series;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/find/schema")
    public MongoPojoSchema findThingSchema(@RequestParam(value = "mongoPojoSchemaName", required = true) String mongoPojoSchemaName){
        return persistenceEngine.findOne(MongoPojoSchema.class, DBQuery.is("name", mongoPojoSchemaName));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/schema")
    public MongoPojoSchema saveThingSchema(@RequestBody MongoPojoSchema mongoPojoSchema){
        return  persistenceEngine.saveOrOverride(mongoPojoSchema);
    }
}
