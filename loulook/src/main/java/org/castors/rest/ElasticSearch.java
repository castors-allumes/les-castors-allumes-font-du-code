package org.castors.rest;

import feign.Param;
import feign.RequestLine;

import java.util.Map;

public interface ElasticSearch {

    @RequestLine("GET /products/external/{id_categorie}")
    String products(@Param("id_categorie") String idCategorie);

    //Test
//    @RequestLine("GET /api/user/{user}/games?nb={nb}&page={page}")
//    Map<String, Object> lichess(@Param("user") String user, @Param("nb") String nb, @Param("page") String page);

}
