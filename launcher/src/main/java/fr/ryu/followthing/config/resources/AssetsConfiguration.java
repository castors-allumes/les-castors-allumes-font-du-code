package fr.ryu.followthing.config.resources;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.*;

/**
 * Created by Ryuko on 20/12/2014.
 */
@Configuration
public class AssetsConfiguration implements ResourceLoaderAware {

    @Value("${ryu.assets.location}")
    private String assetsLocation;

    @Value("${ryu.assets.mapping}")
    private String assetsMapping;

    private ResourceLoader resourceLoader;

    @Bean
    public SimpleUrlHandlerMapping assetsHandlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MIN_VALUE + 1);
        ResourceHttpRequestHandler handler = assetsRequestHandler();
        Map<String,ResourceHttpRequestHandler> mappingMap = new HashMap<String, ResourceHttpRequestHandler>();
        mappingMap.put("/"+assetsMapping+"/**", handler);
        mapping.setUrlMap(mappingMap);
        return mapping;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public ResourceHttpRequestHandler assetsRequestHandler() {
        ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
        requestHandler.setLocations(getLocations());
        return requestHandler;
    }

    protected List<Resource> getLocations() {
        List<Resource> locations = new ArrayList<Resource>();
        Resource resource = resourceLoader.getResource("file:"+assetsLocation);
        locations.add(resource);
        return locations;
    }

}
