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
import android.widget.EditText;
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
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_START_LEFT, false);
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_START_CENTER, false);
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_START_RIGHT, false);
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_HAS_AUTONOMOUS, false);
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_PIT_COMMENT, "");
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
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_LOW) != scoreLowCheckBox.isChecked()) {
                            currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_LOW, scoreLowCheckBox.isChecked());
                            dbHelper.updatePitInfo(currTeam);
                        }
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
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_HIGH) != scoreHighCheckBox.isChecked()) {
                            currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_HIGH, scoreHighCheckBox.isChecked());
                            dbHelper.updatePitInfo(currTeam);
                        }
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
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_CAN_BLOCK) != canBlockCheckBox.isChecked()) {
                            currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_BLOCK, canBlockCheckBox.isChecked());
                            dbHelper.updatePitInfo(currTeam);
                        }
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
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_CAN_CLIMB) != canClimbCheckBox.isChecked()) {
                            currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_CAN_CLIMB, canClimbCheckBox.isChecked());
                            dbHelper.updatePitInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final CheckBox startLeftCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_start_left_check);
        startLeftCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_START_LEFT) != startLeftCheckBox.isChecked()) {
                            currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_START_LEFT, startLeftCheckBox.isChecked());
                            dbHelper.updatePitInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final CheckBox startCenterCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_start_center_check);
        startCenterCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_START_CENTER) != startCenterCheckBox.isChecked()) {
                            currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_START_CENTER, startCenterCheckBox.isChecked());
                            dbHelper.updatePitInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final CheckBox startRightCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_start_right_check);
        startRightCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_START_RIGHT) != startRightCheckBox.isChecked()) {
                            currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_START_RIGHT, startRightCheckBox.isChecked());
                            dbHelper.updatePitInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final CheckBox hasAutonomousCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_has_autonomous_check);
        hasAutonomousCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_HAS_AUTONOMOUS) != hasAutonomousCheckBox.isChecked()) {
                            currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_HAS_AUTONOMOUS, hasAutonomousCheckBox.isChecked());
                            dbHelper.updatePitInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final EditText pitCommentEditText = (EditText) mRootView.findViewById(R.id.pit_comments_text);
        pitCommentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        currTeam.put(DBContract.TablePitInfo.COLUMN_NAME_PIT_COMMENT, pitCommentEditText.getText());
                        dbHelper.updatePitInfo(currTeam);
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

            CheckBox startLeftCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_start_left_check);
            startLeftCheckBox.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_START_LEFT));

            CheckBox startCenterCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_start_center_check);
            startCenterCheckBox.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_START_CENTER));

            CheckBox startRightCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_start_right_check);
            startRightCheckBox.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_START_RIGHT));

            CheckBox hasAutonomousCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_has_autonomous_check);
            hasAutonomousCheckBox.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLUMN_NAME_HAS_AUTONOMOUS));

            EditText pitCommentEditText = (EditText) mRootView.findViewById(R.id.pit_comments_text);
            pitCommentEditText.setText(currTeam.getString(DBContract.TablePitInfo.COLUMN_NAME_PIT_COMMENT));

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

        CheckBox startLeftCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_start_left_check);
        startLeftCheckBox.setChecked(false);

        CheckBox startCenterCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_start_center_check);
        startCenterCheckBox.setChecked(false);

        CheckBox startRightCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_start_right_check);
        startRightCheckBox.setChecked(false);

        CheckBox hasAutonomousCheckBox = (CheckBox) mRootView.findViewById(R.id.pit_has_autonomous_check);
        hasAutonomousCheckBox.setChecked(false);

        EditText pitCommentEditText = (EditText) mRootView.findViewById(R.id.pit_comments_text);
        pitCommentEditText.setText("");

    }

}
