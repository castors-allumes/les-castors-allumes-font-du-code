package org.castors.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.castors.dto.Theme;
import org.castors.dto.Vetement;
import org.castors.dto.ViewDto;
import org.castors.enumeration.LookIndex;
import org.castors.enumeration.VetementType;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Created by jauparts on 29/04/2016.
 */
@RestController
@RequestMapping("/looks")
public class LookApi {

    private static final String[] STOP_WORDS = {"pour","votre","d","de","un","une","a","le","la","avec","du","en","c","ou","mon","et","des","les","l","je","ce"};

    private final List<String> stopWordsList = new ArrayList<>();

    private Random random = new Random();

    private static final String HOST = "localhost";

    private static int PORT = 9300;

    private static final String ELASTICSEARCH_INDEX_TYPE = "external";

    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET, value = "/", produces = "application/json")
    public String looks(@RequestParam("fields") String fields, @RequestParam("filters") String filters) {
        return "looks_home";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ViewDto lookById(@PathVariable("id") String id) {
        List<String> themesJson = searchMatch(LookIndex.THEME, id, Theme.ID_NAME);
        List<Theme> themes = transformToBeanList(themesJson, Theme.class);

        if (themes.size() > 1) {
            throw new IllegalArgumentException(format("Multi theme found with id %s", id));
        }

        Theme theme = themes.get(0);

        ViewDto viewDto = new ViewDto();
        viewDto.setId(id);
        viewDto.setTheme(theme.getNom());

        StringBuilder text = new StringBuilder();
        text.append(theme.getKeyWord1()).append(" ");
        text.append(theme.getKeyWord2()).append(" ");
        text.append(theme.getKeyWord3());

        if (theme.getMasterPiece() != null && !theme.getMasterPiece().isEmpty()) {
            text.append(" ").append(theme.getMasterPiece());
        }

        List<String> vetementsJson = searchMatch(LookIndex.VETEMENT, text.toString(), Vetement.KEY_WORD_1, Vetement.KEY_WORD_2, Vetement.KEY_WORD_3, Vetement.NAME);
        List<Vetement> vetements = transformToBeanList(vetementsJson, Vetement.class);

        Vetement masterPieceVetement = null;
        if (theme.getMasterPiece() != null && !theme.getMasterPiece().isEmpty()) {
            masterPieceVetement = vetements.stream().filter(v -> v.getNom().equals(theme.getMasterPiece())).findFirst().get();
            viewDto.getImages().put(masterPieceVetement.getType(), masterPieceVetement.getNom());
        }

        List<String> masterPieceVetementS = new ArrayList<>();
        if(masterPieceVetement != null) {
            masterPieceVetementS.add(masterPieceVetement.getType());
        }
        addOneVetementOfType(viewDto, masterPieceVetementS, vetements, VetementType.ACCESSOIRE);
        addOneVetementOfType(viewDto, masterPieceVetementS, vetements, VetementType.HAUT);
        addOneVetementOfType(viewDto, masterPieceVetementS, vetements, VetementType.MANTEAU);
        addOneVetementOfType(viewDto, masterPieceVetementS, vetements, VetementType.BAS);
        addOneVetementOfType(viewDto, masterPieceVetementS, vetements, VetementType.CHAUSSURES);

        return viewDto;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/", consumes = "application/json")
    public ViewDto lookByKeyWorlds(List<String> themeWordsWithStopWords) {
        List<String> themeWords = themeWordsWithStopWords.stream().filter(s -> !getStopWordsList().contains(s)).collect(Collectors.toList());

        StringBuilder themeWord = new StringBuilder();
        Iterator<String> themeWordsI = themeWords.iterator();
        while(themeWordsI.hasNext()) {
            if(themeWord.length() != 0) {
                themeWord.append(" ");
            }
            themeWord.append(themeWordsI.next());
        }

        List<String> themesJson = searchMatch(LookIndex.THEME, themeWord.toString(), Theme.LABEL);
        List<Theme> themes = transformToBeanList(themesJson, Theme.class);

        ViewDto viewDto = new ViewDto();
        viewDto.setId("WOW");
        viewDto.setTheme(themeWord.toString());

        StringBuilder text = new StringBuilder();
        for(Theme theme : themes) {
            if(text.length() != 0) {
                text.append(" ");
            }

            text.append(theme.getKeyWord1()).append(" ");
            text.append(theme.getKeyWord2()).append(" ");
            text.append(theme.getKeyWord3());

            if (theme.getMasterPiece() != null && !theme.getMasterPiece().isEmpty()) {
                text.append(" ").append(theme.getMasterPiece());
            }
        }

        List<String> vetementsJson = searchMatch(LookIndex.VETEMENT, text.toString(), Vetement.KEY_WORD_1, Vetement.KEY_WORD_2, Vetement.KEY_WORD_3, Vetement.NAME);
        List<Vetement> vetements = transformToBeanList(vetementsJson, Vetement.class);

        List<String> masterPieceVetementS = themes.stream().map(t -> t.getMasterPiece()).collect(Collectors.toList());
        if (!masterPieceVetementS.isEmpty()) {
            List<Vetement>  masterPieceVetements = vetements.stream().filter(v -> masterPieceVetementS.contains(v.getNom())).collect(Collectors.toList());

            for(Vetement masterPieceVetement : masterPieceVetements) {
                viewDto.getImages().put(masterPieceVetement.getType(), masterPieceVetement.getNom());
            }
        }

        addOneVetementOfType(viewDto, masterPieceVetementS, vetements, VetementType.ACCESSOIRE);
        addOneVetementOfType(viewDto, masterPieceVetementS, vetements, VetementType.HAUT);
        addOneVetementOfType(viewDto, masterPieceVetementS, vetements, VetementType.MANTEAU);
        addOneVetementOfType(viewDto, masterPieceVetementS, vetements, VetementType.BAS);
        addOneVetementOfType(viewDto, masterPieceVetementS, vetements, VetementType.CHAUSSURES);

        return viewDto;
    }

    protected void addOneVetementOfType(ViewDto viewDto, List<String> masterPieceVetement, List<Vetement> vetements, VetementType vetementType) {
        if (masterPieceVetement != null && !masterPieceVetement.contains(vetementType.getName())) {
            List<Vetement> chaussures = filter(vetements, vetementType);
            if(chaussures.size() > 0) {
                int choice = random.nextInt(chaussures.size());
                viewDto.getImages().put(vetementType.getName(), chaussures.get(choice).getNom());
            }
        }
    }

    protected List<Vetement> filter(List<Vetement> vetements, VetementType vetementType) {
        return vetements.stream().filter(v -> v.getType().equals(vetementType.getName())).collect(Collectors.toList());
    }

    protected List<String> searchMatch(LookIndex lookIndex, String fieldValue, String... fieldNames) {
        Client client = null;
        List<String> resultJson = new ArrayList<>();
        try {
            client = createElasticSearchClient();
            SearchRequestBuilder searchRequest = createSearchResponse(client, lookIndex, QueryBuilders.multiMatchQuery(fieldValue, fieldNames));
            SearchResponse searchResponse = searchRequest.execute().actionGet();
            resultJson = hitsToListString(searchResponse);
        } finally {
            if (client != null) {
                client.close();
            }
        }

        return resultJson;
    }


    protected <T> List<T> transformToBeanList(List<String> beansJson, Class<T> beanClass) {
        List<T> beans = new ArrayList<>();
        for (String beanJson : beansJson) {
            T bean = transformToBean(beanJson, beanClass);
            if (bean != null) {
                beans.add(bean);
            }
        }
        return beans;
    }

    protected <T> T transformToBean(String value, Class<T> beanClass) {
        T bean = null;
        try {
            bean = objectMapper.readValue(value, beanClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }

    protected List<String> hitsToListString(SearchResponse searchResponse) {
        List<String> result = new ArrayList<>();
        SearchHit[] results = searchResponse.getHits().getHits();
        for (SearchHit hit : results) {
            String sourceAsString = hit.getSourceAsString();
            if (sourceAsString != null) {
                result.add(sourceAsString);
            }
        }
        return result;
    }

    protected SearchRequestBuilder createSearchResponse(Client client, LookIndex lookIndex, QueryBuilder queryBuilder) {
        return client.prepareSearch(lookIndex.getName()).setTypes(ELASTICSEARCH_INDEX_TYPE).setSearchType(SearchType.DEFAULT).setQuery(queryBuilder);
    }

    protected Client createElasticSearchClient() {
        InetSocketTransportAddress address;
        try {
            address = new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        if (address == null) {
            throw new RuntimeException("fail to create socket transport");
        }

        return TransportClient.builder().build().addTransportAddress(address);
    }


    public List<String> getStopWordsList() {
        if(stopWordsList == null) {
            for(String s : STOP_WORDS) {
                stopWordsList.add(s);
            }
        }
        return stopWordsList;
    }
}
