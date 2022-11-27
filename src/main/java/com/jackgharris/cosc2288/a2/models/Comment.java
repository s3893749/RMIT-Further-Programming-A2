//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.models;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Vector;

//**** START COMMENT CLASS ****\\
public class Comment extends Record{

    private int recordId;
    private String note;

    //**** CONSTRUCTOR ****\\
    //The Comment class extends the record class and so inherits the bulk of its
    //functionality from it, here we can see the main difference is the value
    //parameter is split into a record id and a note.
    public Comment(int id, String type, int userId, String value, String date) {
        super(id, type, userId, value, date, null);

        //split the value into a data array
        String[] data = value.split("/");

        //set the record id
        this.recordId = Integer.parseInt(data[0]);

        //check if we have a note provided, if so proceed.
        if(data.length == 2){

            //blast the note into an array of characters
            char[] noteCharacters = data[1].toCharArray();

            //declare our counter
            int i = 0;

            //loop over each character
            for (char character : noteCharacters) {

                //if we find a single quote ascii code 39, then replace it with a space
                if((int)character == 39){
                    noteCharacters[i] = ' ';
                }

                //increment out count
                i++;
            }

            //finally set the note value
            this.note = new String(noteCharacters);
        }else{

            //else if a note was not provided set it to a blank string
            this.note = "";
        }

    }

    //**** GET RECORD ID METHOD ****\\
    //Returns the record ID of a comment.
    public int getRecordId(){
        return this.recordId;
    }

    //**** GET NOTE METHOD ****\\
    //returns the note value to the caller.
    public String getNote(){
        return this.note;
    }

    //**** STATIC ADD METHOD ****\\
    //This method will accept a comment and insert it into the database.
    public static boolean add(Comment comment){

        //if the comment is already in the database then delete it
        Comment.delete(comment);

        //add an activity stating a new comment was saved
        Activity.add(new Activity("New comment saved"));

        //build our SQL query
        String sql = "INSERT INTO comments (user_id, record_id, value) VALUES ('"+comment.getUserId()+"','"+ comment.getRecordId()+"', '"+comment.getNote()+"')";

        //return the result of the insert query.
        return Database.queryWithBooleanResult(sql);
    }

    //**** STATIC DELETE METHOD ***\\
    //The delete method accept a comment object and will then delete it from
    //the database.
    public static boolean delete(Comment comment){

        //build our SQL query
        String deleteQuery = "DELETE FROM comments WHERE user_id='"+ MyHealth.getInstance().getUser().getId() +"' AND record_id='"+comment.getRecordId()+"'";

        //return the result of the delete query.
        return Database.queryWithBooleanResult(deleteQuery);
    }

    //**** STATIC GET METHOD ****\\
    //The get method returns a comment based on the provided user_id & record_id.
    public static ObservableList<Comment> get(int user_id, int record_id){

        //Build our sql query.
        String sql = "SELECT * FROM comments WHERE user_id='"+user_id+"' AND record_id='"+record_id+"'";

        //create our output array list
        ObservableList<Comment> output = FXCollections.observableArrayList();

        //get our result from the database
        Vector<HashMap<String, String>> resultSet = Database.query(sql);

        //for each of the results build a comment and add it to the output array
        resultSet.forEach((n) -> {
            output.add(new Comment(
                    Integer.parseInt(n.get("id")),
                    n.get("type"),
                    Integer.parseInt(n.get("user_id")),
                    n.get("record_id")+"/"+n.get("value"),
                    n.get("date")));
        });

        //finally return the output array.
        return output;
    }
}
