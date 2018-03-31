package ch.ffhs.vity.vity.Helper;


import android.location.Location;

import java.time.Instant;

public class ActivityItem {

    // Never set a id
    // Id = SequenceNumber DB

    private String id;
    private String owner;
    private String date;
    private String title;
    private String description;
    private String category;
    private String link;
    private String link_image;
    private Location location;

    // Default Constructor
    public ActivityItem(){
    }

    // Complete Constructor
    public ActivityItem(String owner, String date, String title, String description, String category, String link, String link_image, Location location) {
        this.owner = owner;
        this.date = date;
        this.title = title;
        this.description = description;
        this.category = category;
        this.link = link;
        this.link_image = link_image;
        this.location = location;
    }

    // Getter Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink_image() {
        return link_image;
    }

    public void setLink_image(String link_image) {
        this.link_image = link_image;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
