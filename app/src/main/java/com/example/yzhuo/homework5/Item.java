package com.example.yzhuo.homework5;

/**
 * Created by yzhuo on 10/16/2015.
 */
public class Item {
    String titles;
    String releaseDate;
    String imageURL;
    String mediaURL;
    String description;



    @Override
    public String toString() {
        return "Item{" +
                "titles='" + titles + '\n' +
                ", releaseDate='" + releaseDate + '\n' +
                ", imageURL='" + imageURL + '\n' +
                ", mediaURL='" + mediaURL + '\n' +
                ", description='" + description + '\n' +
                ", duration='" + duration + '\n' +
                '}';
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String duration;




    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }
}
