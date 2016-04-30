package org.castors.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ballandd on 30/04/2016.
 */
public class ViewDto {

    private String id;

    private String theme;

    private Map<String, String> images = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }
}
