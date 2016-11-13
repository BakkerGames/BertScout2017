package com.bertrobotics.bertscout2017;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PitScoutingFragment extends Fragment {

    public View mRootView;
    public DBHelper dbHelper;

    public String currEvent;
    public JSONArray currPitInfoArray;
    public int currPitInfoIndex;
    public JSONObject currTeam;

    StatisticsFragment mStatisticsFragment;

    public PitScoutingFragment(StatisticsFragment statisticsFragment) {
        mStatisticsFragment = statisticsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.pit_scouting, container, false);
        dbHelper = new DBHelper(mRootView.getContext());

        final Spinner teamSpinner = (Spinner) mRootView.findViewById(R.id.team_spinner);
        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                clearScreen();
                currPitInfoIndex = -1;
                int teamNumber = Integer.parseInt((String) teamSpinner.getSelectedItem());
                for (int i = 0; i < currPitInfoArray.length(); i++) {
                    try {
                        currTeam = currPitInfoArray.getJSONObject(i);
                        if (currTeam.getInt(DBContract.TablePitInfo.COLUMN_NAME_TEAM) == teamNumber) {
                            currPitInfoIndex = i;
                            break;
                        }
                    } catch (JSONException e) {
                    }
                }
                if (currPitInfoIndex < 0) {
                    currTeam = new JSONObject();
                    try {
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_EVENT, currEvent);
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_TEAM, teamNumber);
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_LOW, false);
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_HIGH, false);
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_BLOCK, false);
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_CLIMB, false);
                        currPitInfoArray.put(currTeam);
                        currPitInfoIndex = currPitInfoArray.length() - 1;
                    } catch (JSONException e) {
                    }
                }
                showPitInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final CheckBox scoreLowCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_score_low_check);
        scoreLowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            try {
                currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_LOW, scoreLowCheckBox.isChecked());
                currPitInfoArray.put(currPitInfoIndex, currTeam);
            } catch (JSONException e) {
            }
            }
        });

        final CheckBox scoreHighCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_score_high_check);
        scoreHighCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_HIGH, scoreHighCheckBox.isChecked());
                    currPitInfoArray.put(currPitInfoIndex, currTeam);
                } catch (JSONException e) {
                }
            }
        });

        final CheckBox canBlockCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_can_block_check);
        canBlockCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_BLOCK, canBlockCheckBox.isChecked());
                    currPitInfoArray.put(currPitInfoIndex, currTeam);
                } catch (JSONException e) {
                }
            }
        });

        final CheckBox canClimbCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_can_climb_check);
        canClimbCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_CLIMB, canClimbCheckBox.isChecked());
                    currPitInfoArray.put(currPitInfoIndex, currTeam);
                } catch (JSONException e) {
                }
            }
        });

        buildTeamSpinner("north_shore");

        return mRootView;
    }

    private void showPitInfo() {
        try {

            CheckBox canScoreLowCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_score_low_check);
            if (currTeam.getInt(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_LOW) == 0) {
                canScoreLowCheckBox.setChecked(false);
            } else {
                canScoreLowCheckBox.setChecked(true);
            }

            CheckBox canScoreHighCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_score_high_check);
            if (currTeam.getInt(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_HIGH) == 0) {
                canScoreHighCheckBox.setChecked(false);
            } else {
                canScoreHighCheckBox.setChecked(true);
            }

            CheckBox canBlockCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_can_block_check);
            if (currTeam.getInt(DBContract.TablePitInfo.COLUMN_NAME_CAN_BLOCK) == 0) {
                canBlockCheckBox.setChecked(false);
            } else {
                canBlockCheckBox.setChecked(true);
            }

            CheckBox canClimbCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_can_climb_check);
            if (currTeam.getInt(DBContract.TablePitInfo.COLUMN_NAME_CAN_BLOCK) == 0) {
                canClimbCheckBox.setChecked(false);
            } else {
                canClimbCheckBox.setChecked(true);
            }

        } catch (JSONException e) {
        }
    }

    public void buildTeamSpinner(String event) {
        Spinner teamSpinner = (Spinner) mRootView.findViewById(R.id.team_spinner);

        Integer teamList = null;

        if (event.equals("north_shore")) {
            teamList = R.array.north_shore_teams;
        } else if (event.equals("pine_tree")) {
            teamList = R.array.pine_tree_teams;
        }

        if (teamList != null) {
            ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(mRootView.getContext(),
                    teamList, R.layout.spinner_item);
            dataAdapter.setDropDownViewResource(R.layout.spinner_item);
            teamSpinner.setAdapter(dataAdapter);
        } else {
            teamSpinner.setAdapter(null);
        }

        // Handle in-memory information

        currEvent = event;
        currPitInfoArray = dbHelper.getDataAllPit(currEvent);

    }

    public void clearScreen() {

        CheckBox scoreLowCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_score_low_check);
        scoreLowCheckBox.setChecked(false);

        CheckBox scoreHighCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_score_high_check);
        scoreHighCheckBox.setChecked(false);

        CheckBox canBlockCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_can_block_check);
        canBlockCheckBox.setChecked(false);

        CheckBox canClimbCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_can_climb_check);
        canClimbCheckBox.setChecked(false);

    }

}