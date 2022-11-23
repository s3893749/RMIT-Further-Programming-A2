package com.jackgharris.cosc2288.a2.models;

import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Vector;

public class Comment extends Record{

    private int recordId;
    private String note;

    public Comment(int id, String type, int userId, String value, String date) {
        super(id, type, userId, value, date);
        String[] data = value.split("/");
        this.recordId = Integer.parseInt(data[0]);
        if(data.length == 2){
            this.note = data[1];
        }else{
            this.note = "";
        }

    }

    public int getRecordId(){
        return this.recordId;
    }

    public String getNote(){
        return this.note;
    }

    public static boolean add(Comment comment){

        String deleteQuery = "DELETE FROM comments WHERE user_id='"+ MyHealth.getInstance().getUser().getId() +"' AND record_id='"+comment.getRecordId()+"'";
        Database.queryWithBooleanResult(deleteQuery);

        Activity.add(new Activity("New comment saved"));

        String sql = "INSERT INTO comments (user_id, record_id, value) VALUES ('"+comment.getUserId()+"','"+ comment.getRecordId()+"', '"+comment.getNote()+"')";

        return Database.queryWithBooleanResult(sql);
    }

    public static ObservableList<Comment> get(int user_id, int record_id){



        String sql = "SELECT * FROM comments WHERE user_id='"+user_id+"' AND record_id='"+record_id+"'";


        ObservableList<Comment> output = FXCollections.observableArrayList();

        Vector<HashMap<String, String>> resultSet = Database.query(sql);

        resultSet.forEach((n) -> {
            output.add(new Comment(
                    Integer.parseInt(n.get("id")),
                    n.get("type"),
                    Integer.parseInt(n.get("user_id")),
                    n.get("record_id")+"/"+n.get("value"),
                    n.get("date")));
        });

        return output;
    }
}
