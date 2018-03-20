package com.gelostech.pocketbartender.models;

import java.util.Map;

/**
 * Created by tirgei on 3/16/18.
 */

public class CocktailModel {
    private int idDrink;
    private String strDrink;
    private String strVideo;
    private String strCategory;
    private String strIBA;
    private String strAlcoholic;
    private String strGlass;
    private String strInstructions;
    private String strDrinkThumb;
    private String dateModified;
    private Map<String,String> ingridients;

    private CocktailModel(){}

    public CocktailModel(int idDrink, String strDrink, String strVideo, String strCategory, String strIBA, String strAlcoholic, String strGlass, String strInstructions, String strDrinkThumb, String dateModified) {
        this.idDrink = idDrink;
        this.strDrink = strDrink;
        this.strVideo = strVideo;
        this.strCategory = strCategory;
        this.strIBA = strIBA;
        this.strAlcoholic = strAlcoholic;
        this.strGlass = strGlass;
        this.strInstructions = strInstructions;
        this.strDrinkThumb = strDrinkThumb;
        this.dateModified = dateModified;
    }

    public Map<String, String> getIngridients() {
        return ingridients;
    }

    public void setIngridients(Map<String, String> ingridients) {
        this.ingridients = ingridients;
    }

    public int getIdDrink() {
        return idDrink;
    }

    public String getStrDrink() {
        return strDrink;
    }

    public String getStrVideo() {
        return strVideo;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrIBA() {
        return strIBA;
    }

    public String getStrAlcoholic() {
        return strAlcoholic;
    }

    public String getStrGlass() {
        return strGlass;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }


    public String getDateModified() {
        return dateModified;
    }
}
