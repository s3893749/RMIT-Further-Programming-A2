package com.jackgharris.cosc2288.a2.core;

import javafx.scene.paint.Color;

public class Theme {

    private String backgroundColor;
    private String menuColor;
    private String textColor;
    private String accentPrimary;
    private String accentSecondary;

    public Theme(String backgroundColor, String menuColor, String textColor, String accentPrimary, String accentSecondary){
        this.backgroundColor = backgroundColor;
        this.menuColor = menuColor;
        this.textColor = textColor;
        this.accentPrimary = accentPrimary;
        this.accentSecondary = accentSecondary;
    }

    public String getBackgroundColor(){
        return this.backgroundColor;
    }

    public void setBackgroundColor(String hex){
        this.backgroundColor = hex;
    }

    public void setMenuColor(String hex){
        this.menuColor = hex;
    }

    public String getMenuColor(){
        return this.menuColor;
    }

    public void updateAppColors(){
        MyHealth.getStageById("dashboard").getScene().lookup("#parent").setStyle("-fx-background-color: "+this.backgroundColor);
        MyHealth.getStageById("dashboard").getScene().lookup("#menu").setStyle("-fx-background-color: "+this.menuColor);
    }
}
