package com.example.comps.myapplication;

import java.io.Serializable;
import java.util.List;

/**
 * This class holds the user who has his own username, password, hebrew and english word lists.
 * This class have getters, setters and constructors.
 */

public class User implements Serializable {
    private String userName;
    private String password;
    private ListOfWordList hebrewListOfWordList = new ListOfWordList();
    private ListOfWordList englishListOfWordList = new ListOfWordList();


    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public ListOfWordList getEnglishListOfWordList() {
        return englishListOfWordList;
    }

    public void setEnglishListOfWordList(ListOfWordList englishListOfWordList) {
        this.englishListOfWordList = englishListOfWordList;
    }

    public ListOfWordList getHebrewListOfWordList() {
        return hebrewListOfWordList;
    }

    public void setHebrewListOfWordList(ListOfWordList hebrewListOfWordList) {
        this.hebrewListOfWordList = hebrewListOfWordList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
