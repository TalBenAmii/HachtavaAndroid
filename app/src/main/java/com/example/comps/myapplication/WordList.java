package com.example.comps.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is for holding a list of words.
 * This class have getters, setters and constructors.
 */

public class WordList implements Serializable {
    private ArrayList<String> words = new ArrayList<>();
    private String listName;

    WordList() {
    }

    WordList(ArrayList<String> words, String listName) {
        this.words = words;
        this.listName = listName;
    }

    @Override
    public String toString() {
        return listName;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
