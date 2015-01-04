package com.wardziniak.worktimestopwatch.ui.common;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wardziniak.worktimestopwatch.App;
import com.wardziniak.worktimestopwatch.R;
import com.wardziniak.worktimestopwatch.ui.history.WorkHistoryFragment;
import com.wardziniak.worktimestopwatch.ui.main.TimerViewFragment;
import com.wardziniak.worktimestopwatch.ui.settings.TimerSettingsFragment;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.ObjectGraph;


public class MainActivity extends FragmentActivity implements MainView {

    @Inject
    MainPresenter mainPresenter;

    @Inject
    TimerViewFragment timerViewFragment;
    @Inject
    WorkHistoryFragment workHistoryFragment;
    @Inject
    TimerSettingsFragment timerSettingsFragment;

    private ObjectGraph activityGraph;

    private DrawerLayout mDrawerLayout;
    private String[] mPlanetTitles;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityGraph = ((App) getApplication()).createScopedGraph(getModules().toArray());
        activityGraph.inject(this);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        mPlanetTitles = mainPresenter.getDrawerTitles();
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainPresenter.onDrawerClick(position);
            }
        });
    }

    @Override
    public void switchToTimerView(int selectedItem) {
        //if (timerViewFragment == null) {
        //    timerViewFragment = TimerViewFragment.newInstance();
        //}
        switchToFragment(timerViewFragment, selectedItem);
    }

    @Override
    public void switchToTimerSettingsView(int selectedItem) {
        //if (timerSettingsFragment == null) {
        //    timerSettingsFragment = TimerSettingsFragment.newInstance();
        //}
        switchToFragment(timerSettingsFragment, selectedItem);
    }

    @Override
    public void switchToHistoryView(int selectedItem) {
        switchToFragment(workHistoryFragment, selectedItem);
    }

    private void switchToFragment(Fragment fragment, int position) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityGraph = null;
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new MainModule(this));
    }
}
