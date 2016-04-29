package org.castors.controller;

import org.castors.rest.ElasticSearch;
import org.castors.service.ElasticSearchService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
//    @RequestMapping(method = RequestMethod.GET, value = "/t", produces = "application/json")
//    public Map<String, Object> feign() {
//        elasticSearchService = new ElasticSearchService();
//        ElasticSearch elasticSearch = elasticSearchService.build("http://fr.lichess.org/");
//        Map<String, Object> map = elasticSearch.lichess("toshihirofr2", "50", "1");
//        return map;
//    }

}
