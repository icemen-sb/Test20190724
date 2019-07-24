package ru.relastic.test20190724;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DBManagerRoom {
    public static final String TABLE_NAME="notes";
    public static final String FIELD_POS="pos";
    public static final String FIELD_ID="id";
    public static final String FIELD_NOTE="note";
    public static final String FIELD_AUTHOR="author";
    public static final String FIELD_DATE="date";
    public static final String FIELD_DESCRIPTION="description";

    public static final String DB_NAME = "database.db";
    public static final int VERSION_DB = 1;

    private final NotesDatabase mNotesDatabase;
    private final NotesDAO mNotesDAO;
    private String mFilter = "";
    private boolean mSort_asc = true;

    public DBManagerRoom(Context context) {
        Log.v("DBManager:","Creating instance of DBManagerRoom class ...");
        mNotesDatabase = Room.databaseBuilder(context, NotesDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        mNotesDAO = mNotesDatabase.getNotesDAO();
        if (mNotesDatabase!=null) {
            Log.v("DBManager:","Created instance of DBManagerRoom class.");
        }else {
            Log.v("DBManager: ERROR","ERROR creating instance of DBManagerRoom class.");
        }
    }


    public ArrayList<Bundle> getData(String filter){
        mFilter = filter;
        ArrayList<Bundle> data = new ArrayList<>();
        int i = 0;
        List<Note> notes = (mSort_asc ? mNotesDAO.getDataAsc(filter) : mNotesDAO.getDataDesc(filter));
        for (Note note : notes) {
            i++;
            Bundle item = note.getData();
            item.putInt(FIELD_POS,i);
            data.add(item);
        }
        return data;
    }

    public ArrayList<Bundle> sortData(){
        mSort_asc =!mSort_asc;
        return getData(mFilter);
    }

    public void populateDatabase() {
        for (int i = 0; i<100; i++) {
            mNotesDAO.insertData(Note.createRandom(i+1));
        }
    }
    public void eraseDatabase() {
        mNotesDAO.eraseData();
    }

    @Dao
    public abstract static class NotesDAO {
        @Query("select * from notes where note like :filter || '%' order by note ASC")
        //@Query("select * from notes order by note ASC")
        public abstract List<Note> getDataAsc(String filter);

        //@Query("select * from notes where note like :filter+'%' order by note DESC")
        @Query("select * from notes where note like :filter || '%' order by note DESC")
        public abstract List<Note> getDataDesc(String filter);

        @Insert
        public abstract long insertData(Note data);

        @Query("delete from notes")
        public abstract void eraseData();
    }

    @Database(entities = {Note.class}, version = VERSION_DB)
    public abstract static class NotesDatabase extends RoomDatabase {
        public abstract NotesDAO getNotesDAO();
    }
}
