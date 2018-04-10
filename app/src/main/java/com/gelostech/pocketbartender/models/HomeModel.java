package com.gelostech.pocketbartender.models;

import java.io.Serializable;

/**
 * Created by tirgei on 3/3/18.
 */

@SuppressWarnings("serial")
public class HomeModel implements Serializable{
    private String name;
    private String imageUrl;
    private int id;
    private byte[] cocktailThumb;
    private int type;

    public HomeModel(){}

    public HomeModel(String name, String imageUrl, int id, byte[] cocktailThumb){
        this.name = name;
        this.imageUrl = imageUrl;
        this.id = id;
        this.cocktailThumb = cocktailThumb;
    }

    public byte[] getCocktailThumb() {
        return cocktailThumb;
    }

    public void setCocktailThumb(byte[] cocktailThumb) {
        this.cocktailThumb = cocktailThumb;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
