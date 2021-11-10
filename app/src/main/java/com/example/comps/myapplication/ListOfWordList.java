package com.example.comps.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is for holding a list that holds word lists.
 * This class have getters, setters and constructors.
 */

public class ListOfWordList implements Serializable {
    private ArrayList<WordList> listOfWordList = new ArrayList<>();

    ListOfWordList() {
    }

    public ArrayList<WordList> getListOfWordList() {
        return listOfWordList;
    }

    public void setListOfWordList(ArrayList<WordList> listOfWordList) {
        this.listOfWordList = listOfWordList;
    }

}
