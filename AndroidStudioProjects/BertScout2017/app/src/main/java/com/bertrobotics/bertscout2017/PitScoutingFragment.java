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

    private boolean fillingPitInfo = false;

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

        final Button pitOKButton = (Button) mRootView.findViewById(R.id.stand_ok_btn);
        pitOKButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                currPitInfoIndex = -1;
                clearPitScreen();

                int teamNumber;
                try {
                    TextView teamText = (TextView) mRootView.findViewById(R.id.pit_team_number);
                    teamNumber = Integer.parseInt(teamText.getText().toString());
                } catch (Exception e) {
                    return;
                }

                currPitInfoArray = dbHelper.getDataAllPit();

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
//                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_EVENT, currEvent);
                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TEAM, teamNumber);
//                        currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_SCOUT_NAME, "");

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

        });

        final EditText pit_team_years_Textbox = (EditText) mRootView.findViewById(R.id.pit_team_years);
        pit_team_years_Textbox.addTextChangedListener(new TextWatcher() {
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
                        int tempValue = Integer.parseInt(pit_team_years_Textbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_YEARS) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TEAM_YEARS, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button pit_team_years_MinusButton = (Button) mRootView.findViewById(R.id.pit_team_years_minus_btn);
        pit_team_years_MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_team_years_Text = (TextView) mRootView.findViewById(R.id.pit_team_years);
                pit_team_years_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_team_years_Text.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        pit_team_years_Text.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_team_years_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        Button pit_team_years_PlusButton = (Button) mRootView.findViewById(R.id.pit_team_years_plus_btn);
        pit_team_years_PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_team_years_Text = (TextView) mRootView.findViewById(R.id.pit_team_years);
                pit_team_years_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_team_years_Text.getText().toString());
                    tempValue++;
                    pit_team_years_Text.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_team_years_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        final EditText pit_team_members_Textbox = (EditText) mRootView.findViewById(R.id.pit_team_members);
        pit_team_members_Textbox.addTextChangedListener(new TextWatcher() {
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
                        int tempValue = Integer.parseInt(pit_team_members_Textbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_MEMBERS) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TEAM_MEMBERS, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button pit_team_members_MinusButton = (Button) mRootView.findViewById(R.id.pit_team_members_minus_btn);
        pit_team_members_MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView teamMemberText = (TextView) mRootView.findViewById(R.id.pit_team_members);
                teamMemberText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(teamMemberText.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        teamMemberText.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                    int tempValue = 0;
                    teamMemberText.setText(Integer.toString(tempValue));
                }
            }
        });

        Button pit_team_members_PlusButton = (Button) mRootView.findViewById(R.id.pit_team_members_plus_btn);
        pit_team_members_PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_team_members_Text = (TextView) mRootView.findViewById(R.id.pit_team_members);
                pit_team_members_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_team_members_Text.getText().toString());
                    tempValue++;
                    pit_team_members_Text.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_team_members_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        final EditText pit_height_Textbox = (EditText) mRootView.findViewById(R.id.pit_height);
        pit_height_Textbox.addTextChangedListener(new TextWatcher() {
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
                        int tempValue = Integer.parseInt(pit_height_Textbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_HEIGHT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_HEIGHT, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button pit_height_MinusButton = (Button) mRootView.findViewById(R.id.pit_height_minus_btn);
        pit_height_MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView heightText = (TextView) mRootView.findViewById(R.id.pit_height);
                heightText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(heightText.getText().toString());
                    if (tempValue >= 4) {
                        tempValue = 24;
                        heightText.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                    int tempValue = 0;
                    heightText.setText(Integer.toString(tempValue));
                }
            }
        });

        Button heightPlusButton = (Button) mRootView.findViewById(R.id.pit_height_plus_btn);
        heightPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView heightText = (TextView) mRootView.findViewById(R.id.pit_height);
                heightText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(heightText.getText().toString());
                    tempValue = 36;
                    heightText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    int tempValue = 0;
                    heightText.setText(Integer.toString(tempValue));
                }
            }
        });

        final EditText pit_weight_Textbox = (EditText) mRootView.findViewById(R.id.pit_weight);
        pit_weight_Textbox.addTextChangedListener(new TextWatcher() {
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
                        int tempValue = Integer.parseInt(pit_weight_Textbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_WEIGHT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_WEIGHT, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button pit_weight_MinusButton = (Button) mRootView.findViewById(R.id.pit_weight_minus_btn);
        pit_weight_MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView weightText = (TextView) mRootView.findViewById(R.id.pit_weight);
                weightText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(weightText.getText().toString());
                    if (tempValue >= 5) {
                        tempValue = tempValue - 5;
                    } else {
                        tempValue = 0;
                    }
                    weightText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    int tempValue = 0;
                    weightText.setText(Integer.toString(tempValue));
                }
            }
        });

        Button pit_weight_PlusButton = (Button) mRootView.findViewById(R.id.pit_weight_plus_btn);
        pit_weight_PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView weightText = (TextView) mRootView.findViewById(R.id.pit_weight);
                weightText.requestFocus();
                try {
                    int tempValue = Integer.parseInt(weightText.getText().toString());
                    if (tempValue <= 115) {
                        tempValue = tempValue + 5;
                    } else {
                        tempValue = 120;
                    }
                    weightText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    int tempValue = 120;
                    weightText.setText(Integer.toString(tempValue));
                }
            }
        });

        final EditText pit_num_cims_Textbox = (EditText) mRootView.findViewById(R.id.pit_num_cims);
        pit_num_cims_Textbox.addTextChangedListener(new TextWatcher() {
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
                        int tempValue = Integer.parseInt(pit_num_cims_Textbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_NUM_CIMS) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_NUM_CIMS, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        final Button pit_num_cims_MinusButton = (Button) mRootView.findViewById(R.id.pit_num_cims_minus_btn);
        pit_num_cims_MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_num_cims_Text = (TextView) mRootView.findViewById(R.id.pit_num_cims);
                pit_num_cims_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_num_cims_Text.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        pit_num_cims_Text.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_num_cims_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        Button pit_num_cims_PlusButton = (Button) mRootView.findViewById(R.id.pit_num_cims_plus_btn);
        pit_num_cims_PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_num_cims_Text = (TextView) mRootView.findViewById(R.id.pit_num_cims);
                pit_num_cims_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_num_cims_Text.getText().toString());
                    tempValue++;
                    pit_num_cims_Text.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_num_cims_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        final EditText pit_max_speed_Textbox = (EditText) mRootView.findViewById(R.id.pit_max_speed);
        pit_max_speed_Textbox.addTextChangedListener(new TextWatcher() {
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
                        int tempValue = Integer.parseInt(pit_max_speed_Textbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_MAX_SPEED) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_MAX_SPEED, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button pit_max_speed_MinusButton = (Button) mRootView.findViewById(R.id.pit_max_speed_minus_btn);
        pit_max_speed_MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_max_speed_Text = (TextView) mRootView.findViewById(R.id.pit_max_speed);
                pit_max_speed_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_max_speed_Text.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        pit_max_speed_Text.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_max_speed_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        Button pit_max_speed_PlusButton = (Button) mRootView.findViewById(R.id.pit_max_speed_plus_btn);
        pit_max_speed_PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_max_speed_Text = (TextView) mRootView.findViewById(R.id.pit_max_speed);
                pit_max_speed_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_max_speed_Text.getText().toString());
                    tempValue++;
                    pit_max_speed_Text.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_max_speed_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        final EditText pit_wheel_type_Textbox = (EditText) mRootView.findViewById(R.id.pit_wheel_type);
        pit_wheel_type_Textbox.addTextChangedListener(new TextWatcher() {
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
                        String tempValue = pit_wheel_type_Textbox.getText().toString();
                        if (currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_TYPE) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_TYPE, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final EditText pit_wheel_layout_Textbox = (EditText) mRootView.findViewById(R.id.pit_wheel_layout);
        pit_wheel_layout_Textbox.addTextChangedListener(new TextWatcher() {
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
                        String tempValue = pit_wheel_layout_Textbox.getText().toString();
                        if (currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_LAYOUT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_LAYOUT, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final EditText pit_max_fuel_Textbox = (EditText) mRootView.findViewById(R.id.pit_max_fuel);
        pit_max_fuel_Textbox.addTextChangedListener(new TextWatcher() {
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
                        int tempValue = Integer.parseInt(pit_max_fuel_Textbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_MAX_FUEL) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_MAX_FUEL, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button pit_max_fuel_MinusButton = (Button) mRootView.findViewById(R.id.pit_max_fuel_minus_btn);
        pit_max_fuel_MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_max_fuel_Text = (TextView) mRootView.findViewById(R.id.pit_max_fuel);
                pit_max_fuel_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_max_fuel_Text.getText().toString());
                    if (tempValue >= 5) {
                        tempValue = tempValue - 5;
                        pit_max_fuel_Text.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_max_fuel_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        Button pit_max_fuel_PlusButton = (Button) mRootView.findViewById(R.id.pit_max_fuel_plus_btn);
        pit_max_fuel_PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_max_fuel_Text = (TextView) mRootView.findViewById(R.id.pit_max_fuel);
                pit_max_fuel_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_max_fuel_Text.getText().toString());
                    tempValue = tempValue + 5;
                    pit_max_fuel_Text.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_max_fuel_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        final EditText pit_shoot_speed_Textbox = (EditText) mRootView.findViewById(R.id.pit_shoot_speed);
        pit_shoot_speed_Textbox.addTextChangedListener(new TextWatcher() {
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
                        int tempValue = Integer.parseInt(pit_shoot_speed_Textbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_SPEED) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_SPEED, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button pit_shoot_speed_MinusButton = (Button) mRootView.findViewById(R.id.pit_shoot_speed_minus_btn);
        pit_shoot_speed_MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_shoot_speed_Text = (TextView) mRootView.findViewById(R.id.pit_shoot_speed);
                pit_shoot_speed_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_shoot_speed_Text.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        pit_shoot_speed_Text.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_shoot_speed_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        Button pit_shoot_speed_PlusButton = (Button) mRootView.findViewById(R.id.pit_shoot_speed_plus_btn);
        pit_shoot_speed_PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_shoot_speed_Text = (TextView) mRootView.findViewById(R.id.pit_shoot_speed);
                pit_shoot_speed_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_shoot_speed_Text.getText().toString());
                    tempValue++;
                    pit_shoot_speed_Text.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_shoot_speed_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        final EditText pit_shoot_location_Textbox = (EditText) mRootView.findViewById(R.id.pit_shoot_location);
        pit_shoot_location_Textbox.addTextChangedListener(new TextWatcher() {
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
                        String tempValue = pit_shoot_location_Textbox.getText().toString();
                        if (currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_LOCATION) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_LOCATION, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_can_shoot_high_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_shoot_high);
        pit_can_shoot_high_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_can_shoot_high_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_HIGH) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_HIGH, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_can_shoot_low_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_shoot_low);
        pit_can_shoot_low_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_can_shoot_low_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_LOW) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_LOW, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_floor_pickup_Button = (ToggleButton) mRootView.findViewById(R.id.pit_floor_pickup);
        pit_floor_pickup_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_floor_pickup_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_FLOOR_PICKUP) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_FLOOR_PICKUP, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_top_loader_Button = (ToggleButton) mRootView.findViewById(R.id.pit_top_loader);
        pit_top_loader_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_top_loader_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_TOP_LOADER) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TOP_LOADER, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_auto_aim_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_aim);
        pit_auto_aim_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_auto_aim_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_AIM) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_AIM, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_can_carry_gear_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_carry_gear);
        pit_can_carry_gear_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_can_carry_gear_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_CARRY_GEAR) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_CARRY_GEAR, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_can_climb_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_climb);
        pit_can_climb_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_can_climb_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_CLIMB) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_CAN_CLIMB, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_own_rope_Button = (ToggleButton) mRootView.findViewById(R.id.pit_own_rope);
        pit_own_rope_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_own_rope_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_OWN_ROPE) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_OWN_ROPE, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_start_left_Button = (ToggleButton) mRootView.findViewById(R.id.pit_start_left);
        pit_start_left_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_start_left_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_LEFT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_START_LEFT, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_start_center_Button = (ToggleButton) mRootView.findViewById(R.id.pit_start_center);
        pit_start_center_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_start_center_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_CENTER) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_START_CENTER, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_start_right_Button = (ToggleButton) mRootView.findViewById(R.id.pit_start_right);
        pit_start_right_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_start_right_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_RIGHT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_START_RIGHT, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final EditText pit_auto_num_modes_Textbox = (EditText) mRootView.findViewById(R.id.pit_auto_num_modes);
        pit_auto_num_modes_Textbox.addTextChangedListener(new TextWatcher() {
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
                        int tempValue = Integer.parseInt(pit_auto_num_modes_Textbox.getText().toString());
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_AUTO_NUM_MODES) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_NUM_MODES, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        });

        Button pit_auto_num_modes_MinusButton = (Button) mRootView.findViewById(R.id.pit_auto_num_modes_minus_btn);
        pit_auto_num_modes_MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_auto_num_modes_Text = (TextView) mRootView.findViewById(R.id.pit_auto_num_modes);
                pit_auto_num_modes_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_auto_num_modes_Text.getText().toString());
                    if (tempValue > 0) {
                        tempValue--;
                        pit_auto_num_modes_Text.setText(Integer.toString(tempValue));
                    }
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_auto_num_modes_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        Button pit_auto_num_modes_PlusButton = (Button) mRootView.findViewById(R.id.pit_auto_num_modes_plus_btn);
        pit_auto_num_modes_PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pit_auto_num_modes_Text = (TextView) mRootView.findViewById(R.id.pit_auto_num_modes);
                pit_auto_num_modes_Text.requestFocus();
                try {
                    int tempValue = Integer.parseInt(pit_auto_num_modes_Text.getText().toString());
                    tempValue++;
                    pit_auto_num_modes_Text.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    int tempValue = 0;
                    pit_auto_num_modes_Text.setText(Integer.toString(tempValue));
                }
            }
        });

        final ToggleButton pit_auto_base_line_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_base_line);
        pit_auto_base_line_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_auto_base_line_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_BASE_LINE) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_BASE_LINE, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_auto_place_gear_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_place_gear);
        pit_auto_place_gear_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_auto_place_gear_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_PLACE_GEAR) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_PLACE_GEAR, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_auto_high_goal_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_high_goal);
        pit_auto_high_goal_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_auto_high_goal_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HIGH_GOAL) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HIGH_GOAL, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_auto_low_goal_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_low_goal);
        pit_auto_low_goal_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_auto_low_goal_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_LOW_GOAL) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_LOW_GOAL, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton pit_auto_hopper_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_hopper);
        pit_auto_hopper_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currPitInfoIndex >= 0) {
                    try {
                        boolean tempValue = pit_auto_hopper_Button.isChecked();
                        if (currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HOPPER) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HOPPER, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final RatingBar pit_team_rating_Bar = (RatingBar) mRootView.findViewById(R.id.pit_team_rating);
        pit_team_rating_Bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar pit_team_rating_Bar, float rating, boolean fromUser) {
                if (currPitInfoIndex >= 0) {
                    try {
                        int tempValue = (int) pit_team_rating_Bar.getRating();
                        if (currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_RATING) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_TEAM_RATING, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final EditText pit_comments_Textbox = (EditText) mRootView.findViewById(R.id.pit_comments);
        pit_comments_Textbox.addTextChangedListener(new TextWatcher() {
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
                        String tempValue = pit_comments_Textbox.getText().toString();
                        if (currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_COMMENT) != tempValue) {
                            currTeam.put(DBContract.TablePitInfo.COLNAME_PIT_COMMENT, tempValue);
                            if (!fillingPitInfo) {
                                dbHelper.updatePitInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        buildPitTeamSpinner("north_shore");

        return mRootView;
    }

    public void buildPitTeamSpinner(String event) {

//        currEvent = event;
//        Integer teamList = null;
//
//        Spinner teamSpinner = (Spinner) mRootView.findViewById(R.id.pit_team_spinner);
//
//        if (event.equals("north_shore")) {
//            teamList = R.array.north_shore_teams;
//        } else if (event.equals("pine_tree")) {
//            teamList = R.array.pine_tree_teams;
//        }
//
//        if (teamList != null) {
//            currPitInfoArray = dbHelper.getDataAllPit();
//            ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(mRootView.getContext(),
//                    teamList, R.layout.spinner_item);
//            dataAdapter.setDropDownViewResource(R.layout.spinner_item);
//            teamSpinner.setAdapter(dataAdapter);
//        } else {
//            currPitInfoArray = new JSONArray();
//            teamSpinner.setAdapter(null);
//        }

    }

    private void showPitInfo() {

        try {

            fillingPitInfo = true;

            EditText pit_team_years_Textbox = (EditText) mRootView.findViewById(R.id.pit_team_years);
            pit_team_years_Textbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_YEARS)));

            EditText pit_team_members_Textbox = (EditText) mRootView.findViewById(R.id.pit_team_members);
            pit_team_members_Textbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_MEMBERS)));

            EditText pit_height_Textbox = (EditText) mRootView.findViewById(R.id.pit_height);
            pit_height_Textbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_HEIGHT)));

            EditText pit_weight_Textbox = (EditText) mRootView.findViewById(R.id.pit_weight);
            pit_weight_Textbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_WEIGHT)));

            EditText pit_num_cims_Textbox = (EditText) mRootView.findViewById(R.id.pit_num_cims);
            pit_num_cims_Textbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_NUM_CIMS)));

            EditText pit_max_speed_Textbox = (EditText) mRootView.findViewById(R.id.pit_max_speed);
            pit_max_speed_Textbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_MAX_SPEED)));

            EditText pit_wheel_type_Textbox = (EditText) mRootView.findViewById(R.id.pit_wheel_type);
            pit_wheel_type_Textbox.setText(currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_TYPE));

            EditText pit_wheel_layout_Textbox = (EditText) mRootView.findViewById(R.id.pit_wheel_layout);
            pit_wheel_layout_Textbox.setText(currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_WHEEL_LAYOUT));

            EditText pit_max_fuel_Textbox = (EditText) mRootView.findViewById(R.id.pit_max_fuel);
            pit_max_fuel_Textbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_MAX_FUEL)));

            EditText pit_shoot_speed_Textbox = (EditText) mRootView.findViewById(R.id.pit_shoot_speed);
            pit_shoot_speed_Textbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_SPEED)));

            EditText pit_shoot_location_Textbox = (EditText) mRootView.findViewById(R.id.pit_shoot_location);
            pit_shoot_location_Textbox.setText(currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_SHOOT_LOCATION));

            ToggleButton pit_can_shoot_high_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_shoot_high);
            pit_can_shoot_high_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_HIGH));

            ToggleButton pit_can_shoot_low_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_shoot_low);
            pit_can_shoot_low_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_LOW));

            ToggleButton pit_floor_pickup_Button = (ToggleButton) mRootView.findViewById(R.id.pit_floor_pickup);
            pit_floor_pickup_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_FLOOR_PICKUP));

            ToggleButton pit_top_loader_Button = (ToggleButton) mRootView.findViewById(R.id.pit_top_loader);
            pit_top_loader_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_TOP_LOADER));

            ToggleButton pit_auto_aim_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_aim);
            pit_auto_aim_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_AIM));

            ToggleButton pit_can_carry_gear_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_carry_gear);
            pit_can_carry_gear_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_CARRY_GEAR));

            ToggleButton pit_can_climb_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_climb);
            pit_can_climb_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_CAN_CLIMB));

            ToggleButton pit_own_rope_Button = (ToggleButton) mRootView.findViewById(R.id.pit_own_rope);
            pit_own_rope_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_OWN_ROPE));

            ToggleButton pit_start_left_Button = (ToggleButton) mRootView.findViewById(R.id.pit_start_left);
            pit_start_left_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_LEFT));

            ToggleButton pit_start_center_Button = (ToggleButton) mRootView.findViewById(R.id.pit_start_center);
            pit_start_center_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_CENTER));

            ToggleButton pit_start_right_Button = (ToggleButton) mRootView.findViewById(R.id.pit_start_right);
            pit_start_right_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_START_RIGHT));

            EditText pit_auto_num_modes_Textbox = (EditText) mRootView.findViewById(R.id.pit_auto_num_modes);
            pit_auto_num_modes_Textbox.setText(Integer.toString(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_AUTO_NUM_MODES)));

            ToggleButton pit_auto_base_line_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_base_line);
            pit_auto_base_line_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_BASE_LINE));

            ToggleButton pit_auto_place_gear_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_place_gear);
            pit_auto_place_gear_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_PLACE_GEAR));

            ToggleButton pit_auto_high_goal_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_high_goal);
            pit_auto_high_goal_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HIGH_GOAL));

            ToggleButton pit_auto_low_goal_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_low_goal);
            pit_auto_low_goal_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_LOW_GOAL));

            ToggleButton pit_auto_hopper_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_hopper);
            pit_auto_hopper_Button.setChecked(currTeam.getBoolean(DBContract.TablePitInfo.COLNAME_PIT_AUTO_HOPPER));

            RatingBar pit_team_rating_Bar = (RatingBar) mRootView.findViewById(R.id.pit_team_rating);
            pit_team_rating_Bar.setRating(currTeam.getInt(DBContract.TablePitInfo.COLNAME_PIT_TEAM_RATING));

            EditText pit_comments_Textbox = (EditText) mRootView.findViewById(R.id.pit_comments);
            pit_comments_Textbox.setText(currTeam.getString(DBContract.TablePitInfo.COLNAME_PIT_COMMENT));

            fillingPitInfo = false;

        } catch (JSONException e) {
            fillingPitInfo = false;
            //e.printStackTrace();
        }
    }

    public void clearPitScreen() {

        fillingPitInfo = true;

        EditText pit_team_years_Textbox = (EditText) mRootView.findViewById(R.id.pit_team_years);
        pit_team_years_Textbox.setText("");

        EditText pit_team_members_Textbox = (EditText) mRootView.findViewById(R.id.pit_team_members);
        pit_team_members_Textbox.setText("");

        EditText pit_height_Textbox = (EditText) mRootView.findViewById(R.id.pit_height);
        pit_height_Textbox.setText("");

        EditText pit_weight_Textbox = (EditText) mRootView.findViewById(R.id.pit_weight);
        pit_weight_Textbox.setText("");

        EditText pit_num_cims_Textbox = (EditText) mRootView.findViewById(R.id.pit_num_cims);
        pit_num_cims_Textbox.setText("");

        EditText pit_max_speed_Textbox = (EditText) mRootView.findViewById(R.id.pit_max_speed);
        pit_max_speed_Textbox.setText("");

        EditText pit_wheel_type_Textbox = (EditText) mRootView.findViewById(R.id.pit_wheel_type);
        pit_wheel_type_Textbox.setText("");

        EditText pit_wheel_layout_Textbox = (EditText) mRootView.findViewById(R.id.pit_wheel_layout);
        pit_wheel_layout_Textbox.setText("");

        EditText pit_max_fuel_Textbox = (EditText) mRootView.findViewById(R.id.pit_max_fuel);
        pit_max_fuel_Textbox.setText("");

        EditText pit_shoot_speed_Textbox = (EditText) mRootView.findViewById(R.id.pit_shoot_speed);
        pit_shoot_speed_Textbox.setText("");

        EditText pit_shoot_location_Textbox = (EditText) mRootView.findViewById(R.id.pit_shoot_location);
        pit_shoot_location_Textbox.setText("");

        ToggleButton pit_can_shoot_high_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_shoot_high);
        pit_can_shoot_high_Button.setChecked(false);

        ToggleButton pit_can_shoot_low_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_shoot_low);
        pit_can_shoot_low_Button.setChecked(false);

        ToggleButton pit_floor_pickup_Button = (ToggleButton) mRootView.findViewById(R.id.pit_floor_pickup);
        pit_floor_pickup_Button.setChecked(false);

        ToggleButton pit_top_loader_Button = (ToggleButton) mRootView.findViewById(R.id.pit_top_loader);
        pit_top_loader_Button.setChecked(false);

        ToggleButton pit_auto_aim_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_aim);
        pit_auto_aim_Button.setChecked(false);

        ToggleButton pit_can_carry_gear_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_carry_gear);
        pit_can_carry_gear_Button.setChecked(false);

        ToggleButton pit_can_climb_Button = (ToggleButton) mRootView.findViewById(R.id.pit_can_climb);
        pit_can_climb_Button.setChecked(false);

        ToggleButton pit_own_rope_Button = (ToggleButton) mRootView.findViewById(R.id.pit_own_rope);
        pit_own_rope_Button.setChecked(false);

        ToggleButton pit_start_left_Button = (ToggleButton) mRootView.findViewById(R.id.pit_start_left);
        pit_start_left_Button.setChecked(false);

        ToggleButton pit_start_center_Button = (ToggleButton) mRootView.findViewById(R.id.pit_start_center);
        pit_start_center_Button.setChecked(false);

        ToggleButton pit_start_right_Button = (ToggleButton) mRootView.findViewById(R.id.pit_start_right);
        pit_start_right_Button.setChecked(false);

        EditText pit_auto_num_modes_Textbox = (EditText) mRootView.findViewById(R.id.pit_auto_num_modes);
        pit_auto_num_modes_Textbox.setText("");

        ToggleButton pit_auto_base_line_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_base_line);
        pit_auto_base_line_Button.setChecked(false);

        ToggleButton pit_auto_place_gear_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_place_gear);
        pit_auto_place_gear_Button.setChecked(false);

        ToggleButton pit_auto_high_goal_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_high_goal);
        pit_auto_high_goal_Button.setChecked(false);

        ToggleButton pit_auto_low_goal_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_low_goal);
        pit_auto_low_goal_Button.setChecked(false);

        ToggleButton pit_auto_hopper_Button = (ToggleButton) mRootView.findViewById(R.id.pit_auto_hopper);
        pit_auto_hopper_Button.setChecked(false);

        RatingBar pit_team_rating_Bar = (RatingBar) mRootView.findViewById(R.id.pit_team_rating);
        pit_team_rating_Bar.setRating(0);

        EditText pit_comments_Textbox = (EditText) mRootView.findViewById(R.id.pit_comments);
        pit_comments_Textbox.setText("");

        fillingPitInfo = false;

    }

}
