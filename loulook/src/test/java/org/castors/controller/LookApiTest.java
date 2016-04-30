package org.castors.controller;

import org.castors.dto.Theme;
import org.castors.dto.Vetement;
import org.castors.dto.ViewDto;
import org.castors.enumeration.LookIndex;
import org.castors.enumeration.VetementType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


/**
 * Created by ballandd on 30/04/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class LookApiTest {

    @Spy
    private LookApi lookApi = new LookApi();

    //search id sur index "theme" -> ... + mots ou masterPiece prio
    //search mots sur index "vetement" -> vetement correspondant / type
    //select 1 chaq type + masterpirce si present

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchMatchMultiThemeId() throws Exception {
        String id = "1";

        List<String> themeJson = new ArrayList<>();
        themeJson.add("{\"id\":\"1\", \"label\":\"les casters s'habille\"}");
        themeJson.add("{\"id\":\"1\", \"label\":\"les casters s'habille lol\"}");

        doReturn(themeJson).when(lookApi).searchMatch(LookIndex.THEME, id, "id");

        lookApi.lookById(id);
    }

    @Test
    public void testSearchMatch() throws Exception {
        String id = "1";

        List<String> themeJson = new ArrayList<>();
        themeJson.add("{\"id\":\"1\", \"label\":\"les casters s'habille\", \"keyWord1\":\"mot1\",\"keyWord2\":\"mot2\",\"keyWord3\":\"mot3\", \"masterPiece\":\"acc_2\"}");

        List<String> vetementsJson = new ArrayList<>();
        vetementsJson.add("{\"name\":\"acc_1\", \"type\":\"accessoire\", \"keyWord1\":\"mot1\", \"keyWord2\":\"mot2\", \"keyWord3\":\"mot3\"}");
        vetementsJson.add("{\"name\":\"acc_2\", \"type\":\"accessoire\", \"keyWord1\":\"mot1\", \"keyWord2\":\"mot2\", \"keyWord3\":\"mot3\"}");
        vetementsJson.add("{\"name\":\"bas_1\", \"type\":\"bas\", \"keyWord1\":\"mot1\", \"keyWord2\":\"mot2\", \"keyWord3\":\"mot3\"}");
        vetementsJson.add("{\"name\":\"bas_2\", \"type\":\"bas\", \"keyWord1\":\"mot1\", \"keyWord2\":\"mot2\", \"keyWord3\":\"mot3\"}");
        vetementsJson.add("{\"name\":\"haut_1\", \"type\":\"haut\", \"keyWord1\":\"mot1\", \"keyWord2\":\"mot2\", \"keyWord3\":\"mot3\"}");
        //vetementsJson.add("{\"name\":\"manteau_1\", \"type\":\"manteau\", \"keyWord1\":\"mot1\", \"keyWord2\":\"mot2\", \"keyWord3\":\"mot3\"}");
        vetementsJson.add("{\"name\":\"chaussures_1\", \"type\":\"chaussures\", \"keyWord1\":\"mot1\", \"keyWord2\":\"mot2\", \"keyWord3\":\"mot3\"}");

        doReturn(themeJson).when(lookApi).searchMatch(LookIndex.THEME, id, Theme.ID_NAME);
        doReturn(vetementsJson).when(lookApi).searchMatch(LookIndex.VETEMENT, "mot1 mot2 mot3 acc_2", Vetement.KEY_WORD_1, Vetement.KEY_WORD_2, Vetement.KEY_WORD_3, Vetement.NAME);

        ViewDto result = lookApi.lookById(id);

        verify(lookApi, times(1)).searchMatch(LookIndex.THEME, id, Theme.ID_NAME);
        verify(lookApi, times(1)).searchMatch(LookIndex.VETEMENT, "mot1 mot2 mot3 acc_2", Vetement.KEY_WORD_1, Vetement.KEY_WORD_2, Vetement.KEY_WORD_3, Vetement.NAME);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getTheme()).isEqualTo("les casters s'habille");

        assertThat(result.getImages().get(VetementType.ACCESSOIRE.getName())).isEqualTo("acc_2");
        assertThat(result.getImages().get(VetementType.BAS.getName())).startsWith("bas_");
        assertThat(result.getImages().get(VetementType.HAUT.getName())).isEqualTo("haut_1");
        assertThat(result.getImages().get(VetementType.MANTEAU.getName())).isNull();//isEqualTo("manteau_1");
        assertThat(result.getImages().get(VetementType.CHAUSSURES.getName())).isEqualTo("chaussures_1");
    }
}