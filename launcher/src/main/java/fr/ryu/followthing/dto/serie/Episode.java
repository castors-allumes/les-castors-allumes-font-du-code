package fr.ryu.followthing.dto.serie;

import java.util.Date;

/**
 * Created by Ryuko on 20/12/2014.
 */
public class Episode {

    private String id;

    private String name;

    private Date broadcastingDate;

    private String description;

    public Date getBroadcastingDate() {
        return broadcastingDate;
    }

    public void setBroadcastingDate(Date broadcastingDate) {
        this.broadcastingDate = broadcastingDate;
    }

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

}
