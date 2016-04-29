package org.castors.service;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.castors.rest.ElasticSearch;

public class ElasticSearchService {

    public ElasticSearch build(String host) {
        return Feign.builder()
                .decoder(new JacksonDecoder())
                .target(ElasticSearch.class, host);
    }

}
