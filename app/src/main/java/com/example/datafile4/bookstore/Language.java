package com.example.datafile4.bookstore;

import java.io.Serializable;

/**
 * Created by datafile4 on 8/29/16.
 */
public class Language implements Serializable{
    private int mID;
    private String mLanguage;
    private boolean selected;

    public Language(int ID, String language){
        mID = ID;
        mLanguage = language;
    }

    public String getLanguage(){
        return mLanguage;
    }

    public int getId(){
        return mID;
    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
