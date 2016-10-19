package com.bertrobotics.bertscout2017;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.test.espresso.core.deps.guava.base.Charsets;
import android.support.test.espresso.core.deps.guava.io.CharStreams;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Stand Scouting"));
        tabLayout.addTab(tabLayout.newTab().setText("Pit Scouting"));
        tabLayout.addTab(tabLayout.newTab().setText("Statistics"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setTitle("North Shore");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        StandScouting standScouting = new StandScouting();

        View standScoutingView = (View) findViewById(R.id.stand_scouting);

        if (id == R.id.north_shore) {
            setTitle("North Shore");

            TextView event = (TextView) standScoutingView.findViewById(R.id.event);
            event.setText("north_shore");

            standScouting.buildMatchSpinner("north_shore", standScoutingView);
            standScouting.buildTeamSpinner("north_shore", standScoutingView);

            return true;
        } else if (id == R.id.pine_tree) {
            setTitle("Pine Tree");

            TextView event = (TextView) standScoutingView.findViewById(R.id.event);
            event.setText("pine_tree");

            standScouting.buildMatchSpinner("pine_tree", standScoutingView);
            standScouting.buildTeamSpinner("pine_tree", standScoutingView);

            return true;
        } else if (id == R.id.sync_data) {
//            ProgressDialog progress = new ProgressDialog(this);
//            progress.setTitle("Saving");
//            progress.setMessage("Please wait...");
//            progress.show();

            DBHelper dbHelper = new DBHelper(this);

            JSONArray results = dbHelper.getData();

            Toast.makeText(this, String.valueOf(results.length()), Toast.LENGTH_SHORT).show();

            View statsView = (View) findViewById(R.id.statistics);

            JSONArray data = dbHelper.getData();

            ArrayList<String> teams = new ArrayList<String>();

            for (int i = 0; i < data.length(); i++) {
                try {
                    String team = data.getJSONObject(i).getString("team");
                    teams.add(team);

                } catch (JSONException e) {

                }
            }

            ListView listView = (ListView) statsView.findViewById(R.id.data_listview);
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, teams);

            listView.setAdapter(listAdapter);


//            progress.dismiss();

        }

        return super.onOptionsItemSelected(item);
    }
}