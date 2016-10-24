package com.bertrobotics.bertscout2017;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class StatisticsFragment extends Fragment {

    public View mRootView;
    public DBHelper dbHelper;
    ListView mListView;
    FragmentActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.statistics, container, false);
        dbHelper = new DBHelper(mRootView.getContext());

        mListView = (ListView) mRootView.findViewById(R.id.data_listview);

        mActivity = getActivity();

        Spinner sortSpinner = (Spinner) mRootView.findViewById(R.id.sort_spinner);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                populateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner sortOrderSpinner = (Spinner) mRootView.findViewById(R.id.sort_order_spinner);
        sortOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                populateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buildSortSpinner();
        buildSortOrderSpinner();

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        populateList();
    }

    public void buildSortSpinner() {
        Spinner sortSpinner = (Spinner) mRootView.findViewById(R.id.sort_spinner);

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(mRootView.getContext(),
                R.array.sorts, R.layout.spinner_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        sortSpinner.setAdapter(dataAdapter);
    }

    public void buildSortOrderSpinner() {
        Spinner sortOrderSpinner = (Spinner) mRootView.findViewById(R.id.sort_order_spinner);

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(mRootView.getContext(),
                R.array.sort_order, R.layout.spinner_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        sortOrderSpinner.setAdapter(dataAdapter);
    }

    public void populateList() {
        if (mActivity == null) {
            return;
        }

        String event = mActivity.getTitle().toString();

        JSONArray data = dbHelper.getData(event);

        ArrayList<String> teams = new ArrayList<String>();

        TeamData[] totalTeamData = new TeamData[40];

        boolean teamFound;

        Integer lastIndex = 0;

        for (int i = 0; i < data.length(); i++) {
            try {
                Integer team = Integer.parseInt(data.getJSONObject(i).getString("team"));
                Integer autoHighGoal = Integer.parseInt(data.getJSONObject(i).getString("auto_high"));
                Integer autoLowGoal = Integer.parseInt(data.getJSONObject(i).getString("auto_low"));
                Integer defense = Integer.parseInt(data.getJSONObject(i).getString("auto_cross"));
                Integer teleopHighGoal = Integer.parseInt(data.getJSONObject(i).getString("tele_high"));
                Integer teleopLowGoal = Integer.parseInt(data.getJSONObject(i).getString("tele_low"));
                Integer teleopCrossings = Integer.parseInt(data.getJSONObject(i).getString("tele_cross"));
                Integer endgame = Integer.parseInt(data.getJSONObject(i).getString("endgame"));

                teamFound = false;
                Integer nextIndex = 0;

                for (int j = 0; j < totalTeamData.length; j++) {
                    if (totalTeamData[j] == null) {
                        nextIndex = j;
                        break;
                    } else if (totalTeamData[j].team == team) {
                        teamFound = true;

                        totalTeamData[j].numMatches++;
                        totalTeamData[j].totalAutoHigh += autoHighGoal;
                        totalTeamData[j].totalAutoLow += autoLowGoal;
                        totalTeamData[j].totalAutoCrossing += defense;
                        totalTeamData[j].totalTeleopHigh += teleopHighGoal;
                        totalTeamData[j].totalTeleopLow += teleopLowGoal;
                        totalTeamData[j].totalTeleopCrossing += teleopCrossings;
                        totalTeamData[j].totalEndgame += endgame;

                        break;
                    }
                }

                if (!teamFound) {
                    TeamData teamData = new TeamData(this);

                    teamData.team = team;
                    teamData.numMatches = 1;
                    teamData.totalAutoHigh = autoHighGoal;
                    teamData.totalAutoLow = autoLowGoal;
                    teamData.totalAutoCrossing = defense;
                    teamData.totalTeleopHigh = teleopHighGoal;
                    teamData.totalTeleopLow = teleopLowGoal;
                    teamData.totalTeleopCrossing = teleopCrossings;
                    teamData.totalEndgame = endgame;

                    totalTeamData[nextIndex] = teamData;
                    lastIndex = nextIndex;
                }


            } catch (JSONException e) {

            }
        }

        TeamData[] sortedTeamData = new TeamData[lastIndex + 1];

        for (int i = 0; i <= lastIndex; i++) {
            sortedTeamData[i] = totalTeamData[i];
        }

        Arrays.sort(sortedTeamData);

        for (int i = 0; i < sortedTeamData.length; i++) {
            if (totalTeamData[i] == null) {
                break;
            }

            teams.add(String.valueOf(i + 1) + ")  " + Integer.toString(sortedTeamData[i].team) +
                    "  Avg Auto High: " + String.format("%.2f", (double) sortedTeamData[i].totalAutoHigh / (double) sortedTeamData[i].numMatches) +
                    "  Avg Teleop High: " + String.format("%.2f", (double) sortedTeamData[i].totalTeleopHigh / (double) sortedTeamData[i].numMatches) +
                    "  Total Matches: " + String.valueOf(sortedTeamData[i].numMatches));

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mRootView.getContext(),
                android.R.layout.simple_list_item_1, teams);

        mListView.setAdapter(adapter);
    }

    public class StatisticsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

            }


            return convertView;
        }
    }

    public class TeamData implements Comparable<TeamData> {
        public StatisticsFragment statisticsFragment;
        int team;
        int numMatches;
        int totalAutoHigh;
        int totalAutoLow;
        int totalAutoCrossing;
        int totalTeleopHigh;
        int totalTeleopLow;
        int totalTeleopCrossing;
        int totalEndgame;

        public TeamData(StatisticsFragment statisticsFragment) {
            this.statisticsFragment = statisticsFragment;
            team = 0;
            numMatches = 0;
            totalAutoHigh = 0;
            totalAutoLow = 0;
            totalAutoCrossing = 0;
            totalTeleopHigh = 0;
            totalTeleopLow = 0;
            totalTeleopCrossing = 0;
            totalEndgame = 0;
        }

        @Override
        public int compareTo(TeamData td) {
            Spinner sortSpinner = (Spinner) mRootView.findViewById(R.id.sort_spinner);
            String sortField = sortSpinner.getSelectedItem().toString();

            Spinner sortOrderSpinner = (Spinner) mRootView.findViewById(R.id.sort_order_spinner);
            String sortOrderField = sortOrderSpinner.getSelectedItem().toString();

            switch (sortField) {
                case "Team Number":
                    if (sortOrderField.equals("Asc")) {
                        return this.team - td.team;
                    } else {
                        return td.team - this.team;
                    }
                case "Avg Auto High":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalAutoHigh / (double) this.numMatches, (double) td.totalAutoHigh / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalAutoHigh / (double) td.numMatches, (double) this.totalAutoHigh / (double) this.numMatches);
                    }
                case "Avg Auto Low":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalAutoLow / (double) this.numMatches, (double) td.totalAutoLow / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalAutoLow / (double) td.numMatches, (double) this.totalAutoLow / (double) this.numMatches);
                    }
                case "Avg Auto Cross":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalAutoCrossing / (double) this.numMatches, (double) td.totalAutoCrossing / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalAutoCrossing / (double) td.numMatches, (double) this.totalAutoCrossing / (double) this.numMatches);
                    }
                case "Avg Teleop High":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalTeleopHigh / (double) this.numMatches, (double) td.totalTeleopHigh / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalTeleopHigh / (double) td.numMatches, (double) this.totalTeleopHigh / (double) this.numMatches);
                    }
                case "Avg Teleop Low":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalTeleopLow / (double) this.numMatches, (double) td.totalTeleopLow / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalTeleopLow / (double) td.numMatches, (double) this.totalTeleopLow / (double) this.numMatches);
                    }
                case "Avg Teleop Cross":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalTeleopCrossing / (double) this.numMatches, (double) td.totalTeleopCrossing / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalTeleopCrossing / (double) td.numMatches, (double) this.totalTeleopCrossing / (double) this.numMatches);
                    }
                case "Avg Endgame":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalEndgame / (double) this.numMatches, (double) td.totalEndgame / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalEndgame / (double) td.numMatches, (double) this.totalEndgame / (double) this.numMatches);
                    }
            }

            return 0;
        }
    }
}