package com.jackgharris.cosc2288.a2.models;


import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;

import java.util.HashMap;
import java.util.Vector;

public class Theme {

    private HashMap<String, String> values;
    private final boolean requiresInserting;

    private boolean saved;


    public Theme(HashMap<String, String> values){
        this.values = values;
        this.values.remove("id");
        this.values.remove("user_id");
        this.requiresInserting = false;
        this.saved = true;

    }

    public Theme(){
        this.requiresInserting = true;
        this.saved = true;

    }

    public boolean themeRequiresInserting(){
        return this.requiresInserting;
    }

    public String getColor(String key){
        return this.values.get(key);
    }

    public void setColor(String key, String hex){
        this.saved = false;
        this.values.remove(key);
        this.values.put(key,hex);

        MyHealth.getInstance().getParent().setStyle(this.getStyle());
    }

    public String getStyle(){
        StringBuilder style = new StringBuilder();

        this.values.forEach((k,v) ->{
            String entry = k+": "+v+"; ";
            style.append(entry);
        });

        return style.toString();
    }

    public void reset(boolean updateInstance){
        HashMap<String, String> values = new HashMap<>();

        values.put("-fx-error","#F75165");
        values.put("-fx-success","#7CCF37");

        values.put("-fx-button-primary-background","#4F7BFE");
        values.put("-fx-button-primary-background-hover","#4265d5");
        values.put("-fx-button-primary-text","white");

        values.put("-fx-button-danger-background","#F75165");
        values.put("-fx-button-danger-background-hover","#cc3e50");
        values.put("-fx-button-danger-text","white");

        values.put("-fx-background-primary","#25284B");
        values.put("-fx-background-secondary","#2D325A");

        this.values = values;
        this.saved = false;
        if(updateInstance){
            MyHealth.getInstance().getParent().setStyle(this.getStyle());
        }
    }

    public void save(){
        User user = MyHealth.getInstance().getUser();

        String sql = "UPDATE `themePreferences` SET " +
                "`-fx-error`='"+this.getColor("-fx-error")+"', "+
                "`-fx-success`='"+this.getColor("-fx-success")+"', "+
                "`-fx-button-primary-background`='"+this.getColor("-fx-button-primary-background")+"', "+
                "`-fx-button-primary-text`='"+this.getColor("-fx-button-primary-text")+"', "+
                "`-fx-button-primary-background-hover`='"+this.getColor("-fx-button-primary-background-hover")+"', "+
                "`-fx-button-danger-background`='"+this.getColor("-fx-button-danger-background")+"', "+
                "`-fx-button-danger-text`='"+this.getColor("-fx-button-danger-text")+"', "+
                "`-fx-button-danger-background-hover`='"+this.getColor("-fx-button-danger-background-hover")+"', "+
                "`-fx-background-primary`='"+this.getColor("-fx-background-primary")+"', "+
                "`-fx-background-secondary`='"+this.getColor("-fx-background-secondary")+"'"+
                " WHERE user_id="+user.getId();

        Database.queryWithBooleanResult(sql);
        this.saved = true;
        Activity.add(new Activity("Updated Theme Preferences Saved"));
    }

    public boolean isSaved(){
        return this.saved;
    }


    public static Theme get(int user_id){
        String query = "SELECT * FROM themePreferences WHERE user_id='"+user_id+"'";

        Vector<HashMap<String,String>> vector = Database.query(query);

        if(!vector.isEmpty()){
            return new Theme(vector.get(0));
        }else{
            Theme theme = new Theme();
            theme.reset(false);
            return theme;
        }

    }

    public static boolean add(User user){

        String sql = "INSERT INTO themePreferences " +
                "(user_id, '-fx-error', '-fx-success', '-fx-button-primary-background', '-fx-button-primary-text', " +
                "'-fx-button-primary-background-hover', '-fx-button-danger-background', '-fx-button-danger-text', '-fx-button-danger-background-hover', " +
                "'-fx-background-primary', '-fx-background-secondary') " +
                "VALUES " +
                "('"+user.getId()+"', '"+user.getTheme().getColor("-fx-error")+"', '"+user.getTheme().getColor("-fx-success")+"','" +
                ""+user.getTheme().getColor("-fx-button-primary-background")+"','"+user.getTheme().getColor("-fx-button-primary-text")+"'," +
                "'"+user.getTheme().getColor("-fx-button-primary-background-hover")+"','"+user.getTheme().getColor("-fx-button-danger-background")+"'," +
                "'"+user.getTheme().getColor("-fx-button-danger-text")+"','"+user.getTheme().getColor("-fx-button-danger-background-hover")+"'," +
                "'"+user.getTheme().getColor("-fx-background-primary")+"','"+user.getTheme().getColor("-fx-background-secondary")+"')";

        return Database.queryWithBooleanResult(sql);
    }

}
