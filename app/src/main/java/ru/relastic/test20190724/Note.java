package ru.relastic.test20190724;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Bundle;

import java.util.Date;
import java.util.Random;

import static ru.relastic.test20190724.DBManagerRoom.*;

@Entity(tableName = TABLE_NAME, indices = {@Index("id")})
public class Note {

    @Ignore
    private int pos=0;

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String note;
    private String author;

    private long date;
    private String description;


    public Note(){

    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Bundle getData() {
        Bundle retVal = new Bundle();
        retVal.putInt(FIELD_POS,pos);
        retVal.putInt(FIELD_ID,id);
        retVal.putString(FIELD_NOTE,note);
        retVal.putString(FIELD_AUTHOR,author);
        retVal.putLong(FIELD_DATE,date);
        retVal.putString(FIELD_DESCRIPTION,description);
        return retVal;
    }

    public static Note createRandom(int pos) {
        Note retVal = new Note();
        String suff = String.valueOf(new Random().nextInt(999));
        retVal.pos = pos;
        retVal.setId(pos);
        retVal.setNote(FIELD_NOTE+suff);
        retVal.setAuthor(FIELD_AUTHOR+suff);
        retVal.setDate(new Date().getTime());
        System.out.println("--------------------------- "+(new Date().getTime()));
        retVal.setDescription(FIELD_DESCRIPTION+suff);
        return retVal;
    }
}