package ru.relastic.test20190724;

import com.arellomobile.mvp.MvpView;

import java.util.List;

public interface IMainView extends MvpView {
    void responseResult(List<String> list);
}
