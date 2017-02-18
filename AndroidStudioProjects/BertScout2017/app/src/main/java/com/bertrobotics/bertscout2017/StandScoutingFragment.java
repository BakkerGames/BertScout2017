package com.bertrobotics.bertscout2017;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StandScoutingFragment extends Fragment {

    public View mRootView;
    public DBHelper dbHelper;

    public String currEvent;
    public JSONArray currStandInfoArray;
    public int currStandInfoIndex = -1;
    public JSONObject currTeam;

    private boolean fillingStandInfo = false;

    public StandScoutingFragment() {
        // default constructor
    }

//    public StandScoutingFragment(StatisticsFragment statisticsFragment) {
//        mStatisticsFragment = statisticsFragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.stand_scouting, container, false);
        dbHelper = new DBHelper(mRootView.getContext());

        final Button standOKButton = (Button) mRootView.findViewById(R.id.stand_ok_btn);
        standOKButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (standOKButton.getText().equals("") ||
                        standOKButton.getText().equals("BLUE")) {
                    if (currStandInfoIndex >= 0) {
                        standOKButton.setText("RED");
                        try {
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_ALLIANCE, "RED");
                        } catch (JSONException e) {
                        }
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    }
                    return;
                }
                if (standOKButton.getText().equals("RED")) {
                    if (currStandInfoIndex >= 0) {
                        standOKButton.setText("BLUE");
                        try {
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_ALLIANCE, "BLUE");
                        } catch (JSONException e) {
                        }
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    }
                    return;
                }

                if (standOKButton.getText().equals("LOAD")) {

                    currStandInfoIndex = -1;

                    int currMatch;
                    String tempMatchString;
                    int teamNumber;
                    try {
                        TextView matchText = (TextView) mRootView.findViewById(R.id.stand_match_number);
                        tempMatchString = matchText.getText().toString();
                        currMatch = Integer.parseInt(tempMatchString);
                        TextView teamText = (TextView) mRootView.findViewById(R.id.stand_team_number);
                        teamNumber = Integer.parseInt(teamText.getText().toString());
                        if (currMatch < 1 || teamNumber < 1) {
                            return; // no match and/or team yet
                        }
                    } catch (Exception e) {
                        clearStandScreen();
                        standOKButton.setText("LOAD");
                        return; // no match and/or team yet
                    }

                    currStandInfoArray = dbHelper.getDataAllStand();

                    for (int i = 0; i < currStandInfoArray.length(); i++) {
                        try {
                            currTeam = currStandInfoArray.getJSONObject(i);
                            if (currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_MATCH) == currMatch &&
                                    currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TEAM) == teamNumber) {
                                currStandInfoIndex = i;
                                break;
                            }
                        } catch (JSONException e) {
                            currTeam = null;
                        }
                    }

                    if (currStandInfoIndex < 0) {
                        currTeam = new JSONObject();
                        try {
//                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_EVENT, currEvent);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_MATCH, currMatch);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TEAM, teamNumber);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_ALLIANCE, "RED");
//                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_SCOUT_NAME, "");

                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_HIGH, 0);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_LOW, 0);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_BASE_LINE, false);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_PLACE_GEAR, false);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_OPEN_HOPPER, false);

                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_HIGH, 0);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_LOW, 0);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_RECEIVED, 0);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_PLACED, 0);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_PENALTIES, 0);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_CLIMBED, false);
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_TOUCHPAD, false);

                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_COMMENT, "");

                            currStandInfoArray.put(currTeam);
                            currStandInfoIndex = currStandInfoArray.length() - 1;
                        } catch (JSONException e) {
                            currTeam = null;
                        }
                    }

                    standOKButton.setText("");
                    showStandInfo();

                }
            }

        });

        Button matchMinusButton = (Button) mRootView.findViewById(R.id.stand_match_minus_btn);
        matchMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView matchText = (TextView) mRootView.findViewById(R.id.stand_match_number);
                matchText.requestFocus();
                int tempValue;
                try {
                    tempValue = Integer.parseInt(matchText.getText().toString());
                    if (tempValue <= 1) {
                        return;
                    }
                    tempValue--;
                    matchText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    tempValue = 1;
                    matchText.setText(Integer.toString(tempValue));
                }
                TextView teamText = (TextView) mRootView.findViewById(R.id.stand_team_number);
                teamText.setText("");
                Button standOKButton = (Button) mRootView.findViewById(R.id.stand_ok_btn);
                clearStandScreen();
                standOKButton.setText("LOAD");
            }
        });

        Button matchPlusButton = (Button) mRootView.findViewById(R.id.stand_match_plus_btn);
        matchPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView matchText = (TextView) mRootView.findViewById(R.id.stand_match_number);
                matchText.requestFocus();
                int tempValue;
                try {
                    tempValue = Integer.parseInt(matchText.getText().toString());
                    tempValue++;
                    matchText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    tempValue = 1;
                    matchText.setText(Integer.toString(tempValue));
                }
                TextView teamText = (TextView) mRootView.findViewById(R.id.stand_team_number);
                teamText.setText("");
                Button standOKButton = (Button) mRootView.findViewById(R.id.stand_ok_btn);
                clearStandScreen();
                standOKButton.setText("LOAD");
            }
        });

        final EditText stand_team_number_Textbox = (EditText) mRootView.findViewById(R.id.stand_team_number);
        stand_team_number_Textbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Button stand_ok_Button = (Button) mRootView.findViewById(R.id.stand_ok_btn);
                stand_ok_Button.setText("LOAD");
            }
        });

        Button score_highMinusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_high_minus_btn);
        score_highMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView score_highText = (TextView) mRootView.findViewById(R.id.stand_auto_score_high_number);
                    score_highText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(score_highText.getText().toString());
                        if (tempValue <= 0) {
                            return;
                        }
                        tempValue--;
                        score_highText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        score_highText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_HIGH, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button score_highPlusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_high_plus_btn);
        score_highPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView score_highText = (TextView) mRootView.findViewById(R.id.stand_auto_score_high_number);
                    score_highText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(score_highText.getText().toString());
                        tempValue++;
                        score_highText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        score_highText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_HIGH, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button score_lowMinusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_low_minus_btn);
        score_lowMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView score_lowText = (TextView) mRootView.findViewById(R.id.stand_auto_score_low_number);
                    score_lowText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(score_lowText.getText().toString());
                        if (tempValue <= 0) {
                            return;
                        }
                        tempValue--;
                        score_lowText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        score_lowText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_LOW, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button score_lowPlusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_low_plus_btn);
        score_lowPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView score_lowText = (TextView) mRootView.findViewById(R.id.stand_auto_score_low_number);
                    score_lowText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(score_lowText.getText().toString());
                        tempValue++;
                        score_lowText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        score_lowText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_LOW, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton stand_auto_cross_baseline_Button = (ToggleButton) mRootView.findViewById(R.id.stand_auto_cross_baseline_toggle);
        stand_auto_cross_baseline_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currStandInfoIndex >= 0) {
                    try {
                        boolean tempValue = stand_auto_cross_baseline_Button.isChecked();
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_BASE_LINE, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton stand_auto_place_gear_Button = (ToggleButton) mRootView.findViewById(R.id.stand_auto_place_gear_toggle);
        stand_auto_place_gear_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currStandInfoIndex >= 0) {
                    try {
                        boolean tempValue = stand_auto_place_gear_Button.isChecked();
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_PLACE_GEAR, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton stand_auto_open_hopper_Button = (ToggleButton) mRootView.findViewById(R.id.stand_auto_open_hopper_toggle);
        stand_auto_open_hopper_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currStandInfoIndex >= 0) {
                    try {
                        boolean tempValue = stand_auto_open_hopper_Button.isChecked();
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_OPEN_HOPPER, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button teleop_score_highMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_high_minus_btn);
        teleop_score_highMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView score_highText = (TextView) mRootView.findViewById(R.id.stand_teleop_score_high_number);
                    score_highText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(score_highText.getText().toString());
                        if (tempValue <= 0) {
                            return;
                        }
                        tempValue--;
                        score_highText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        score_highText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_HIGH, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button teleop_score_highPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_high_plus_btn);
        teleop_score_highPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView score_highText = (TextView) mRootView.findViewById(R.id.stand_teleop_score_high_number);
                    score_highText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(score_highText.getText().toString());
                        tempValue++;
                        score_highText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        score_highText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_HIGH, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button teleop_score_lowMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_low_minus_btn);
        teleop_score_lowMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView score_lowText = (TextView) mRootView.findViewById(R.id.stand_teleop_score_low_number);
                score_lowText.requestFocus();
                int tempValue;
                try {
                    tempValue = Integer.parseInt(score_lowText.getText().toString());
                    if (tempValue <= 0) {
                        return;
                    }
                    tempValue--;
                    score_lowText.setText(Integer.toString(tempValue));
                } catch (Exception e) {
                    tempValue = 0;
                    score_lowText.setText(Integer.toString(tempValue));
                }
                try {
                    currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_LOW, tempValue);
                    if (!fillingStandInfo) {
                        dbHelper.updateStandInfo(currTeam);
                    }
                } catch (JSONException e) {
                }
            }
        });

        Button teleop_score_lowPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_low_plus_btn);
        teleop_score_lowPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView score_lowText = (TextView) mRootView.findViewById(R.id.stand_teleop_score_low_number);
                    score_lowText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(score_lowText.getText().toString());
                        tempValue++;
                        score_lowText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        score_lowText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_LOW, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button teleop_gears_receviedMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_received_minus_btn);
        teleop_gears_receviedMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView gears_receivedText = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_received_number);
                    gears_receivedText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(gears_receivedText.getText().toString());
                        if (tempValue <= 0) {
                            return;
                        }
                        tempValue--;
                        gears_receivedText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        gears_receivedText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_RECEIVED, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button teleop_gears_receivedPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_received_plus_btn);
        teleop_gears_receivedPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView gears_receivedText = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_received_number);

                    gears_receivedText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(gears_receivedText.getText().toString());
                        tempValue++;
                        gears_receivedText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        gears_receivedText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_RECEIVED, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button teleop_gears_placedMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_placed_minus_btn);
        teleop_gears_placedMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView gears_placedText = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_placed_number);

                    gears_placedText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(gears_placedText.getText().toString());
                        if (tempValue <= 0) {
                            return;
                        }
                        tempValue--;
                        gears_placedText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        gears_placedText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_PLACED, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button teleop_gears_placedPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_placed_plus_btn);
        teleop_gears_placedPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView gears_placedText = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_placed_number);

                    gears_placedText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(gears_placedText.getText().toString());
                        tempValue++;
                        gears_placedText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        gears_placedText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_PLACED, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button penalties_incurredMinusButton = (Button) mRootView.findViewById(R.id.stand_penalties_incurred_minus_btn);
        penalties_incurredMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView penalties_incurredText = (TextView) mRootView.findViewById(R.id.stand_penalties_incurred_number);
                    penalties_incurredText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(penalties_incurredText.getText().toString());
                        if (tempValue <= 0) {
                            return;
                        }
                        tempValue--;
                        penalties_incurredText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        penalties_incurredText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_PENALTIES, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        Button penalties_incurredPlusButton = (Button) mRootView.findViewById(R.id.stand_penalties_incurred_plus_btn);
        penalties_incurredPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0) {
                    TextView penalties_incurredText = (TextView) mRootView.findViewById(R.id.stand_penalties_incurred_number);
                    penalties_incurredText.requestFocus();
                    int tempValue;
                    try {
                        tempValue = Integer.parseInt(penalties_incurredText.getText().toString());
                        tempValue++;
                        penalties_incurredText.setText(Integer.toString(tempValue));
                    } catch (Exception e) {
                        tempValue = 0;
                        penalties_incurredText.setText(Integer.toString(tempValue));
                    }
                    try {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_PENALTIES, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton stand_teleop_climbed_Button = (ToggleButton) mRootView.findViewById(R.id.stand_teleop_climb_toggle);
        stand_teleop_climbed_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currStandInfoIndex >= 0) {
                    try {
                        boolean tempValue = stand_teleop_climbed_Button.isChecked();
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_CLIMBED, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final ToggleButton stand_teleop_touchpad_Button = (ToggleButton) mRootView.findViewById(R.id.stand_teleop_touchpad_scored_toggle);
        stand_teleop_touchpad_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (currStandInfoIndex >= 0) {
                    try {
                        boolean tempValue = stand_teleop_touchpad_Button.isChecked();
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_TOUCHPAD, tempValue);
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });

        final EditText stand_comments_Textbox = (EditText) mRootView.findViewById(R.id.stand_comments_text);
        stand_comments_Textbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currStandInfoIndex >= 0) {
                    try {
                        String tempValue = stand_comments_Textbox.getText().toString();
                        if (currTeam.getString(DBContract.TableStandInfo.COLNAME_STAND_COMMENT) != tempValue) {
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_COMMENT, tempValue);
                            if (!fillingStandInfo) {
                                dbHelper.updateStandInfo(currTeam);
                            }
                        }
                    } catch (JSONException e) {
                    }
                } else {
                }
            }
        });

        buildStandTeamSpinner("north_shore");

        return mRootView;
    }

    public void buildStandTeamSpinner(String event) {

//        currEvent = event;
//        Integer teamList;
//
//        Spinner teamSpinner = (Spinner) mRootView.findViewById(R.id.stand_team_spinner);
//
//        if (event.equals("north_shore")) {
//            teamList = R.array.north_shore_teams;
//        } else {
//            teamList = R.array.pine_tree_teams;
//        }
//
//        if (teamList != null) {
//            currStandInfoArray = dbHelper.getDataAllStand(currEvent);
//            ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(mRootView.getContext(),
//                    teamList, R.layout.spinner_item);
//            dataAdapter.setDropDownViewResource(R.layout.spinner_item);
//            teamSpinner.setAdapter(dataAdapter);
//        } else {
//            currStandInfoArray = new JSONArray();
//            teamSpinner.setAdapter(null);
//        }
//
    }

    private void showStandInfo() {

        try {

            fillingStandInfo = true;

            Button stand_ok_Button = (Button) mRootView.findViewById(R.id.stand_ok_btn);
            stand_ok_Button.setText(currTeam.getString(DBContract.TableStandInfo.COLNAME_STAND_ALLIANCE));

            TextView auto_score_high_number_Textview = (TextView) mRootView.findViewById(R.id.stand_auto_score_high_number);
            auto_score_high_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_HIGH)));

            TextView auto_score_low_number_Textview = (TextView) mRootView.findViewById(R.id.stand_auto_score_low_number);
            auto_score_low_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_LOW)));

            ToggleButton auto_cross_baseline_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_cross_baseline_toggle);
            boolean auto_cross_baseline_flag = currTeam.getBoolean(DBContract.TableStandInfo.COLNAME_STAND_AUTO_BASE_LINE);
            auto_cross_baseline_toggle_ToggleButton.setChecked(auto_cross_baseline_flag);

            ToggleButton auto_place_gear_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_place_gear_toggle);
            boolean auto_place_gear_flag = currTeam.getBoolean(DBContract.TableStandInfo.COLNAME_STAND_AUTO_PLACE_GEAR);
            auto_place_gear_toggle_ToggleButton.setChecked(auto_place_gear_flag);

            ToggleButton auto_open_hopper_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_open_hopper_toggle);
            boolean auto_open_hopper_flag = currTeam.getBoolean(DBContract.TableStandInfo.COLNAME_STAND_AUTO_OPEN_HOPPER);
            auto_open_hopper_toggle_ToggleButton.setChecked(auto_open_hopper_flag);

            TextView teleop_score_high_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_score_high_number);
            teleop_score_high_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_HIGH)));

            TextView teleop_score_low_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_score_low_number);
            teleop_score_low_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_LOW)));

            TextView teleop_gears_received_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_received_number);
            teleop_gears_received_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_RECEIVED)));

            TextView teleop_gears_placed_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_placed_number);
            teleop_gears_placed_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_PLACED)));

            TextView penalties_incurred_number_Textview = (TextView) mRootView.findViewById(R.id.stand_penalties_incurred_number);
            penalties_incurred_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_PENALTIES)));

            ToggleButton teleop_climb_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_teleop_climb_toggle);
            boolean teleop_climb_flag = currTeam.getBoolean(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_CLIMBED);
            teleop_climb_toggle_ToggleButton.setChecked(teleop_climb_flag);

            ToggleButton teleop_touchpad_scored_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_teleop_touchpad_scored_toggle);
            boolean teleop_touchpad_scored_flag = currTeam.getBoolean(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_TOUCHPAD);
            teleop_touchpad_scored_toggle_ToggleButton.setChecked(teleop_touchpad_scored_flag);

            EditText comments_text_Textview = (EditText) mRootView.findViewById(R.id.stand_comments_text);
            String comments_flag = currTeam.getString(DBContract.TableStandInfo.COLNAME_STAND_COMMENT);
            comments_text_Textview.setText(comments_flag);

            fillingStandInfo = false;

        } catch (JSONException e) {
            fillingStandInfo = false;
            //e.printStackTrace();
        }
    }

    public void clearStandScreen() {

        fillingStandInfo = true;

        try {

            Button stand_ok_Button = (Button) mRootView.findViewById(R.id.stand_ok_btn);
            stand_ok_Button.setText("?");

            TextView auto_score_high_number_Textview = (TextView) mRootView.findViewById(R.id.stand_auto_score_high_number);
            auto_score_high_number_Textview.setText("0");

            TextView auto_score_low_number_Textview = (TextView) mRootView.findViewById(R.id.stand_auto_score_low_number);
            auto_score_low_number_Textview.setText("0");

            ToggleButton auto_cross_baseline_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_cross_baseline_toggle);
            auto_cross_baseline_toggle_ToggleButton.setChecked(false);

            ToggleButton auto_place_gear_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_place_gear_toggle);
            auto_place_gear_toggle_ToggleButton.setChecked(false);

            ToggleButton auto_open_hopper_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_open_hopper_toggle);
            auto_open_hopper_toggle_ToggleButton.setChecked(false);

            TextView teleop_score_high_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_score_high_number);
            teleop_score_high_number_Textview.setText("0");

            TextView teleop_score_low_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_score_low_number);
            teleop_score_low_number_Textview.setText("0");

            TextView teleop_gears_received_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_received_number);
            teleop_gears_received_number_Textview.setText("0");

            TextView teleop_gears_placed_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_placed_number);
            teleop_gears_placed_number_Textview.setText("0");

            TextView penalties_incurred_number_Textview = (TextView) mRootView.findViewById(R.id.stand_penalties_incurred_number);
            penalties_incurred_number_Textview.setText("0");

            ToggleButton teleop_climb_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_teleop_climb_toggle);
            teleop_climb_toggle_ToggleButton.setChecked(false);

            ToggleButton teleop_touchpad_scored_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_teleop_touchpad_scored_toggle);
            teleop_touchpad_scored_toggle_ToggleButton.setChecked(false);

            EditText stand_comments_text_Textview = (EditText) mRootView.findViewById(R.id.stand_comments_text);
            stand_comments_text_Textview.setText("");

        } catch (Exception e) {

            fillingStandInfo = false;

        }

        fillingStandInfo = false;

    }

}