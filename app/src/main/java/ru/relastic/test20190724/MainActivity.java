package ru.relastic.test20190724;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

public class MainActivity extends MvpAppCompatActivity implements IMainView{
    private ArrayAdapter<String> mArrayAdapter=null;
    private EditText mEditText=null;
    private Button mButton=null;

    @InjectPresenter
    LocalDataPresenter mLocalDataPresenter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.user_info_menu, menu);
        MenuItem menuItem = menu.getItem(0);
        mEditText = ((FrameLayout)menuItem.getActionView()).findViewById(R.id.edittext);
        if (mEditText!=null) mLocalDataPresenter.search_ready(mEditText);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mEditText!=null) {
            mEditText.setText("");
            mEditText.setVisibility(mEditText.getVisibility()== View.GONE ? View.VISIBLE : View.GONE);
            mLocalDataPresenter.requestSearchResults("");
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMvpDelegate().onAttach();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLocalDataPresenter.sort_ready(findViewById(R.id.sort));
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.search_results);
        listView.setAdapter(mArrayAdapter);


    }

    @Override
    public void responseResult(List<String> list) {
        mArrayAdapter.clear();
        mArrayAdapter.addAll(list);
    }
}
