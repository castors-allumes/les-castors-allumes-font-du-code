package fr.ryu.followthing.service.persistence;

import org.bson.types.ObjectId;
import org.mongojack.Id;

public class MongoObjectTest {

    @Id
    private ObjectId id;

    private String title;

    private int cpt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCpt() {
        return cpt;
    }

    public void setCpt(int cpt) {
        this.cpt = cpt;
    }

    @Override
    public String toString() {
        return title+":"+cpt;
    }
}