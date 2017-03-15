package com.bertrobotics.bertscout2017;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
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
    public boolean lastAllianceBlue = true;
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
                        standOKButton.getText().toString().equalsIgnoreCase("Blue")) {
                    if (currStandInfoIndex >= 0) {
                        standOKButton.setText("Red");
                        lastAllianceBlue = false;
                        try {
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_ALLIANCE, "Red");
                        } catch (JSONException e) {
                        }
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    }
                    return;
                }
                if (standOKButton.getText().toString().equalsIgnoreCase("Red")) {
                    if (currStandInfoIndex >= 0) {
                        standOKButton.setText("Blue");
                        lastAllianceBlue = true;
                        try {
                            currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_ALLIANCE, "Blue");
                        } catch (JSONException e) {
                        }
                        if (!fillingStandInfo) {
                            dbHelper.updateStandInfo(currTeam);
                        }
                    }
                    return;
                }

                if (standOKButton.getText().toString().equalsIgnoreCase("LOAD")) {
                    loadScreen();

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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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

//        Button teleop_gears_receviedMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_received_minus_btn);
//        teleop_gears_receviedMinusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (currStandInfoIndex >= 0 && currTeam != null) {
//                    TextView gears_receivedText = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_received_number);
//                    gears_receivedText.requestFocus();
//                    int tempValue;
//                    try {
//                        tempValue = Integer.parseInt(gears_receivedText.getText().toString());
//                        if (tempValue <= 0) {
//                            return;
//                        }
//                        tempValue--;
//                        gears_receivedText.setText(Integer.toString(tempValue));
//                    } catch (Exception e) {
//                        tempValue = 0;
//                        gears_receivedText.setText(Integer.toString(tempValue));
//                    }
//                    try {
//                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_RECEIVED, tempValue);
//                        if (!fillingStandInfo) {
//                            dbHelper.updateStandInfo(currTeam);
//                        }
//                    } catch (JSONException e) {
//                    }
//                }
//            }
//        });
//
//        Button teleop_gears_receivedPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_received_plus_btn);
//        teleop_gears_receivedPlusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (currStandInfoIndex >= 0 && currTeam != null) {
//                    TextView gears_receivedText = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_received_number);
//
//                    gears_receivedText.requestFocus();
//                    int tempValue;
//                    try {
//                        tempValue = Integer.parseInt(gears_receivedText.getText().toString());
//                        tempValue++;
//                        gears_receivedText.setText(Integer.toString(tempValue));
//                    } catch (Exception e) {
//                        tempValue = 0;
//                        gears_receivedText.setText(Integer.toString(tempValue));
//                    }
//                    try {
//                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_RECEIVED, tempValue);
//                        if (!fillingStandInfo) {
//                            dbHelper.updateStandInfo(currTeam);
//                        }
//                    } catch (JSONException e) {
//                    }
//                }
//            }
//        });

        Button teleop_gears_placedMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_placed_minus_btn);
        teleop_gears_placedMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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
                if (currStandInfoIndex >= 0 && currTeam != null) {
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

        clearStandScreen();

        return mRootView;
    }

    public void loadScreen() {
        Button standOKButton = (Button) mRootView.findViewById(R.id.stand_ok_btn);

        currStandInfoIndex = -1;

        if (MainActivity.ScoutName.equals("")) {
            Toast.makeText(getContext(), "Please enter Scout Name", Toast.LENGTH_LONG).show();
            return;
        }

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

        currStandInfoArray = dbHelper.getDataAllStand(0);

        for (int i = 0; i < currStandInfoArray.length(); i++) {
            try {
                currTeam = currStandInfoArray.getJSONObject(i);
                if (currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_MATCH) == currMatch &&
                        currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TEAM) == teamNumber) {
                    currStandInfoIndex = i;
                    if (!MainActivity.ScoutName.equals("")) {
                        currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_SCOUT_NAME, MainActivity.ScoutName);
                    }
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
                if (lastAllianceBlue) {
                    currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_ALLIANCE, "Blue");
                } else {
                    currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_ALLIANCE, "Red");
                }

                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_HIGH, 0);
                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_LOW, 0);
                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_BASE_LINE, false);
                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_PLACE_GEAR, false);
                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_AUTO_OPEN_HOPPER, false);

                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_HIGH, 0);
                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_LOW, 0);
