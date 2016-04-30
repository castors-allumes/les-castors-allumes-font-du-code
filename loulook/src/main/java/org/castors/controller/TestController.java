package org.castors.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.castors.rest.ElasticSearch;
import org.castors.service.ElasticSearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by jauparts on 29/04/2016.
 */
@RestController
@RequestMapping("/looks")
public class TestController {

    private ElasticSearchService elasticSearchService;

    @RequestMapping(method = RequestMethod.GET, value = "/", produces = "application/json")
    public String looks(@RequestParam("fields") String fields, @RequestParam("filters") String filters) {
        return "looks_home";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    public String look(@PathVariable("id") Long id) {
        return id.toString();
    }

    //      Test
    @RequestMapping(method = RequestMethod.GET, value = "/vetements", produces = "application/json")
    public String feign() {
        elasticSearchService = new ElasticSearchService();
        Client client = null;
        try {
            client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
            SearchResponse sr = client.prepareSearch("vetements").setTypes("external").setSearchType(SearchType.DEFAULT)
                    .setQuery(QueryBuilders.matchQuery("Couleur", "Vert")).execute().actionGet();
            SearchHit[] results = sr.getHits().getHits();
            for (SearchHit hit : results) {
                String sourceAsString = hit.getSourceAsString();
                if (sourceAsString != null) {
                    return sourceAsString;
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
        return "";
    }

}
