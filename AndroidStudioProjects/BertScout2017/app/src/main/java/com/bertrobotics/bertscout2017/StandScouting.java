package com.bertrobotics.bertscout2017;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.os.AsyncTask;
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

public class StandScouting extends Fragment {

    public View rootView;
    public DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.stand_scouting, container, false);
        this.rootView = rootView;

        Button saveBtn = (Button) rootView.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AsyncTaskURL getData = new AsyncTaskURL();
//                getData.execute();

                AsyncTaskInsertData insertData = new AsyncTaskInsertData();
                insertData.execute();

//                TextView eventText = (TextView) rootView.findViewById(R.id.event);
//                Spinner matchSpinner = (Spinner) rootView.findViewById(R.id.match_spinner);
//                Spinner teamSpinner = (Spinner) rootView.findViewById(R.id.team_spinner);
//                Spinner defenseSpinner = (Spinner) rootView.findViewById(R.id.defense_spinner);
//                Spinner endgameSpinner = (Spinner) rootView.findViewById(R.id.endgame_spinner);
//                TextView autoHighGoalText = (TextView) rootView.findViewById(R.id.auto_high_goal_text);
//                TextView autoLowGoalText = (TextView) rootView.findViewById(R.id.auto_low_goal_text);
//                TextView teleopHighGoalText = (TextView) rootView.findViewById(R.id.teleop_high_goal_text);
//                TextView teleopLowGoalText = (TextView) rootView.findViewById(R.id.teleop_low_goal_text);
//                TextView teleopCrossingsText = (TextView) rootView.findViewById(R.id.teleop_crossings_text);
//
//                String event = eventText.getText().toString();
//                String match_no = matchSpinner.getSelectedItem().toString();
//                String team = teamSpinner.getSelectedItem().toString();
//                Integer autoHighGoal = Integer.parseInt(autoHighGoalText.getText().toString());
//                Integer autoLowGoal = Integer.parseInt(autoLowGoalText.getText().toString());
//                Integer teleopHighGoal = Integer.parseInt(teleopHighGoalText.getText().toString());
//                Integer teleopLowGoal = Integer.parseInt(teleopLowGoalText.getText().toString());
//                Integer teleopCrossings = Integer.parseInt(teleopCrossingsText.getText().toString());
//
//                Integer defense;
//                if (defenseSpinner.getSelectedItem().toString().equals("Reach")) {
//                    defense = 2;
//                } else if (defenseSpinner.getSelectedItem().toString().equals("Cross")) {
//                    defense = 10;
//                } else {
//                    defense = 0;
//                }
//
//                Integer endgame;
//                if (endgameSpinner.getSelectedItem().toString().equals("Challenge")) {
//                    endgame = 5;
//                } else if (endgameSpinner.getSelectedItem().toString().equals("Scale")) {
//                    endgame = 15;
//                } else {
//                    endgame = 0;
//                }
//
//                ProgressDialog progress = new ProgressDialog(rootView.getContext());
//                progress.setTitle("Saving");
//                progress.setMessage("Please wait...");
//                progress.show();
//
//                dbHelper.insertStandScouting(event, match_no, team, autoHighGoal, autoLowGoal,
//                        defense, teleopHighGoal, teleopLowGoal, teleopCrossings, endgame);
//
//                progress.dismiss();

            }
        });

        Button autoHighGoalMinusBtn = (Button) rootView.findViewById(R.id.auto_high_goal_minus_btn);
        autoHighGoalMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView autoHighGoalText = (TextView) rootView.findViewById(R.id.auto_high_goal_text);
                int autoHighGoalInt = Integer.parseInt(autoHighGoalText.getText().toString());

                if (autoHighGoalInt > 0) {
                    autoHighGoalInt--;
                    autoHighGoalText.setText(Integer.toString(autoHighGoalInt));
                }
            }
        });

        Button autoHighGoalPlusBtn = (Button) rootView.findViewById(R.id.auto_high_goal_plus_btn);
        autoHighGoalPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView autoHighGoalText = (TextView) rootView.findViewById(R.id.auto_high_goal_text);
                int autoHighGoalInt = Integer.parseInt(autoHighGoalText.getText().toString());

                if (autoHighGoalInt < 100) {
                    autoHighGoalInt++;
                    autoHighGoalText.setText(Integer.toString(autoHighGoalInt));
                }
            }
        });

        Button autoLowGoalMinusBtn = (Button) rootView.findViewById(R.id.auto_low_goal_minus_btn);
        autoLowGoalMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView autoLowGoalText = (TextView) rootView.findViewById(R.id.auto_low_goal_text);
                int autoLowGoalInt = Integer.parseInt(autoLowGoalText.getText().toString());

                if (autoLowGoalInt > 0) {
                    autoLowGoalInt--;
                    autoLowGoalText.setText(Integer.toString(autoLowGoalInt));
                }
            }
        });

        Button autoLowGoalPlusBtn = (Button) rootView.findViewById(R.id.auto_low_goal_plus_btn);
        autoLowGoalPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView autoLowGoalText = (TextView) rootView.findViewById(R.id.auto_low_goal_text);
                int autoLowGoalInt = Integer.parseInt(autoLowGoalText.getText().toString());

                if (autoLowGoalInt < 100) {
                    autoLowGoalInt++;
                    autoLowGoalText.setText(Integer.toString(autoLowGoalInt));
                }
            }
        });

        Button teleopHighGoalMinusBtn = (Button) rootView.findViewById(R.id.teleop_high_goal_minus_btn);
        teleopHighGoalMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView teleopHighGoalText = (TextView) rootView.findViewById(R.id.teleop_high_goal_text);
                int teleopHighGoalInt = Integer.parseInt(teleopHighGoalText.getText().toString());

                if (teleopHighGoalInt > 0) {
                    teleopHighGoalInt--;
                    teleopHighGoalText.setText(Integer.toString(teleopHighGoalInt));
                }
            }
        });

        Button teleopHighGoalPlusBtn = (Button) rootView.findViewById(R.id.teleop_high_goal_plus_btn);
        teleopHighGoalPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView teleopHighGoalText = (TextView) rootView.findViewById(R.id.teleop_high_goal_text);
                int teleopHighGoalInt = Integer.parseInt(teleopHighGoalText.getText().toString());

                if (teleopHighGoalInt < 100) {
                    teleopHighGoalInt++;
                    teleopHighGoalText.setText(Integer.toString(teleopHighGoalInt));
                }
            }
        });

        Button teleopLowGoalMinusBtn = (Button) rootView.findViewById(R.id.teleop_low_goal_minus_btn);
        teleopLowGoalMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView teleopLowGoalText = (TextView) rootView.findViewById(R.id.teleop_low_goal_text);
                int teleopLowGoalInt = Integer.parseInt(teleopLowGoalText.getText().toString());

                if (teleopLowGoalInt > 0) {
                    teleopLowGoalInt--;
                    teleopLowGoalText.setText(Integer.toString(teleopLowGoalInt));
                }
            }
        });

        Button teleopLowGoalPlusBtn = (Button) rootView.findViewById(R.id.teleop_low_goal_plus_btn);
        teleopLowGoalPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView teleopLowGoalText = (TextView) rootView.findViewById(R.id.teleop_low_goal_text);
                int teleopLowGoalInt = Integer.parseInt(teleopLowGoalText.getText().toString());

                if (teleopLowGoalInt < 100) {
                    teleopLowGoalInt++;
                    teleopLowGoalText.setText(Integer.toString(teleopLowGoalInt));
                }
            }
        });

        Button teleopCrossingsMinusBtn = (Button) rootView.findViewById(R.id.teleop_crossings_minus_btn);
        teleopCrossingsMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView teleopCrossingsText = (TextView) rootView.findViewById(R.id.teleop_crossings_text);
                int teleopCrossingsInt = Integer.parseInt(teleopCrossingsText.getText().toString());

                if (teleopCrossingsInt > 0) {
                    teleopCrossingsInt--;
                    teleopCrossingsText.setText(Integer.toString(teleopCrossingsInt));
                }
            }
        });

        Button teleopCrossingsPlusBtn = (Button) rootView.findViewById(R.id.teleop_crossings_plus_btn);
        teleopCrossingsPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView teleopCrossingsText = (TextView) rootView.findViewById(R.id.teleop_crossings_text);
                int teleopCrossingsInt = Integer.parseInt(teleopCrossingsText.getText().toString());

                if (teleopCrossingsInt < 100) {
                    teleopCrossingsInt++;
                    teleopCrossingsText.setText(Integer.toString(teleopCrossingsInt));
                }
            }
        });

        Spinner matchSpinner = (Spinner) rootView.findViewById(R.id.match_spinner);
        matchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                clearScreen(rootView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner teamSpinner = (Spinner) rootView.findViewById(R.id.team_spinner);
        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                clearScreen(rootView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buildMatchSpinner("north_shore", rootView);
        buildTeamSpinner("north_shore", rootView);
        buildDefenseSpinner(rootView);
        buildEndgameSpinner(rootView);

        return rootView;
    }

    public void buildMatchSpinner(String event, View rootView) {
        Spinner matchSpinner = (Spinner) rootView.findViewById(R.id.match_spinner);

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.matches, R.layout.spinner_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        matchSpinner.setAdapter(dataAdapter);
    }

    public void buildTeamSpinner(String event, View rootView) {
        Spinner teamSpinner = (Spinner) rootView.findViewById(R.id.team_spinner);

        Integer teamList;

        if (event.equals("north_shore")) {
            teamList = R.array.north_shore_teams;
        } else {
            teamList = R.array.pine_tree_teams;
        }

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(rootView.getContext(),
                teamList, R.layout.spinner_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        teamSpinner.setAdapter(dataAdapter);
    }

    public void buildDefenseSpinner(View rootView) {
        Spinner defenseSpinner = (Spinner) rootView.findViewById(R.id.defense_spinner);

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.defense, R.layout.spinner_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        defenseSpinner.setAdapter(dataAdapter);
    }

    public void buildEndgameSpinner(View rootView) {
        Spinner endgameSpinner = (Spinner) rootView.findViewById(R.id.endgame_spinner);

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.endgame, R.layout.spinner_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        endgameSpinner.setAdapter(dataAdapter);
    }

    public void clearScreen(View rootView) {
        Spinner defenseSpinner = (Spinner) rootView.findViewById(R.id.defense_spinner);
        defenseSpinner.setSelection(0);

        Spinner endgameSpinner = (Spinner) rootView.findViewById(R.id.endgame_spinner);
        endgameSpinner.setSelection(0);

        TextView autoHighGoalText = (TextView) rootView.findViewById(R.id.auto_high_goal_text);
        autoHighGoalText.setText("0");

        TextView autoLowGoalText = (TextView) rootView.findViewById(R.id.auto_low_goal_text);
        autoLowGoalText.setText("0");

        TextView teleopHighGoalText = (TextView) rootView.findViewById(R.id.teleop_high_goal_text);
        teleopHighGoalText.setText("0");

        TextView teleopLowGoalText = (TextView) rootView.findViewById(R.id.teleop_low_goal_text);
        teleopLowGoalText.setText("0");

        TextView teleopCrossingsText = (TextView) rootView.findViewById(R.id.teleop_crossings_text);
        teleopCrossingsText.setText("0");
    }

    private class AsyncTaskURL extends AsyncTask<String, Void, String> {

        TextView eventText = (TextView) rootView.findViewById(R.id.event);
        Spinner matchSpinner = (Spinner) rootView.findViewById(R.id.match_spinner);
        Spinner teamSpinner = (Spinner) rootView.findViewById(R.id.team_spinner);
        Spinner defenseSpinner = (Spinner) rootView.findViewById(R.id.defense_spinner);
        Spinner endgameSpinner = (Spinner) rootView.findViewById(R.id.endgame_spinner);
        TextView autoHighGoalText = (TextView) rootView.findViewById(R.id.auto_high_goal_text);
        TextView autoLowGoalText = (TextView) rootView.findViewById(R.id.auto_low_goal_text);
        TextView teleopHighGoalText = (TextView) rootView.findViewById(R.id.teleop_high_goal_text);
        TextView teleopLowGoalText = (TextView) rootView.findViewById(R.id.teleop_low_goal_text);
        TextView teleopCrossingsText = (TextView) rootView.findViewById(R.id.teleop_crossings_text);

        String event = eventText.getText().toString();
        String match_no = matchSpinner.getSelectedItem().toString();
        String team = teamSpinner.getSelectedItem().toString();
        String defense = defenseSpinner.getSelectedItem().toString();
        String endgame = endgameSpinner.getSelectedItem().toString();
        String autoHighGoal = autoHighGoalText.getText().toString();
        String autoLowGoal = autoLowGoalText.getText().toString();
        String teleopHighGoal = teleopHighGoalText.getText().toString();
        String teleopLowGoal = teleopLowGoalText.getText().toString();
        String teleopCrossings = teleopCrossingsText.getText().toString();

        String result;

        @Override
        protected String doInBackground(String... params) {
            try {
                String urlString = "http://76.179.97.182/insertData.php?" +
                        "event=" + event +
                        "&match_no=" + match_no +
                        "&team=" + team +
                        "&auto_high=" + autoHighGoal +
                        "&auto_low=" + autoLowGoal +
                        "&auto_cross=" + defense +
                        "&tele_high=" + teleopHighGoal +
                        "&tele_low=" + teleopLowGoal +
                        "&tele_cross=" + teleopCrossings +
                        "&scale=" + endgame;

                // Do your long operations here and return the result
                //URL url = new URL("http://76.179.97.182/getData.php");
                URL url = new URL(urlString);
                //URL url = new URL("http://www.android.com/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                result = CharStreams.toString(new InputStreamReader(
                        in, Charsets.UTF_8));

            } catch (Exception e) {
                result = "Weird error";
            }

            return result;
            //return "Test";
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            TextView infoText = (TextView) rootView.findViewById(R.id.info_text);
            infoText.setText(result);


            Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
            fadeOut.setDuration(5000);
            infoText.startAnimation(fadeOut);

            fadeOut.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    TextView infoText = (TextView) rootView.findViewById(R.id.info_text);
                    infoText.setText("");
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            try {
                JSONArray jsonArray = new JSONArray(result);

                String tempString = jsonArray.toString();

            } catch (JSONException e) {

            }

        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog

//            if (event.equals("North Shore")) {
//                event = "north_shore";
//            } else {
//                event = "pine_tree";
//            }

            if (defense.equals("Reach")) {
                defense = "2";
            } else if (defense.equals("Cross")) {
                defense = "10";
            } else {
                defense = "0";
            }

            if (endgame.equals("Challenge")) {
                endgame = "5";
            } else if (endgame.equals("Scale")) {
                endgame = "15";
            } else {
                endgame = "0";
            }
        }
    }

    private class AsyncTaskInsertData extends AsyncTask<String, Void, String> {

        TextView eventText = (TextView) rootView.findViewById(R.id.event);
        Spinner matchSpinner = (Spinner) rootView.findViewById(R.id.match_spinner);
        Spinner teamSpinner = (Spinner) rootView.findViewById(R.id.team_spinner);
        Spinner defenseSpinner = (Spinner) rootView.findViewById(R.id.defense_spinner);
        Spinner endgameSpinner = (Spinner) rootView.findViewById(R.id.endgame_spinner);
        TextView autoHighGoalText = (TextView) rootView.findViewById(R.id.auto_high_goal_text);
        TextView autoLowGoalText = (TextView) rootView.findViewById(R.id.auto_low_goal_text);
        TextView teleopHighGoalText = (TextView) rootView.findViewById(R.id.teleop_high_goal_text);
        TextView teleopLowGoalText = (TextView) rootView.findViewById(R.id.teleop_low_goal_text);
        TextView teleopCrossingsText = (TextView) rootView.findViewById(R.id.teleop_crossings_text);

        String event = eventText.getText().toString();
        String match_no = matchSpinner.getSelectedItem().toString();
        String team = teamSpinner.getSelectedItem().toString();
        Integer autoHighGoal = Integer.parseInt(autoHighGoalText.getText().toString());
        Integer autoLowGoal = Integer.parseInt(autoLowGoalText.getText().toString());
        Integer teleopHighGoal = Integer.parseInt(teleopHighGoalText.getText().toString());
        Integer teleopLowGoal = Integer.parseInt(teleopLowGoalText.getText().toString());
        Integer teleopCrossings = Integer.parseInt(teleopCrossingsText.getText().toString());

        Integer defense;
        Integer endgame;

        ProgressDialog progress = new ProgressDialog(rootView.getContext());

        @Override
        protected String doInBackground(String... params) {
            try {
                dbHelper.insertStandScouting(event, match_no, team, autoHighGoal, autoLowGoal,
                        defense, teleopHighGoal, teleopLowGoal, teleopCrossings, endgame);

            } catch (Exception e) {
                return "Failure";
            }

            return "Success";
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();

            Toast.makeText(rootView.getContext(), result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            progress.setTitle("Saving");
            progress.setMessage("Please wait...");
            progress.show();

            if (defenseSpinner.getSelectedItem().toString().equals("Reach")) {
                defense = 2;
            } else if (defenseSpinner.getSelectedItem().toString().equals("Cross")) {
                defense = 10;
            } else {
                defense = 0;
            }

            if (endgameSpinner.getSelectedItem().toString().equals("Challenge")) {
                endgame = 5;
            } else if (endgameSpinner.getSelectedItem().toString().equals("Scale")) {
                endgame = 15;
            } else {
                endgame = 0;
            }
        }
    }
}