package com.bertrobotics.bertscout2017;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

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

    private boolean fillingInfo = false;

//    StatisticsFragment mStatisticsFragment;

    public PitScoutingFragment() {
        // default constructor
    }

//    public PitScoutingFragment(StatisticsFragment statisticsFragment) {
//        mStatisticsFragment = statisticsFragment;
//    }

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
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM) == teamNumber) {
                            currPitInfoIndex = i;
                            break;
                        }
                    } catch (JSONException e) {
                    }
                }

                if (currPitInfoIndex < 0) {
                    currTeam = new JSONObject();
                    try {
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_EVENT, currEvent);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TEAM, teamNumber);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_SCOUT_NAME, "");

                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TEAM_YEARS, 0);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TEAM_MEMBERS, 0);

                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_HEIGHT, 0);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_WEIGHT, 0);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_NUM_CIMS, 0);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_MAX_SPEED, 0);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_TYPE, "");
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_LAYOUT, "");
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_MAX_FUEL, 0);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_SPEED, 0);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_LOCATION, "");

                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_HIGH, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_LOW, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_FLOOR_PICKUP, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TOP_LOADER, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_AIM, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_CARRY_GEAR, false);

                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_CLIMB, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_OWN_ROPE, false);

                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_START_LEFT, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_START_CENTER, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_START_RIGHT, false);

                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_NUM_MODES, 0);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_BASE_LINE, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_PLACE_GEAR, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HIGH_GOAL, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_LOW_GOAL, false);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HOPPER, false);

                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TEAM_RATING, 0);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_COMMENT, "");

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

        final EditText teamYearsTextbox = (EditText) mRootView.findViewById(R.id.team_years);
        teamYearsTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        int tempValue = Integer.parseInt(teamYearsTextbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_YEARS) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TEAM_YEARS, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button teamYearsMinusButton = (Button) mRootView.findViewById(R.id.team_years_minus_btn);
        teamYearsMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView teamMembersText = (TextView) mRootView.findViewById(R.id.team_years);
                teamMembersText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(teamMembersText.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        teamMembersText.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                }
            }
        });

        Button teamYearsPlusButton = (Button) mRootView.findViewById(R.id.team_years_plus_btn);
        teamYearsPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView teamMembersText = (TextView) mRootView.findViewById(R.id.team_years);
                teamMembersText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(teamMembersText.getText().toString());
                    tempValue++;
                    teamMembersText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                }
            }
        });

        final EditText teamMembersTextbox = (EditText) mRootView.findViewById(R.id.team_members);
        teamMembersTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        int tempValue = Integer.parseInt(teamMembersTextbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_MEMBERS) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TEAM_MEMBERS, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button teamMemberMinusButton = (Button) mRootView.findViewById(R.id.team_members_minus_btn);
        teamMemberMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView teamMemberText = (TextView) mRootView.findViewById(R.id.team_members);
                teamMemberText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(teamMemberText.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        teamMemberText.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                }
            }
        });

        Button teamMemberPlusButton = (Button) mRootView.findViewById(R.id.team_members_plus_btn);
        teamMemberPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView teamMemberText = (TextView) mRootView.findViewById(R.id.team_members);
                teamMemberText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(teamMemberText.getText().toString());
                    tempValue++;
                    teamMemberText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                }
            }
        });

        final EditText heightTextbox = (EditText) mRootView.findViewById(R.id.height);
        heightTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        int tempValue = Integer.parseInt(heightTextbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_HEIGHT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_HEIGHT, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button heightMinusButton = (Button) mRootView.findViewById(R.id.height_minus_btn);
        heightMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView heightText = (TextView) mRootView.findViewById(R.id.height);
                heightText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(heightText.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        heightText.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                }
            }
        });

        Button heightPlusButton = (Button) mRootView.findViewById(R.id.height_plus_btn);
        heightPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView heightText = (TextView) mRootView.findViewById(R.id.height);
                heightText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(heightText.getText().toString());
                    tempValue++;
                    heightText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                }
            }
        });

        final EditText weightTextbox = (EditText) mRootView.findViewById(R.id.weight);
        weightTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        int tempValue = Integer.parseInt(weightTextbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_WEIGHT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_WEIGHT, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button weightMinusButton = (Button) mRootView.findViewById(R.id.weight_minus_btn);
        weightMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView weightText = (TextView) mRootView.findViewById(R.id.weight);
                weightText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(weightText.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        weightText.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                }
            }
        });

        Button weightPlusButton = (Button) mRootView.findViewById(R.id.weight_plus_btn);
        weightPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView weightText = (TextView) mRootView.findViewById(R.id.weight);
                weightText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(weightText.getText().toString());
                    tempValue++;
                    weightText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                }
            }
        });

        final EditText numCimsTextbox = (EditText) mRootView.findViewById(R.id.num_cims);
        numCimsTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        int tempValue = Integer.parseInt(numCimsTextbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_NUM_CIMS) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_NUM_CIMS, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button numCimsMinusButton = (Button) mRootView.findViewById(R.id.num_cims_minus_btn);
        numCimsMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView numCimsText = (TextView) mRootView.findViewById(R.id.num_cims);
                numCimsText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(numCimsText.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        numCimsText.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                }
            }
        });

        Button numCimsPlusButton = (Button) mRootView.findViewById(R.id.num_cims_plus_btn);
        numCimsPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView numCimsText = (TextView) mRootView.findViewById(R.id.num_cims);
                numCimsText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(numCimsText.getText().toString());
                    tempValue++;
                    numCimsText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                }
            }
        });

        final EditText maxSpeedTextbox = (EditText) mRootView.findViewById(R.id.max_speed);
        maxSpeedTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        int tempValue = Integer.parseInt(maxSpeedTextbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_MAX_SPEED) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_MAX_SPEED, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button maxSpeedMinusButton = (Button) mRootView.findViewById(R.id.max_speed_minus_btn);
        maxSpeedMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView maxSpeedText = (TextView) mRootView.findViewById(R.id.max_speed);
                maxSpeedText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(maxSpeedText.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        maxSpeedText.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                }
            }
        });

        Button maxSpeedPlusButton = (Button) mRootView.findViewById(R.id.max_speed_plus_btn);
        maxSpeedPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView maxSpeedText = (TextView) mRootView.findViewById(R.id.max_speed);
                maxSpeedText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(maxSpeedText.getText().toString());
                    tempValue++;
                    maxSpeedText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                }
            }
        });

        final EditText wheelTypeTextbox = (EditText) mRootView.findViewById(R.id.wheel_type);
        wheelTypeTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        String tempValue = wheelTypeTextbox.getText().toString();
                        if (currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_TYPE) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_TYPE, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final EditText wheelLayoutTextbox = (EditText) mRootView.findViewById(R.id.wheel_layout);
        wheelLayoutTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        String tempValue = wheelLayoutTextbox.getText().toString();
                        if (currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_LAYOUT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_LAYOUT, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final EditText maxFuelTextbox = (EditText) mRootView.findViewById(R.id.max_fuel);
        maxFuelTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        int tempValue = Integer.parseInt(maxFuelTextbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_MAX_FUEL) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_MAX_FUEL, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button maxFuelMinusButton = (Button) mRootView.findViewById(R.id.max_fuel_minus_btn);
        maxFuelMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView maxFuelText = (TextView) mRootView.findViewById(R.id.max_fuel);
                maxFuelText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(maxFuelText.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        maxFuelText.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                }
            }
        });

        Button maxFuelPlusButton = (Button) mRootView.findViewById(R.id.max_fuel_plus_btn);
        maxFuelPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView maxFuelText = (TextView) mRootView.findViewById(R.id.max_fuel);
                maxFuelText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(maxFuelText.getText().toString());
                    tempValue++;
                    maxFuelText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                }
            }
        });

        final EditText shootSpeedTextbox = (EditText) mRootView.findViewById(R.id.shoot_speed);
        shootSpeedTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        int tempValue = Integer.parseInt(shootSpeedTextbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_SPEED) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_SPEED, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button shootSpeedMinusButton = (Button) mRootView.findViewById(R.id.shoot_speed_minus_btn);
        shootSpeedMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView shootSpeedText = (TextView) mRootView.findViewById(R.id.shoot_speed);
                shootSpeedText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(shootSpeedText.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        shootSpeedText.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                }
            }
        });

        Button shootSpeedPlusButton = (Button) mRootView.findViewById(R.id.shoot_speed_plus_btn);
        shootSpeedPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView shootSpeedText = (TextView) mRootView.findViewById(R.id.shoot_speed);
                shootSpeedText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(shootSpeedText.getText().toString());
                    tempValue++;
                    shootSpeedText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                }
            }
        });

        final EditText shootLocationTextbox = (EditText) mRootView.findViewById(R.id.shoot_location);
        shootLocationTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        String tempValue = shootLocationTextbox.getText().toString();
                        if (currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_LOCATION) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_LOCATION, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton canShootHighButton = (ToggleButton) mRootView.findViewById(R.id.can_shoot_high);
        canShootHighButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = canShootHighButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_HIGH) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_HIGH, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton canShootLowButton = (ToggleButton) mRootView.findViewById(R.id.can_shoot_low);
        canShootLowButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = canShootLowButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_LOW) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_LOW, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton floorPickupButton = (ToggleButton) mRootView.findViewById(R.id.floor_pickup);
        floorPickupButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = floorPickupButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_FLOOR_PICKUP) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_FLOOR_PICKUP, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton topLoaderButton = (ToggleButton) mRootView.findViewById(R.id.top_loader);
        topLoaderButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = topLoaderButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_TOP_LOADER) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TOP_LOADER, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton autoAimButton = (ToggleButton) mRootView.findViewById(R.id.auto_aim);
        autoAimButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = autoAimButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_AIM) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_AIM, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton canCarryGearButton = (ToggleButton) mRootView.findViewById(R.id.can_carry_gear);
        canCarryGearButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = canCarryGearButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_CARRY_GEAR) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_CARRY_GEAR, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton canClimbButton = (ToggleButton) mRootView.findViewById(R.id.can_climb);
        canClimbButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = canClimbButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_CLIMB) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_CLIMB, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton ownRopeButton = (ToggleButton) mRootView.findViewById(R.id.own_rope);
        ownRopeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = ownRopeButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_OWN_ROPE) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_OWN_ROPE, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton startLeftButton = (ToggleButton) mRootView.findViewById(R.id.start_left);
        startLeftButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = startLeftButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_LEFT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_START_LEFT, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton startCenterButton = (ToggleButton) mRootView.findViewById(R.id.start_center);
        startCenterButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = startCenterButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_CENTER) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_START_CENTER, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton startRightButton = (ToggleButton) mRootView.findViewById(R.id.start_right);
        startRightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = startRightButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_RIGHT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_START_RIGHT, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final EditText autoNumModesTextbox = (EditText) mRootView.findViewById(R.id.auto_num_modes);
        autoNumModesTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        int tempValue = Integer.parseInt(autoNumModesTextbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_AUTO_NUM_MODES) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_NUM_MODES, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button autoNumModesMinusButton = (Button) mRootView.findViewById(R.id.auto_num_modes_minus_btn);
        autoNumModesMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView autoNumModesText = (TextView) mRootView.findViewById(R.id.auto_num_modes);
                autoNumModesText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(autoNumModesText.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        autoNumModesText.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                }
            }
        });

        Button autoNumModesPlusButton = (Button) mRootView.findViewById(R.id.auto_num_modes_plus_btn);
        autoNumModesPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView autoNumModesText = (TextView) mRootView.findViewById(R.id.auto_num_modes);
                autoNumModesText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(autoNumModesText.getText().toString());
                    tempValue++;
                    autoNumModesText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                }
            }
        });

        final ToggleButton autoBaseLineButton = (ToggleButton) mRootView.findViewById(R.id.auto_base_line);
        autoBaseLineButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = autoBaseLineButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_BASE_LINE) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_BASE_LINE, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton autoPlaceGearButton = (ToggleButton) mRootView.findViewById(R.id.auto_place_gear);
        autoPlaceGearButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = autoPlaceGearButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_PLACE_GEAR) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_PLACE_GEAR, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton autoHighGoalButton = (ToggleButton) mRootView.findViewById(R.id.auto_high_goal);
        autoHighGoalButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = autoHighGoalButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HIGH_GOAL) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HIGH_GOAL, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton autoLowGoalButton = (ToggleButton) mRootView.findViewById(R.id.auto_low_goal);
        autoLowGoalButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = autoLowGoalButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_LOW_GOAL) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_LOW_GOAL, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton autoHopperButton = (ToggleButton) mRootView.findViewById(R.id.auto_hopper);
        autoHopperButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = autoHopperButton.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HOPPER) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HOPPER, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final RatingBar teamRatingBar = (RatingBar) mRootView.findViewById(R.id.pit_team_rating);
        teamRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar teamRatingBar, float rating, boolean fromUser) {
                if (currPitInfoIndex >= 0) {
                    try {
                        int tempValue = (int) teamRatingBar.getRating();
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_RATING) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TEAM_RATING, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final EditText commentsTextbox = (EditText) mRootView.findViewById(R.id.comments);
        commentsTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currPitInfoIndex >= 0) {
                    try {
                        String tempValue = commentsTextbox.getText().toString();
                        if (currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_COMMENT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_COMMENT, tempValue);
                            if (!fillingInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
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

            fillingInfo = true;

            EditText teamYearsTextbox = (EditText) mRootView.findViewById(R.id.team_years);
            teamYearsTextbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_YEARS)));

            EditText teamMembersTextbox = (EditText) mRootView.findViewById(R.id.team_members);
            teamMembersTextbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_MEMBERS)));

            EditText heightTextbox = (EditText) mRootView.findViewById(R.id.height);
            heightTextbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_HEIGHT)));

            EditText weightTextbox = (EditText) mRootView.findViewById(R.id.weight);
            weightTextbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_WEIGHT)));

            EditText numCimsTextbox = (EditText) mRootView.findViewById(R.id.num_cims);
            numCimsTextbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_NUM_CIMS)));

            EditText maxSpeedTextbox = (EditText) mRootView.findViewById(R.id.max_speed);
            maxSpeedTextbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_MAX_SPEED)));

            EditText wheelTypeTextbox = (EditText) mRootView.findViewById(R.id.wheel_type);
            wheelTypeTextbox.setText(currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_TYPE));

            EditText wheelLayoutTextbox = (EditText) mRootView.findViewById(R.id.wheel_layout);
            wheelLayoutTextbox.setText(currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_LAYOUT));

            EditText maxFuelTextbox = (EditText) mRootView.findViewById(R.id.max_fuel);
            maxFuelTextbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_MAX_FUEL)));

            EditText shootSpeedTextbox = (EditText) mRootView.findViewById(R.id.shoot_speed);
            shootSpeedTextbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_SPEED)));

            EditText shootLocationTextbox = (EditText) mRootView.findViewById(R.id.shoot_location);
            shootLocationTextbox.setText(currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_LOCATION));

            ToggleButton canShootHighButton = (ToggleButton) mRootView.findViewById(R.id.can_shoot_high);
            canShootHighButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_HIGH));

            ToggleButton canShootLowButton = (ToggleButton) mRootView.findViewById(R.id.can_shoot_low);
            canShootLowButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_LOW));

            ToggleButton floorPickupButton = (ToggleButton) mRootView.findViewById(R.id.floor_pickup);
            floorPickupButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_FLOOR_PICKUP));

            ToggleButton topLoaderButton = (ToggleButton) mRootView.findViewById(R.id.top_loader);
            topLoaderButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_TOP_LOADER));

            ToggleButton autoAimButton = (ToggleButton) mRootView.findViewById(R.id.auto_aim);
            autoAimButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_AIM));

            ToggleButton canCarryGearButton = (ToggleButton) mRootView.findViewById(R.id.can_carry_gear);
            canCarryGearButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_CARRY_GEAR));

            ToggleButton canClimbButton = (ToggleButton) mRootView.findViewById(R.id.can_climb);
            canClimbButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_CLIMB));

            ToggleButton ownRopeButton = (ToggleButton) mRootView.findViewById(R.id.own_rope);
            ownRopeButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_OWN_ROPE));

            ToggleButton startLeftButton = (ToggleButton) mRootView.findViewById(R.id.start_left);
            startLeftButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_LEFT));

            ToggleButton startCenterButton = (ToggleButton) mRootView.findViewById(R.id.start_center);
            startCenterButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_CENTER));

            ToggleButton startRightButton = (ToggleButton) mRootView.findViewById(R.id.start_right);
            startRightButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_RIGHT));

            EditText autoNumModesTextbox = (EditText) mRootView.findViewById(R.id.auto_num_modes);
            autoNumModesTextbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_AUTO_NUM_MODES)));

            ToggleButton autoBaseLineButton = (ToggleButton) mRootView.findViewById(R.id.auto_base_line);
            autoBaseLineButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_BASE_LINE));

            ToggleButton autoPlaceGearButton = (ToggleButton) mRootView.findViewById(R.id.auto_place_gear);
            autoPlaceGearButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_PLACE_GEAR));

            ToggleButton autoHighGoalButton = (ToggleButton) mRootView.findViewById(R.id.auto_high_goal);
            autoHighGoalButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HIGH_GOAL));

            ToggleButton autoLowGoalButton = (ToggleButton) mRootView.findViewById(R.id.auto_low_goal);
            autoLowGoalButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_LOW_GOAL));

            ToggleButton autoHopperButton = (ToggleButton) mRootView.findViewById(R.id.auto_hopper);
            autoHopperButton.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HOPPER));

            RatingBar teamRatingBar = (RatingBar) mRootView.findViewById(R.id.pit_team_rating);
            teamRatingBar.setRating(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_RATING));

            EditText commentsTextbox = (EditText) mRootView.findViewById(R.id.comments);
            commentsTextbox.setText(currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_COMMENT));

            fillingInfo = false;

        } catch (JSONException e) {
            fillingInfo = false;
            //e.printStackTrace();
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

        EditText teamYearsTextbox = (EditText) mRootView.findViewById(R.id.team_years);
        teamYearsTextbox.setText("");

        EditText teamMembersTextbox = (EditText) mRootView.findViewById(R.id.team_members);
        teamMembersTextbox.setText("");

        EditText heightTextbox = (EditText) mRootView.findViewById(R.id.height);
        heightTextbox.setText("");

        EditText weightTextbox = (EditText) mRootView.findViewById(R.id.weight);
        weightTextbox.setText("");

        EditText numCimsTextbox = (EditText) mRootView.findViewById(R.id.num_cims);
        numCimsTextbox.setText("");

        EditText maxSpeedTextbox = (EditText) mRootView.findViewById(R.id.max_speed);
        maxSpeedTextbox.setText("");

        EditText wheelTypeTextbox = (EditText) mRootView.findViewById(R.id.wheel_type);
        wheelTypeTextbox.setText("");

        EditText wheelLayoutTextbox = (EditText) mRootView.findViewById(R.id.wheel_layout);
        wheelLayoutTextbox.setText("");

        EditText maxFuelTextbox = (EditText) mRootView.findViewById(R.id.max_fuel);
        maxFuelTextbox.setText("");

        EditText shootSpeedTextbox = (EditText) mRootView.findViewById(R.id.shoot_speed);
        shootSpeedTextbox.setText("");

        EditText shootLocationTextbox = (EditText) mRootView.findViewById(R.id.shoot_location);
        shootLocationTextbox.setText("");

        ToggleButton canShootHighButton = (ToggleButton) mRootView.findViewById(R.id.can_shoot_high);
        canShootHighButton.setChecked(false);

        ToggleButton canShootLowButton = (ToggleButton) mRootView.findViewById(R.id.can_shoot_low);
        canShootLowButton.setChecked(false);

        ToggleButton floorPickupButton = (ToggleButton) mRootView.findViewById(R.id.floor_pickup);
        floorPickupButton.setChecked(false);

        ToggleButton topLoaderButton = (ToggleButton) mRootView.findViewById(R.id.top_loader);
        topLoaderButton.setChecked(false);

        ToggleButton autoAimButton = (ToggleButton) mRootView.findViewById(R.id.auto_aim);
        autoAimButton.setChecked(false);

        ToggleButton canCarryGearButton = (ToggleButton) mRootView.findViewById(R.id.can_carry_gear);
        canCarryGearButton.setChecked(false);

        ToggleButton canClimbButton = (ToggleButton) mRootView.findViewById(R.id.can_climb);
        canClimbButton.setChecked(false);

        ToggleButton ownRopeButton = (ToggleButton) mRootView.findViewById(R.id.own_rope);
        ownRopeButton.setChecked(false);

        ToggleButton startLeftButton = (ToggleButton) mRootView.findViewById(R.id.start_left);
        startLeftButton.setChecked(false);

        ToggleButton startCenterButton = (ToggleButton) mRootView.findViewById(R.id.start_center);
        startCenterButton.setChecked(false);

        ToggleButton startRightButton = (ToggleButton) mRootView.findViewById(R.id.start_right);
        startRightButton.setChecked(false);

        EditText autoNumModesTextbox = (EditText) mRootView.findViewById(R.id.auto_num_modes);
        autoNumModesTextbox.setText("");

        ToggleButton autoBaseLineButton = (ToggleButton) mRootView.findViewById(R.id.auto_base_line);
        autoBaseLineButton.setChecked(false);

        ToggleButton autoPlaceGearButton = (ToggleButton) mRootView.findViewById(R.id.auto_place_gear);
        autoPlaceGearButton.setChecked(false);

        ToggleButton autoHighGoalButton = (ToggleButton) mRootView.findViewById(R.id.auto_high_goal);
        autoHighGoalButton.setChecked(false);

        ToggleButton autoLowGoalButton = (ToggleButton) mRootView.findViewById(R.id.auto_low_goal);
        autoLowGoalButton.setChecked(false);

        ToggleButton autoHopperButton = (ToggleButton) mRootView.findViewById(R.id.auto_hopper);
        autoHopperButton.setChecked(false);

        RatingBar teamRatingBar = (RatingBar) mRootView.findViewById(R.id.pit_team_rating);
        teamRatingBar.setRating(0);

        EditText commentsTextbox = (EditText) mRootView.findViewById(R.id.comments);
        commentsTextbox.setText("");

    }

}
