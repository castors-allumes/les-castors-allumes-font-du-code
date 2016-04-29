package fr.ryu.followthing.dto.serie;

import java.util.Date;
import java.util.List;

/**
 * Created by Ryuko on 20/12/2014.
 */
public class Saison {

    private String id;

    private String name;

    private String description;

    private Date broadcastingDate;

    private List<Episode> episodes;

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

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}
