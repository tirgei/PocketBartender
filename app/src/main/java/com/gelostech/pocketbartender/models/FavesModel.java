package com.gelostech.pocketbartender.models;

/**
 * Created by tirgei on 3/15/18.
 */

public class FavesModel {
    private int id;
    private String name;
    private byte[] cocktailThumb;

    public FavesModel(){}

    public FavesModel(int id, String name, byte[] cocktailThumb){
        this.id = id;
        this.name = name;
        this.cocktailThumb = cocktailThumb;
    }

    public byte[] getCocktailThumb() {
        return cocktailThumb;
    }

    public void setCocktailThumb(byte[] cocktailThumb) {
        this.cocktailThumb = cocktailThumb;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
