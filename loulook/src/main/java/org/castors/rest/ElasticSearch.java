package org.castors.rest;

import feign.Param;
import feign.RequestLine;

import java.util.Map;

public interface ElasticSearch {

    @RequestLine("GET /products/external/{id_categorie}")
    String products(@Param("id_categorie") String idCategorie);

    //Test
    @RequestLine("POST vetements/external/_search?pretty'")
    String lichess();

}
