package com.bertrobotics.bertscout2017;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

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
    TeamData[] sortedTeamData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.statistics, container, false);
        dbHelper = new DBHelper(mRootView.getContext());

        mListView = (ListView) mRootView.findViewById(R.id.data_listview);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent intent = new Intent(mRootView.getContext(), TeamDetails.class);
                intent.putExtra("team", sortedTeamData[position].team);
                startActivityForResult(intent, 1);
            }
        });

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

        JSONArray data = dbHelper.getDataAllStand(0);

        ArrayList<String> teams = new ArrayList<String>();

        TeamData[] totalTeamData = new TeamData[70];

        boolean teamFound;

        Integer lastIndex = 0;

        for (int i = 0; i < data.length(); i++) {
            try {

                Integer autoBaseLine = 0;
                Integer autoPlaceGear = 0;
                Integer teleopTouchpad = 0;

                Integer team = Integer.parseInt(data.getJSONObject(i).getString("team"));
                Integer autoHighGoal = Integer.parseInt(data.getJSONObject(i).getString("auto_score_high"));
                Integer autoLowGoal = Integer.parseInt(data.getJSONObject(i).getString("auto_score_low"));
                Integer teleopHighGoal = Integer.parseInt(data.getJSONObject(i).getString("tele_score_high"));
                Integer teleopLowGoal = Integer.parseInt(data.getJSONObject(i).getString("tele_score_low"));
                Integer teleopGearsPlaced = Integer.parseInt(data.getJSONObject(i).getString("tele_gears_placed"));
                Integer teleopPenalties = Integer.parseInt(data.getJSONObject(i).getString("tele_penalties"));

                if (data.getJSONObject(i).getString("auto_base_line").equals("true")) {
                    autoBaseLine = 1;
                }

                if (data.getJSONObject(i).getString("auto_place_gear").equals("true")) {
                    autoPlaceGear = 1;
                }

                if (data.getJSONObject(i).getString("tele_touchpad").equals("true")) {
                    teleopTouchpad = 1;
                }

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
                        totalTeamData[j].totalAutoBaseLine += autoBaseLine;
                        totalTeamData[j].totalAutoPlaceGear += autoPlaceGear;
                        totalTeamData[j].totalTeleopHigh += teleopHighGoal;
                        totalTeamData[j].totalTeleopLow += teleopLowGoal;
                        totalTeamData[j].totalTeleopGearsPlaced += teleopGearsPlaced;
                        totalTeamData[j].totalTouchpad += teleopTouchpad;
                        totalTeamData[j].totalPenalties += teleopPenalties;

                        break;
                    }
                }

                if (!teamFound) {
                    TeamData teamData = new TeamData(this);

                    teamData.team = team;
                    teamData.numMatches = 1;
                    teamData.totalAutoHigh = autoHighGoal;
                    teamData.totalAutoLow = autoLowGoal;
                    teamData.totalAutoBaseLine = autoBaseLine;
                    teamData.totalAutoPlaceGear = autoPlaceGear;
                    teamData.totalTeleopHigh = teleopHighGoal;
                    teamData.totalTeleopLow = teleopLowGoal;
                    teamData.totalTeleopGearsPlaced = teleopGearsPlaced;
                    teamData.totalTouchpad = teleopTouchpad;
                    teamData.totalPenalties = teleopPenalties;

                    totalTeamData[nextIndex] = teamData;
                    lastIndex = nextIndex;
                }


            } catch (JSONException e) {

            }
        }

        sortedTeamData = new TeamData[lastIndex + 1];

        for (int i = 0; i <= lastIndex; i++) {
            sortedTeamData[i] = totalTeamData[i];
        }

        Arrays.sort(sortedTeamData);

        Integer teamSpacesLength;
        String teamSpaces = "";

        Integer spacesLength;
        String spaces = "";

        for (int i = 0; i < sortedTeamData.length; i++) {
            if (totalTeamData[i] == null) {
                break;
            }

            teamSpacesLength = (2 - String.valueOf(i + 1).length()) * 2;

            teamSpaces = "";

            for (int j = 0; j < teamSpacesLength; j++) {
                teamSpaces += " ";
            }

            spacesLength = (4 - String.valueOf(sortedTeamData[i].team).length()) * 2;

            spaces = "";

            for (int j = 0; j < spacesLength; j++) {
                spaces += " ";
            }

            teams.add(String.valueOf(i + 1) + ")  " + teamSpaces + Integer.toString(sortedTeamData[i].team) + spaces +
                    "                                                                                 " +
                    "   Matches Played: " + String.valueOf(sortedTeamData[i].numMatches) +
                    "\n\nAH: " + String.format("%.1f", (double) sortedTeamData[i].totalAutoHigh / (double) sortedTeamData[i].numMatches) +
                    "  AL: " + String.format("%.1f", (double) sortedTeamData[i].totalAutoLow / (double) sortedTeamData[i].numMatches) +
                    "  AB: " + String.format("%.1f", (double) sortedTeamData[i].totalAutoBaseLine / (double) sortedTeamData[i].numMatches) +
                    "  AG: " + String.format("%.1f", (double) sortedTeamData[i].totalAutoPlaceGear / (double) sortedTeamData[i].numMatches) +
                    "  TH: " + String.format("%.1f", (double) sortedTeamData[i].totalTeleopHigh / (double) sortedTeamData[i].numMatches) +
                    "  TL: " + String.format("%.1f", (double) sortedTeamData[i].totalTeleopLow / (double) sortedTeamData[i].numMatches) +
                    "  TG: " + String.format("%.1f", (double) sortedTeamData[i].totalTeleopGearsPlaced / (double) sortedTeamData[i].numMatches) +
                    "  TT: " + String.format("%.1f", (double) sortedTeamData[i].totalTouchpad / (double) sortedTeamData[i].numMatches) +
                    "  TP: " + String.format("%.1f", (double) sortedTeamData[i].totalPenalties / (double) sortedTeamData[i].numMatches));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mRootView.getContext(),
                android.R.layout.simple_list_item_1, teams);

        mListView.setAdapter(adapter);