//                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_RECEIVED, 0);
                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_PLACED, 0);
                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_PENALTIES, 0);
                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_CLIMBED, false);
                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_TOUCHPAD, false);

                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_COMMENT, "");
                currTeam.put(DBContract.TableStandInfo.COLNAME_STAND_SCOUT_NAME, MainActivity.ScoutName);

                currStandInfoArray.put(currTeam);
                currStandInfoIndex = currStandInfoArray.length() - 1;
            } catch (JSONException e) {
                currTeam = null;
            }
        }

        standOKButton.setText("");
        showStandInfo();

        hideSoftKeyboard(getActivity());
    }

    private void showStandInfo() {

        try {

            fillingStandInfo = true;

            EditText stand_team_number_Textbox = (EditText) mRootView.findViewById(R.id.stand_team_number);
            TextView stand_team_number_view_Textbox = (TextView) mRootView.findViewById(R.id.stand_team_number_view);
            stand_team_number_view_Textbox.setText(stand_team_number_Textbox.getText());
            stand_team_number_view_Textbox.setVisibility(View.VISIBLE);
            stand_team_number_Textbox.setVisibility(View.INVISIBLE);

            Button stand_ok_Button = (Button) mRootView.findViewById(R.id.stand_ok_btn);
            stand_ok_Button.setText(currTeam.getString(DBContract.TableStandInfo.COLNAME_STAND_ALLIANCE));

            TextView auto_score_high_number_Textview = (TextView) mRootView.findViewById(R.id.stand_auto_score_high_number);
            auto_score_high_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_HIGH)));
            auto_score_high_number_Textview.setVisibility(View.VISIBLE);

            TextView auto_score_low_number_Textview = (TextView) mRootView.findViewById(R.id.stand_auto_score_low_number);
            auto_score_low_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_LOW)));
            auto_score_low_number_Textview.setVisibility(View.VISIBLE);

            ToggleButton auto_cross_baseline_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_cross_baseline_toggle);
            boolean auto_cross_baseline_flag = currTeam.getBoolean(DBContract.TableStandInfo.COLNAME_STAND_AUTO_BASE_LINE);
            auto_cross_baseline_toggle_ToggleButton.setChecked(auto_cross_baseline_flag);
            auto_cross_baseline_toggle_ToggleButton.setVisibility(View.VISIBLE);

            ToggleButton auto_place_gear_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_place_gear_toggle);
            boolean auto_place_gear_flag = currTeam.getBoolean(DBContract.TableStandInfo.COLNAME_STAND_AUTO_PLACE_GEAR);
            auto_place_gear_toggle_ToggleButton.setChecked(auto_place_gear_flag);
            auto_place_gear_toggle_ToggleButton.setVisibility(View.VISIBLE);

            ToggleButton auto_open_hopper_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_open_hopper_toggle);
            boolean auto_open_hopper_flag = currTeam.getBoolean(DBContract.TableStandInfo.COLNAME_STAND_AUTO_OPEN_HOPPER);
            auto_open_hopper_toggle_ToggleButton.setChecked(auto_open_hopper_flag);
            auto_open_hopper_toggle_ToggleButton.setVisibility(View.VISIBLE);

            TextView teleop_score_high_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_score_high_number);
            teleop_score_high_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_HIGH)));
            teleop_score_high_number_Textview.setVisibility(View.VISIBLE);

            TextView teleop_score_low_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_score_low_number);
            teleop_score_low_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_SCORE_LOW)));
            teleop_score_low_number_Textview.setVisibility(View.VISIBLE);

