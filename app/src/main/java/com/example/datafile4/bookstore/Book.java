package com.example.datafile4.bookstore;

import java.util.HashMap;

/**
 * Created by datafile4 on 8/19/16.
 */
public class Book {
    private String mName;
    private String mImageUrl;
    private int mId;


    public Book(int Id, String name, String imgUrl){
        mName = name;
        mId = Id;
        mImageUrl = imgUrl;
    }

    public String getBookName(){
        return mName;
    }

    public String getBookImgUrl(){
        return mImageUrl;
    }
    public int getBookID(){
        return mId;
    }
}
