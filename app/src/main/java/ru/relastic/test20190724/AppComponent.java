package ru.relastic.test20190724;


import javax.inject.Singleton;
import dagger.Component;

@Component (modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(LocalDataPresenter localDataPresenter);
}