//            TextView teleop_gears_received_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_received_number);
//            teleop_gears_received_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_RECEIVED)));
//            teleop_gears_received_number_Textview.setVisibility(View.VISIBLE);

            TextView teleop_gears_placed_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_placed_number);
            teleop_gears_placed_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_GEARS_PLACED)));
            teleop_gears_placed_number_Textview.setVisibility(View.VISIBLE);

            TextView penalties_incurred_number_Textview = (TextView) mRootView.findViewById(R.id.stand_penalties_incurred_number);
            penalties_incurred_number_Textview.setText(Integer.toString(currTeam.getInt(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_PENALTIES)));
            penalties_incurred_number_Textview.setVisibility(View.VISIBLE);

            ToggleButton teleop_climb_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_teleop_climb_toggle);
            boolean teleop_climb_flag = currTeam.getBoolean(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_CLIMBED);
            teleop_climb_toggle_ToggleButton.setChecked(teleop_climb_flag);
            teleop_climb_toggle_ToggleButton.setVisibility(View.VISIBLE);

            ToggleButton teleop_touchpad_scored_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_teleop_touchpad_scored_toggle);
            boolean teleop_touchpad_scored_flag = currTeam.getBoolean(DBContract.TableStandInfo.COLNAME_STAND_TELEOP_TOUCHPAD);
            teleop_touchpad_scored_toggle_ToggleButton.setChecked(teleop_touchpad_scored_flag);
            teleop_touchpad_scored_toggle_ToggleButton.setVisibility(View.VISIBLE);

            EditText comments_text_Textview = (EditText) mRootView.findViewById(R.id.stand_comments_text);
            String comments_flag = currTeam.getString(DBContract.TableStandInfo.COLNAME_STAND_COMMENT);
            comments_text_Textview.setText(comments_flag);
            comments_text_Textview.setVisibility(View.VISIBLE);

            Button score_highMinusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_high_minus_btn);
            score_highMinusButton.setVisibility(View.VISIBLE);
            Button score_highPlusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_high_plus_btn);
            score_highPlusButton.setVisibility(View.VISIBLE);
            Button score_lowMinusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_low_minus_btn);
            score_lowMinusButton.setVisibility(View.VISIBLE);
            Button score_lowPlusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_low_plus_btn);
            score_lowPlusButton.setVisibility(View.VISIBLE);
            Button teleop_score_highMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_high_minus_btn);
            teleop_score_highMinusButton.setVisibility(View.VISIBLE);
            Button teleop_score_highPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_high_plus_btn);
            teleop_score_highPlusButton.setVisibility(View.VISIBLE);
            Button teleop_score_lowMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_low_minus_btn);
            teleop_score_lowMinusButton.setVisibility(View.VISIBLE);
            Button teleop_score_lowPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_low_plus_btn);
            teleop_score_lowPlusButton.setVisibility(View.VISIBLE);
//            Button teleop_gears_receviedMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_received_minus_btn);
//            teleop_gears_receviedMinusButton.setVisibility(View.VISIBLE);
//            Button teleop_gears_receivedPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_received_plus_btn);
//            teleop_gears_receivedPlusButton.setVisibility(View.VISIBLE);
            Button teleop_gears_placedMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_placed_minus_btn);
            teleop_gears_placedMinusButton.setVisibility(View.VISIBLE);
            Button teleop_gears_placedPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_placed_plus_btn);
            teleop_gears_placedPlusButton.setVisibility(View.VISIBLE);
            Button penalties_incurredMinusButton = (Button) mRootView.findViewById(R.id.stand_penalties_incurred_minus_btn);
            penalties_incurredMinusButton.setVisibility(View.VISIBLE);
            Button penalties_incurredPlusButton = (Button) mRootView.findViewById(R.id.stand_penalties_incurred_plus_btn);
            penalties_incurredPlusButton.setVisibility(View.VISIBLE);

            TextView stand_auto_period_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_period_label);
            stand_auto_period_label_Text.setVisibility(View.VISIBLE);
            TextView stand_auto_score_high_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_score_high_label);
            stand_auto_score_high_label_Text.setVisibility(View.VISIBLE);
            TextView stand_auto_score_low_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_score_low_label);
            stand_auto_score_low_label_Text.setVisibility(View.VISIBLE);
            TextView stand_auto_cross_baseline_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_cross_baseline_label);
            stand_auto_cross_baseline_label_Text.setVisibility(View.VISIBLE);
            TextView stand_auto_place_gear_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_place_gear_label);
            stand_auto_place_gear_label_Text.setVisibility(View.VISIBLE);
            TextView stand_auto_open_hopper_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_open_hopper_label);
            stand_auto_open_hopper_label_Text.setVisibility(View.VISIBLE);
            TextView stand_teleop_period_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_period_label);
            stand_teleop_period_label_Text.setVisibility(View.VISIBLE);
            TextView stand_teleop_score_high_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_score_high_label);
            stand_teleop_score_high_label_Text.setVisibility(View.VISIBLE);
            TextView stand_teleop_score_low_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_score_low_label);
            stand_teleop_score_low_label_Text.setVisibility(View.VISIBLE);
