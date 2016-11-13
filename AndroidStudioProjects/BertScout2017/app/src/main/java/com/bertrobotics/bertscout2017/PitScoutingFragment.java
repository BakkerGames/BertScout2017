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

                currPitInfoIndex = -1;
                clearScreen();

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
                if (currPitInfoIndex >= 0) {
                    try {
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_LOW, scoreLowCheckBox.isChecked());
                        currPitInfoArray.put(currPitInfoIndex, currTeam);
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final CheckBox scoreHighCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_score_high_check);
        scoreHighCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_HIGH, scoreHighCheckBox.isChecked());
                        currPitInfoArray.put(currPitInfoIndex, currTeam);
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final CheckBox canBlockCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_can_block_check);
        canBlockCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_BLOCK, canBlockCheckBox.isChecked());
                        currPitInfoArray.put(currPitInfoIndex, currTeam);
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final CheckBox canClimbCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_can_climb_check);
        canClimbCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_CLIMB, canClimbCheckBox.isChecked());
                        currPitInfoArray.put(currPitInfoIndex, currTeam);
                    } catch (JSONException e) {
                    }
                }
            }
        });

        buildTeamSpinner("north_shore");

        return mRootView;
    }

    private void showPitInfo() {
        try {

            CheckBox canScoreLowCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_score_low_check);
            canScoreLowCheckBox.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_LOW));

            CheckBox canScoreHighCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_score_high_check);
            canScoreHighCheckBox.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_HIGH));

            CheckBox canBlockCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_can_block_check);
            canBlockCheckBox.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_CAN_BLOCK));

            CheckBox canClimbCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_can_climb_check);
            canClimbCheckBox.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_CAN_CLIMB));

        } catch (JSONException e) {
        }
    }

    public void buildTeamSpinner(String event) {

        currEvent = event;
        Integer teamList = null;

        Spinner teamSpinner = (Spinner) mRootView.findViewById(R.id.team_spinner);

        if (event.equals("north_shore")) {
            teamList = R.array.north_shore_teams;
        } else if (event.equals("pine_tree")) {
            teamList = R.array.pine_tree_teams;
        }

        if (teamList != null) {
            currPitInfoArray = dbHelper.getDataAllPit(currEvent);
            ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(mRootView.getContext(),
                    teamList, R.layout.spinner_item);
            dataAdapter.setDropDownViewResource(R.layout.spinner_item);
            teamSpinner.setAdapter(dataAdapter);
        } else {
            currPitInfoArray = new JSONArray();
            teamSpinner.setAdapter(null);
        }

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