package com.bertrobotics.bertscout2017;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class TeamDetails extends AppCompatActivity {
    public DBHelper dbHelper;

    View mRootView;

    JSONArray data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);

        mRootView = findViewById(R.id.activity_team_details);

        dbHelper = new DBHelper(mRootView.getContext());

        Intent intent = getIntent();
        String event = intent.getStringExtra("event");
        int team = intent.getIntExtra("team", 0);


//        data = dbHelper.getTeamData(event, team);

        ArrayList<String> teamDetailsArray = new ArrayList<String>();

        for (int i = 0; i < data.length(); i++) {
            try {
                Integer matchNo = Integer.parseInt(data.getJSONObject(i).getString("match_no"));
                Integer autoHighGoal = Integer.parseInt(data.getJSONObject(i).getString("auto_high"));
                Integer autoLowGoal = Integer.parseInt(data.getJSONObject(i).getString("auto_low"));
                Integer autoCrossings = Integer.parseInt(data.getJSONObject(i).getString("auto_cross"));
                Integer teleopHighGoal = Integer.parseInt(data.getJSONObject(i).getString("tele_high"));
                Integer teleopLowGoal = Integer.parseInt(data.getJSONObject(i).getString("tele_low"));
                Integer teleopCrossings = Integer.parseInt(data.getJSONObject(i).getString("tele_cross"));
                Integer endgame = Integer.parseInt(data.getJSONObject(i).getString("endgame"));
                String comment = data.getJSONObject(i).getString("comment");

                teamDetailsArray.add(String.valueOf(i + 1) + ")  Match: " + Integer.toString(matchNo) + "\n\n" +
                        "Auto High Goal: " + String.valueOf(autoHighGoal) + "                  " +
                        "Teleop High Goal: " + String.valueOf(teleopHighGoal) +
                        "\nAuto Low Goal: " + String.valueOf(autoLowGoal) + "                  " +
                        "Teleop Low Goal: " + String.valueOf(teleopLowGoal) +
                        "\nAuto Crossings: " + String.valueOf(autoCrossings) + "                  " +
                        "Teleop Crossings: " + String.valueOf(teleopCrossings) +
                        "\n\nEnd Game: " + String.valueOf(endgame) +
                        "\n\nComment: " + comment + "\n");

            } catch (JSONException e) {

            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mRootView.getContext(),
                android.R.layout.simple_list_item_1, teamDetailsArray);

        ListView listView = (ListView) findViewById(R.id.TeamDetailsListView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


//                LayoutInflater inflater = (LayoutInflater) mRootView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View mainView = inflater.inflate(R.layout.activity_main, null, false);
////                ViewPager viewPager = (ViewPager) mainView.findViewById(R.id.pager);
//                TabLayout tabLayout = (TabLayout) mainView.findViewById(R.id.tab_layout);
//
//                int num = tabLayout.getTabCount();
//
//                tabLayout.getTabAt(0).select();

//                viewPager.setCurrentItem(0);

                Integer matchNo = 1;
                Integer team = 58;

                try {
                    matchNo = Integer.parseInt(data.getJSONObject(position).getString("match_no"));
                    team = Integer.parseInt(data.getJSONObject(position).getString("team"));
                } catch (JSONException e) {

                }


                Intent intent=new Intent();
                intent.putExtra("match_no", matchNo);
                intent.putExtra("team", team);
                setResult(2,intent);


                finish();
            }
        });

        listView.setAdapter(adapter);
    }
}