//            TextView stand_teleop_gears_received_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_received_label);
//            stand_teleop_gears_received_label_Text.setVisibility(View.VISIBLE);
            TextView stand_teleop_gears_placed_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_placed_label);
            stand_teleop_gears_placed_label_Text.setVisibility(View.VISIBLE);
            TextView stand_penalties_incurred_label_Text = (TextView) mRootView.findViewById(R.id.stand_penalties_incurred_label);
            stand_penalties_incurred_label_Text.setVisibility(View.VISIBLE);
            TextView stand_teleop_climb_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_climb_label);
            stand_teleop_climb_label_Text.setVisibility(View.VISIBLE);
            TextView stand_teleop_touchpad_scored_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_touchpad_scored_label);
            stand_teleop_touchpad_scored_label_Text.setVisibility(View.VISIBLE);
            TextView stand_comments_label_Text = (TextView) mRootView.findViewById(R.id.stand_comments_label);
            stand_comments_label_Text.setVisibility(View.VISIBLE);

            fillingStandInfo = false;

        } catch (JSONException e) {
            fillingStandInfo = false;
            //e.printStackTrace();
        }
    }

    public void clearStandScreen() {

        fillingStandInfo = true;

        try {

            EditText stand_team_number_Textbox = (EditText) mRootView.findViewById(R.id.stand_team_number);
            TextView stand_team_number_view_Textbox = (TextView) mRootView.findViewById(R.id.stand_team_number_view);
            stand_team_number_view_Textbox.setText("");
            stand_team_number_Textbox.setText("");
            stand_team_number_view_Textbox.setVisibility(View.INVISIBLE);
            stand_team_number_Textbox.setVisibility(View.VISIBLE);

            Button stand_ok_Button = (Button) mRootView.findViewById(R.id.stand_ok_btn);
            stand_ok_Button.setText("LOAD");

            TextView auto_score_high_number_Textview = (TextView) mRootView.findViewById(R.id.stand_auto_score_high_number);
            auto_score_high_number_Textview.setText("0");
            auto_score_high_number_Textview.setVisibility(View.INVISIBLE);

            TextView auto_score_low_number_Textview = (TextView) mRootView.findViewById(R.id.stand_auto_score_low_number);
            auto_score_low_number_Textview.setText("0");
            auto_score_low_number_Textview.setVisibility(View.INVISIBLE);

            ToggleButton auto_cross_baseline_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_cross_baseline_toggle);
            auto_cross_baseline_toggle_ToggleButton.setChecked(false);
            auto_cross_baseline_toggle_ToggleButton.setVisibility(View.INVISIBLE);

            ToggleButton auto_place_gear_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_place_gear_toggle);
            auto_place_gear_toggle_ToggleButton.setChecked(false);
            auto_place_gear_toggle_ToggleButton.setVisibility(View.INVISIBLE);

            ToggleButton auto_open_hopper_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_auto_open_hopper_toggle);
            auto_open_hopper_toggle_ToggleButton.setChecked(false);
            auto_open_hopper_toggle_ToggleButton.setVisibility(View.INVISIBLE);

            TextView teleop_score_high_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_score_high_number);
            teleop_score_high_number_Textview.setText("0");
            teleop_score_high_number_Textview.setVisibility(View.INVISIBLE);

            TextView teleop_score_low_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_score_low_number);
            teleop_score_low_number_Textview.setText("0");
            teleop_score_low_number_Textview.setVisibility(View.INVISIBLE);

