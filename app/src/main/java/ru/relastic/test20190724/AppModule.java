package ru.relastic.test20190724;


import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context mContext;
    private DBManagerRoom mDBManagerRoom;

    public AppModule(@NonNull Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return mContext;
    }


    @Provides
    @Singleton
    @Named("local")
    public DBManagerRoom provideDBManagerRoom() {
        if (mDBManagerRoom == null) {mDBManagerRoom = new DBManagerRoom(mContext);}
        return mDBManagerRoom;
    }
}
