package com.example.datafile4.bookstore;

import java.io.Serializable;

/**
 * Created by datafile4 on 8/29/16.
 */
public class Genre implements Serializable {
    private int mID;
    private String mGenreName;
    private boolean selected;

    public Genre(int ID, String genreName){
        mID = ID;
        mGenreName = genreName;
    }

    public String getGenreName(){
        return mGenreName;
    }

    public int getGenreID() {
        return mID;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