//            TextView teleop_gears_received_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_received_number);
//            teleop_gears_received_number_Textview.setText("0");
//            teleop_gears_received_number_Textview.setVisibility(View.INVISIBLE);

            TextView teleop_gears_placed_number_Textview = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_placed_number);
            teleop_gears_placed_number_Textview.setText("0");
            teleop_gears_placed_number_Textview.setVisibility(View.INVISIBLE);

            TextView penalties_incurred_number_Textview = (TextView) mRootView.findViewById(R.id.stand_penalties_incurred_number);
            penalties_incurred_number_Textview.setText("0");
            penalties_incurred_number_Textview.setVisibility(View.INVISIBLE);

            ToggleButton teleop_climb_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_teleop_climb_toggle);
            teleop_climb_toggle_ToggleButton.setChecked(false);
            teleop_climb_toggle_ToggleButton.setVisibility(View.INVISIBLE);

            ToggleButton teleop_touchpad_scored_toggle_ToggleButton = (ToggleButton) mRootView.findViewById(R.id.stand_teleop_touchpad_scored_toggle);
            teleop_touchpad_scored_toggle_ToggleButton.setChecked(false);
            teleop_touchpad_scored_toggle_ToggleButton.setVisibility(View.INVISIBLE);

            EditText stand_comments_text_Textview = (EditText) mRootView.findViewById(R.id.stand_comments_text);
            stand_comments_text_Textview.setText("");
            stand_comments_text_Textview.setVisibility(View.INVISIBLE);

            Button score_highMinusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_high_minus_btn);
            score_highMinusButton.setVisibility(View.INVISIBLE);
            Button score_highPlusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_high_plus_btn);
            score_highPlusButton.setVisibility(View.INVISIBLE);
            Button score_lowMinusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_low_minus_btn);
            score_lowMinusButton.setVisibility(View.INVISIBLE);
            Button score_lowPlusButton = (Button) mRootView.findViewById(R.id.stand_auto_score_low_plus_btn);
            score_lowPlusButton.setVisibility(View.INVISIBLE);
            Button teleop_score_highMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_high_minus_btn);
            teleop_score_highMinusButton.setVisibility(View.INVISIBLE);
            Button teleop_score_highPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_high_plus_btn);
            teleop_score_highPlusButton.setVisibility(View.INVISIBLE);
            Button teleop_score_lowMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_low_minus_btn);
            teleop_score_lowMinusButton.setVisibility(View.INVISIBLE);
            Button teleop_score_lowPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_score_low_plus_btn);
            teleop_score_lowPlusButton.setVisibility(View.INVISIBLE);
//            Button teleop_gears_receviedMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_received_minus_btn);
//            teleop_gears_receviedMinusButton.setVisibility(View.INVISIBLE);
//            Button teleop_gears_receivedPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_received_plus_btn);
//            teleop_gears_receivedPlusButton.setVisibility(View.INVISIBLE);
            Button teleop_gears_placedMinusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_placed_minus_btn);
            teleop_gears_placedMinusButton.setVisibility(View.INVISIBLE);
            Button teleop_gears_placedPlusButton = (Button) mRootView.findViewById(R.id.stand_teleop_gears_placed_plus_btn);
            teleop_gears_placedPlusButton.setVisibility(View.INVISIBLE);
            Button penalties_incurredMinusButton = (Button) mRootView.findViewById(R.id.stand_penalties_incurred_minus_btn);
            penalties_incurredMinusButton.setVisibility(View.INVISIBLE);
            Button penalties_incurredPlusButton = (Button) mRootView.findViewById(R.id.stand_penalties_incurred_plus_btn);
            penalties_incurredPlusButton.setVisibility(View.INVISIBLE);

            TextView stand_auto_period_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_period_label);
            stand_auto_period_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_auto_score_high_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_score_high_label);
            stand_auto_score_high_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_auto_score_low_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_score_low_label);
            stand_auto_score_low_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_auto_cross_baseline_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_cross_baseline_label);
            stand_auto_cross_baseline_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_auto_place_gear_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_place_gear_label);
            stand_auto_place_gear_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_auto_open_hopper_label_Text = (TextView) mRootView.findViewById(R.id.stand_auto_open_hopper_label);
            stand_auto_open_hopper_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_teleop_period_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_period_label);
            stand_teleop_period_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_teleop_score_high_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_score_high_label);
            stand_teleop_score_high_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_teleop_score_low_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_score_low_label);
            stand_teleop_score_low_label_Text.setVisibility(View.INVISIBLE);
//            TextView stand_teleop_gears_received_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_received_label);
//            stand_teleop_gears_received_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_teleop_gears_placed_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_gears_placed_label);
            stand_teleop_gears_placed_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_penalties_incurred_label_Text = (TextView) mRootView.findViewById(R.id.stand_penalties_incurred_label);
            stand_penalties_incurred_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_teleop_climb_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_climb_label);
            stand_teleop_climb_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_teleop_touchpad_scored_label_Text = (TextView) mRootView.findViewById(R.id.stand_teleop_touchpad_scored_label);
            stand_teleop_touchpad_scored_label_Text.setVisibility(View.INVISIBLE);
            TextView stand_comments_label_Text = (TextView) mRootView.findViewById(R.id.stand_comments_label);
            stand_comments_label_Text.setVisibility(View.INVISIBLE);

        } catch (Exception e) {

            fillingStandInfo = false;

        }

        fillingStandInfo = false;

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}