//        mListView.setAdapter(new StatisticsAdapter(getActivity(), totalTeamData));
    }

//    public class StatisticsAdapter extends BaseAdapter {
//
//        Context context;
//        TeamData[] totalTeamData;
//        private LayoutInflater inflater;
//
//        public StatisticsAdapter(Activity context, TeamData[] td) {
//            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            totalTeamData = td;
//        }
//
//        @Override
//        public int getCount() {
//            return 0;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
////            if (convertView == null) {
////
////            }
//
//            View rowView = inflater.inflate(R.layout.stat_list_item, null);
//
//            TextView item = (TextView) rowView.findViewById(R.id.item_no);
//            item.setText(String.valueOf(position));
//
//            TextView team_no = (TextView) rowView.findViewById(R.id.team_no);
//            team_no.setText(String.valueOf(totalTeamData[position].team));
//
//            rowView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    Toast.makeText(context, "You Clicked " + position, Toast.LENGTH_LONG).show();
//                }
//            });
//
//            return rowView;
//        }
//    }

    public class TeamData implements Comparable<TeamData> {
        public StatisticsFragment statisticsFragment;
        int team;
        int numMatches;
        int totalAutoHigh;
        int totalAutoLow;
        int totalAutoBaseLine;
        int totalAutoPlaceGear;
        int totalTeleopHigh;
        int totalTeleopLow;
        int totalTeleopGearsPlaced;
        int totalTouchpad;
        int totalPenalties;

        public TeamData(StatisticsFragment statisticsFragment) {
            this.statisticsFragment = statisticsFragment;
            team = 0;
            numMatches = 0;
            totalAutoHigh = 0;
            totalAutoLow = 0;
            totalAutoBaseLine = 0;
            totalAutoPlaceGear = 0;
            totalTeleopHigh = 0;
            totalTeleopLow = 0;
            totalTeleopGearsPlaced = 0;
            totalTouchpad = 0;
            totalPenalties = 0;
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
                case "Avg Auto Baseline":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalAutoBaseLine / (double) this.numMatches, (double) td.totalAutoBaseLine / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalAutoBaseLine / (double) td.numMatches, (double) this.totalAutoBaseLine / (double) this.numMatches);
                    }
                case "Avg Auto Gear":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalAutoPlaceGear / (double) this.numMatches, (double) td.totalAutoPlaceGear / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalAutoPlaceGear / (double) td.numMatches, (double) this.totalAutoPlaceGear / (double) this.numMatches);
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
                case "Avg Teleop Gear":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalTeleopGearsPlaced / (double) this.numMatches, (double) td.totalTeleopGearsPlaced / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalTeleopGearsPlaced / (double) td.numMatches, (double) this.totalTeleopGearsPlaced / (double) this.numMatches);
                    }
                case "Avg Touchpad":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalTouchpad / (double) this.numMatches, (double) td.totalTouchpad / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalTouchpad / (double) td.numMatches, (double) this.totalTouchpad / (double) this.numMatches);
                    }
                case "Avg Penalties":
                    if (sortOrderField.equals("Asc")) {
                        return Double.compare((double) this.totalPenalties / (double) this.numMatches, (double) td.totalPenalties / (double) td.numMatches);
                    } else {
                        return Double.compare((double) td.totalPenalties / (double) td.numMatches, (double) this.totalPenalties / (double) this.numMatches);
                    }
            }

            return 0;
        }
    }
}