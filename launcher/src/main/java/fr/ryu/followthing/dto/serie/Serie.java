package fr.ryu.followthing.dto.serie;

import java.util.Date;
import java.util.List;

/**
 * Created by Ryuko on 20/12/2014.
 */
public class Serie {

    private String id;

    private String name;

    private String description;

    private Date broadcastingDate;

    private List<String> types;

    private List<Saison> saison;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBroadcastingDate() {
        return broadcastingDate;
    }

    public void setBroadcastingDate(Date broadcastingDate) {
        this.broadcastingDate = broadcastingDate;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<Saison> getSaison() {
        return saison;
    }

    public void setSaison(List<Saison> saison) {
        this.saison = saison;
    }
}

