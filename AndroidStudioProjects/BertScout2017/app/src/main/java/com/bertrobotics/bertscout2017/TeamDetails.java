package com.bertrobotics.bertscout2017;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
        int team = intent.getIntExtra("team", 0);

        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.toolbar2);
        toolbar.setTitle("Team: " + Integer.toString(team));

        data = dbHelper.getDataAllStand(team);

        ArrayList<String> teamDetailsArray = new ArrayList<String>();

        for (int i = 0; i < data.length(); i++) {
            try {
                String autoBaseLine = "No ";
                String autoPlaceGear = "No ";
                String autoOpenHopper = "No ";
                String teleopClimbed = "No ";
                String teleopTouchpad = "No ";

                Integer match = Integer.parseInt(data.getJSONObject(i).getString("match_no"));
                Integer autoHighGoal = Integer.parseInt(data.getJSONObject(i).getString("auto_score_high"));
                Integer autoLowGoal = Integer.parseInt(data.getJSONObject(i).getString("auto_score_low"));
                Integer teleopHighGoal = Integer.parseInt(data.getJSONObject(i).getString("tele_score_high"));
                Integer teleopLowGoal = Integer.parseInt(data.getJSONObject(i).getString("tele_score_low"));
                Integer teleopGearsPlaced = Integer.parseInt(data.getJSONObject(i).getString("tele_gears_placed"));
                Integer teleopPenalties = Integer.parseInt(data.getJSONObject(i).getString("tele_penalties"));

                if (data.getJSONObject(i).getString("auto_base_line").equals("true")) {
                    autoBaseLine = "Yes";
                }

                if (data.getJSONObject(i).getString("auto_place_gear").equals("true")) {
                    autoPlaceGear = "Yes";
                }

                if (data.getJSONObject(i).getString("auto_open_hopper").equals("true")) {
                    autoOpenHopper = "Yes";
                }

                String temp = data.getJSONObject(i).getString("tele_climbed");

                if (data.getJSONObject(i).getString("tele_climbed").equals("true")) {
                    teleopClimbed = "Yes";
                }

                if (data.getJSONObject(i).getString("tele_touchpad").equals("true")) {
                    teleopTouchpad = "Yes";
                }

                String comment = data.getJSONObject(i).getString("stand_comment");
                String scoutName = data.getJSONObject(i).getString("scout_name");
                int approxScore = 0;
                if (autoBaseLine.equals("Yes")) {
                    approxScore += 5;
                }
                approxScore += autoHighGoal;
                if (autoPlaceGear.equals("Yes")) {
                    approxScore += 30;
                }
                approxScore += teleopHighGoal;
                approxScore += teleopGearsPlaced * 10;
                if (teleopTouchpad.equals("Yes")) {
                    approxScore += 50;
                }

                teamDetailsArray.add(String.valueOf(i + 1) + ")  Match: " + Integer.toString(match) +
                        "                                   " +
                        "Aproximate Score: " + String.format("%1d", approxScore) +
                        "\n\n" +
                        "Auto High Goal: " + String.valueOf(autoHighGoal) + "                           " +
                        "Teleop High Goal: " + String.valueOf(teleopHighGoal) +
                        "\n" +
                        "Auto Low Goal: " + String.valueOf(autoLowGoal) + "                            " +
                        "Teleop Low Goal: " + String.valueOf(teleopLowGoal) +
                        "\n" +
                        "Auto Baseline: " + String.valueOf(autoBaseLine) + "                          " +
                        "Teleop Gears Placed: " + String.valueOf(teleopGearsPlaced) +
                        "\n" +
                        "Auto Gear Placed: " + String.valueOf(autoPlaceGear) + "                    " +
                        "Teleop Climbed: " + String.valueOf(teleopClimbed) +
                        "\n" +
                        "Auto Open Hopper: " + String.valueOf(autoOpenHopper) + "                  " +
                        "Teleop Touchpad: " + String.valueOf(teleopTouchpad) +
                        "\n" +
                        "Penalties: " + String.valueOf(teleopPenalties) + "                                      " +
                        "\n\n" +
                        "Comment(" + scoutName + "): " + comment + "\n");

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
                Integer team = 133;

                try {
                    matchNo = Integer.parseInt(data.getJSONObject(position).getString("match_no"));
                    team = Integer.parseInt(data.getJSONObject(position).getString("team"));
                } catch (JSONException e) {

                }


                Intent intent = new Intent();
                intent.putExtra("match_no", matchNo);
                intent.putExtra("team", team);
                setResult(2, intent);


                finish();
            }
        });

        listView.setAdapter(adapter);
    }
}
