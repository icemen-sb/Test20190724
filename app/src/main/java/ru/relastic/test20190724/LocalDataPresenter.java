package ru.relastic.test20190724;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

@InjectViewState
public class LocalDataPresenter extends MvpPresenter<IMainView> {
    static final String FORMAT_DEFAULT_DATE = "dd/MM/yyyy";
    static final String FORMAT_DEFAULT_TIME = "HH:mm:ss";
    static final String DEFAULT_TIME_MIDDAY = "12:00:00";
    static final String FORMAT_DEFAULT_DATETIME = FORMAT_DEFAULT_DATE +" "+FORMAT_DEFAULT_TIME;

    @Inject
    @Named("local")
    DBManagerRoom mLocalDBManagerRoom;

    public void requestSearchResults(CharSequence text) {
        List<String> list = new ArrayList<>();
        List<Bundle> data = mLocalDBManagerRoom.getData(text.toString());
        for (Bundle bundle : data) {
            list.add(bundle.getInt(DBManagerRoom.FIELD_POS) + " | "
                    + bundle.getString(DBManagerRoom.FIELD_NOTE) + " | "
                    + bundle.getString(DBManagerRoom.FIELD_AUTHOR) + " | "
                    + getDateString(bundle.getLong(DBManagerRoom.FIELD_DATE), null) + " | "
                    + bundle.getString(DBManagerRoom.FIELD_DESCRIPTION));
        }
        getViewState().responseResult(list);
    }


    public void requestSortResult(){
        List<String> list = new ArrayList<>();
        List<Bundle> data = mLocalDBManagerRoom.sortData();
        for (Bundle bundle : data) {
            list.add(bundle.getInt(DBManagerRoom.FIELD_POS) + " | "
                    + bundle.getString(DBManagerRoom.FIELD_NOTE) + " | "
                    + bundle.getString(DBManagerRoom.FIELD_AUTHOR) + " | "
                    + getDateString(bundle.getLong(DBManagerRoom.FIELD_DATE), null) + " | "
                    + bundle.getString(DBManagerRoom.FIELD_DESCRIPTION));
        }
        getViewState().responseResult(list);
    }


    @SuppressLint("CheckResult")
    public void search_ready(EditText editText){

        RxTextView.textChanges(editText)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::requestSearchResults);

    }
    @SuppressLint("CheckResult")
    public void sort_ready(Button button){

        RxView.clicks(button)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Object>() {

                    @Override
                    public void onNext(Object o) {
                        requestSortResult();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void attachView(IMainView view) {
        super.attachView(view);
    }

    @Override
    protected void onFirstViewAttach() {
        App.getComponent().inject(this);

        if (mLocalDBManagerRoom.getData("").size()==0) {
            mLocalDBManagerRoom.populateDatabase();
        }

        super.onFirstViewAttach();
    }

    public static String getDateString(long date, @Nullable String date_format){
        if (date_format==null) {date_format = FORMAT_DEFAULT_DATE;}
        SimpleDateFormat sdf = new SimpleDateFormat(date_format);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return sdf.format(new Date(date));
    }
}
