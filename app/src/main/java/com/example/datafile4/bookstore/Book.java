package com.example.datafile4.bookstore;

import java.util.HashMap;

/**
 * Created by datafile4 on 8/19/16.
 */
public class Book {
    private String mName;
    private String mAuthor;
    private String mImageUrl;
    private String mGenre;
    private String mLanguage;
    private Uploader uploader;

    public class Uploader{
        public String mFirstName;
        public String mLastName;
        public String mUsername;
        public String mEmail;
    }

    public Book(HashMap<String,String> info){
        mName = info.get("bookName");
        mAuthor = info.get("author");
        mImageUrl = info.get("url");
        mGenre = info.get("genre");
        mLanguage = info.get("language");
        Uploader uploader = new Uploader();
        uploader.mFirstName = info.get("uFirstName");
        uploader.mLastName = info.get("uLastName");
        uploader.mEmail = info.get("uEmail");
        uploader.mUsername = info.get("username");
    }

    public String getBookName(){
        return mName;
    }

    public String getBookImgUrl(){
        return mImageUrl;
    }
}
