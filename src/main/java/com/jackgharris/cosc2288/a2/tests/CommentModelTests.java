package com.jackgharris.cosc2288.a2.tests;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Comment;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.Time;
import org.junit.Assert;
import org.junit.Test;

public class CommentModelTests {

    public CommentModelTests(){
        MyHealth app = new MyHealth();
        app.setUser(User.getByEmail("junit@testing.com"));
    }

    @Test
    public void addCommentAndDelete(){
        String note = "Comment from Junit test";
        String date = "2022-11-26";
        int user_id = MyHealth.getInstance().getUser().getId();

        Record record = new Record(-1,"Temperature",user_id,"105.4",date, Time.now());
        Record.add(record);
        int record_id = Record.lastAddedId;

        Comment comment = new Comment(-1,"Comment",user_id,record_id+"/"+note,date);
        Comment.add(comment);

        Assert.assertFalse(Comment.get(user_id, record_id).isEmpty());

        Record.delete(record);
        Comment.delete(comment);
    }


    @Test
    public void getCommentNote(){
        String note = "Comment get note test";
        String date = "2022-11-26";
        int user_id = MyHealth.getInstance().getUser().getId();

        Record record = new Record(-1,"Temperature",user_id,"105.4",date, Time.now());
        Record.add(record);
        int record_id = Record.lastAddedId;

        Comment comment = new Comment(-1,"Comment",user_id,record_id+"/"+note,date);
        Comment.add(comment);

        Assert.assertEquals(Comment.get(user_id, record_id).get(0).getNote(),note);

        Record.delete(record);
        Comment.delete(comment);
    }

    @Test
    public void getCommentRecordID(){
        String note = "Comment get note test";
        String date = "2022-11-26";
        int user_id = MyHealth.getInstance().getUser().getId();

        Record record = new Record(-1,"Temperature",user_id,"105.4",date, Time.now());
        Record.add(record);
        int record_id = Record.lastAddedId;

        Comment comment = new Comment(-1,"Comment",user_id,record_id+"/"+note,date);
        Comment.add(comment);

        Assert.assertEquals(Comment.get(user_id, record_id).get(0).getRecordId(),record_id);

        Record.delete(record);
        Comment.delete(comment);
    }

    @Test
    public void addCommentWithSingleQuoteRemoval(){
        String note = "this is a 'single' quotation test";
        String target = "this is a  single  quotation test";
        String date = "2022-11-27";

        int user_id = MyHealth.getInstance().getUser().getId();
        int record_id = 123456789;

        Comment comment = new Comment(-1,"Comment",user_id,record_id+"/"+note,date);

        Comment.add(comment);

        Assert.assertEquals(target,Comment.get(user_id,record_id).get(0).getNote());

        Comment.delete(comment);

    }

}
