package com.bertrobotics.bertscout2017;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.test.espresso.core.deps.guava.base.Charsets;
import android.support.test.espresso.core.deps.guava.io.CharStreams;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static android.net.Uri.encode;


/**
 * Created by Clayton on 3/7/2017.
 */

public class ManageDataFragment extends Fragment {
    public View mRootView;
    private final String PASSWORD = "bertdata";
    private final String IP_ADDRESS = "76.179.97.182";

    public DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.manage_data, container, false);
        dbHelper = new DBHelper(mRootView.getContext());

        Button sendDataBtn = (Button) mRootView.findViewById(R.id.sendDataBtn);
        sendDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordText = ((EditText) mRootView.findViewById(R.id.passwordText)).getText().toString();

                if (passwordText.equals(PASSWORD)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mRootView.getContext());
                    alertDialogBuilder.setMessage("Do you want to send data to the remote server?");

                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            String result;
                            try {
                                SendData sendData = new SendData(mRootView.getContext());
                                sendData.execute();
                            } catch (Exception e) {
//                                result = "Error";
                            }
//                            Context context = mRootView.getContext();
//                            int duration = Toast.LENGTH_LONG;
//                            Toast toast = Toast.makeText(context, result, duration);
//                            toast.show();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button pullDataBtn = (Button) mRootView.findViewById(R.id.pullDataBtn);
        pullDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordText = ((EditText) mRootView.findViewById(R.id.passwordText)).getText().toString();

                if (passwordText.equals(PASSWORD)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mRootView.getContext());
                    alertDialogBuilder.setMessage("Do you want to pull data from the remote server?");

                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            String result;
                            try {
                                PullData pullData = new PullData(mRootView.getContext());
                                pullData.execute();


                            } catch (Exception e) {
//                                result = "Error";
                            }
//                            Context context = mRootView.getContext();
//                            int duration = Toast.LENGTH_LONG;
//                            Toast toast = Toast.makeText(context, result, duration);
//                            toast.show();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button exportDataBtn = (Button) mRootView.findViewById(R.id.exportDataBtn);
        exportDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordText = ((EditText) mRootView.findViewById(R.id.passwordText)).getText().toString();

                if (passwordText.equals(PASSWORD)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mRootView.getContext());
                    alertDialogBuilder.setMessage("Export data to Documents folder?");

                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        String result;
                        try {
                            // stand info
                            JSONObject standInfo = new JSONObject();
                            standInfo.put("dbversion", DBHelper.DATABASE_VERSION);
                            JSONArray currStandInfoArray = dbHelper.getDataAllStand(0);
                            standInfo.put("stand_data", currStandInfoArray);

                            String kindleName = "Scouting 2017";

                            String standOutFilename = ExportData.getDocumentStorageDir() + "/" +
                                    kindleName.toLowerCase().replaceAll(" ", "_") + "_stand.json";

                            ExportData.ExportData(standOutFilename, standInfo);
                            // pit info
                            JSONObject pitInfo = new JSONObject();
                            pitInfo.put("dbversion", DBHelper.DATABASE_VERSION);
                            JSONArray currPitInfoArray = dbHelper.getDataAllPit();
                            pitInfo.put("pit_data", currPitInfoArray);
                            String pitOutFilename = ExportData.getDocumentStorageDir() + "/" +
                                    kindleName.toLowerCase().replaceAll(" ", "_") + "_pit.json";
                            ExportData.ExportData(pitOutFilename, pitInfo);
                            result = "Export done!";
                        } catch (Exception e) {
                            result = "Error during export";
                        }
                        Context context = mRootView.getContext();
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, result, duration);
                        toast.show();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button clearLocalDatabaseBtn = (Button) mRootView.findViewById(R.id.clearLocalDatabaseBtn);
        clearLocalDatabaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordText = ((EditText) mRootView.findViewById(R.id.passwordText)).getText().toString();

                if (passwordText.equals(PASSWORD)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mRootView.getContext());
                    alertDialogBuilder.setMessage("Do you want to *** ERASE ALL LOCAL DATA *** ???");

                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            String result;
                            try {
                                dbHelper.deleteStandScouting();
                                dbHelper.deletePitInfo();
                                result = "Clear done!";
                            } catch (Exception e) {
                                result = "Error during clear";
                            }
                            Context context = mRootView.getContext();
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, result, duration);
                            toast.show();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button clearRemoteDatabaseBtn = (Button) mRootView.findViewById(R.id.clearRemoteDatabaseBtn);
        clearRemoteDatabaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordText = ((EditText) mRootView.findViewById(R.id.passwordText)).getText().toString();

                if (passwordText.equals(PASSWORD)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mRootView.getContext());
                    alertDialogBuilder.setMessage("Do you want to *** ERASE ALL REMOTE DATA *** ???");

                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            String result;
                            try {
                                ClearData clearData = new ClearData(mRootView.getContext());
                                clearData.execute();
                            } catch (Exception e) {
//                                result = "Error during clear";
                            }
//                            Context context = mRootView.getContext();
//                            int duration = Toast.LENGTH_LONG;
//                            Toast toast = Toast.makeText(context, result, duration);
//                            toast.show();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                }
            }
        });


        return mRootView;
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    private class SendData extends AsyncTask<String, Void, String> {

        ProgressDialog progress;

        private SendData(Context context) {
            progress = new ProgressDialog(context);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONArray standData = dbHelper.getDataAllStand(0);

                String insertResult;

                for (int i = 0; i < standData.length(); i++) {
                    JSONObject row = standData.getJSONObject(i);

                    try {
                        String insertString = "http://" + IP_ADDRESS + "/insertStandData.php?" +
                                "&match_no=" + row.getInt("match_no") +
                                "&team=" + row.getInt("team") +
                                "&alliance_red_blue=" + encode(row.getString("alliance_red_blue")) +
                                "&auto_score_high=" + row.getInt("auto_score_high") +
                                "&auto_score_low=" + row.getInt("auto_score_low") +
                                "&auto_base_line=" + row.getBoolean("auto_base_line") +
                                "&auto_place_gear=" + row.getBoolean("auto_place_gear") +
                                "&auto_open_hopper=" + row.getBoolean("auto_open_hopper") +
                                "&tele_score_high=" + row.getInt("tele_score_high") +
                                "&tele_score_low=" + row.getInt("tele_score_low") +
                                "&tele_gears_received=" + row.getInt("tele_gears_received") +
                                "&tele_gears_placed=" + row.getInt("tele_gears_placed") +
                                "&tele_penalties=" + row.getInt("tele_penalties") +
                                "&tele_climbed=" + row.getBoolean("tele_climbed") +
                                "&tele_touchpad=" + row.getBoolean("tele_touchpad") +
                                "&stand_comment=" + encode(row.getString("stand_comment")) +
                                "&scout_name=" + encode(row.getString("scout_name"));

                        // Do your long operations here and return the result

                        URL insertUrl = new URL(insertString);

                        HttpURLConnection insertUrlConnection = (HttpURLConnection) insertUrl.openConnection();

                        InputStream insertIn = new BufferedInputStream(insertUrlConnection.getInputStream());

                        insertResult = CharStreams.toString(new InputStreamReader(
                                insertIn, Charsets.UTF_8));

                    } catch (Exception e) {
                        return "Error";
                    }
                }
            } catch (Exception e) {
                return "Error";
            }

            try {
                JSONArray pitData = dbHelper.getDataAllPit();

                String insertResult;

                for (int i = 0; i < pitData.length(); i++) {
                    JSONObject row = pitData.getJSONObject(i);

                    try {
                        String insertString = "http://" + IP_ADDRESS + "/insertPitData.php?" +
                                "&team=" + row.getInt("team") +
                                "&team_name=" + encode(row.getString("team_name")) +
                                "&team_years=" + row.getInt("team_years") +
                                "&team_members=" + row.getInt("team_members") +
                                "&height=" + row.getInt("height") +
                                "&weight=" + row.getInt("weight") +
                                "&num_cims=" + row.getInt("num_cims") +
                                "&max_speed=" + row.getInt("max_speed") +
                                "&wheel_type=" + encode(row.getString("wheel_type")) +
                                "&wheel_layout=" + encode(row.getString("wheel_layout")) +
                                "&max_fuel=" + row.getInt("max_fuel") +
                                "&shoot_speed=" + row.getInt("shoot_speed") +
                                "&shoot_location=" + encode(row.getString("shoot_location")) +
                                "&can_shoot_high=" + row.getBoolean("can_shoot_high") +
                                "&can_shoot_low=" + row.getBoolean("can_shoot_low") +
                                "&floor_pickup=" + row.getBoolean("floor_pickup") +
                                "&top_loader=" + row.getBoolean("top_loader") +
                                "&auto_aim=" + row.getBoolean("auto_aim") +
                                "&can_carry_gear=" + row.getBoolean("can_carry_gear") +
                                "&can_climb=" + row.getBoolean("can_climb") +
                                "&own_rope=" + row.getBoolean("own_rope") +
                                "&start_left=" + row.getBoolean("start_left") +
                                "&start_center=" + row.getBoolean("start_center") +
                                "&start_right=" + row.getBoolean("start_right") +
                                "&auto_num_modes=" + row.getInt("auto_num_modes") +
                                "&auto_base_line=" + row.getBoolean("auto_base_line") +
                                "&auto_place_gear=" + row.getBoolean("auto_place_gear") +
                                "&auto_high_goal=" + row.getBoolean("auto_high_goal") +
                                "&auto_low_goal=" + row.getBoolean("auto_low_goal") +
                                "&auto_hopper=" + row.getBoolean("auto_hopper") +
                                "&team_rating=" + row.getInt("team_rating") +
                                "&comment=" + encode(row.getString("comment")) +
                                "&scout_name=" + encode(row.getString("scout_name"));

                        // Do your long operations here and return the result

                        URL insertUrl = new URL(insertString);

                        HttpURLConnection insertUrlConnection = (HttpURLConnection) insertUrl.openConnection();

                        InputStream insertIn = new BufferedInputStream(insertUrlConnection.getInputStream());

                        insertResult = CharStreams.toString(new InputStreamReader(
                                insertIn, Charsets.UTF_8));

                    } catch (Exception e) {
                        return "Error";
                    }
                }
            } catch (Exception e) {
                return "Error";
            }

            return "Success";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Syncing");
            progress.setMessage("Please wait...");
            progress.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progress.dismiss();

            Toast.makeText(mRootView.getContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    private class PullData extends AsyncTask<String, Void, String> {

        ProgressDialog progress;

        private PullData(Context context) {
            progress = new ProgressDialog(context);
        }

        @Override
        protected String doInBackground(String... params) {
            String results;

            try {
                // Update Stand Data
                String urlString = "http://" + IP_ADDRESS + "/getStandData.php";

                // Do your long operations here and return the result
                URL url = new URL(urlString);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                results = CharStreams.toString(new InputStreamReader(
                        in, Charsets.UTF_8));

                JSONArray remoteData = new JSONArray(results);

                for (int i = 0; i < remoteData.length(); i++) {
                    JSONObject row = remoteData.getJSONObject(i);

                    if (row.has("auto_base_line")) {
                        if (row.getInt("auto_base_line") == 1) {
                            row.put("auto_base_line", true);
                        } else {
                            row.put("auto_base_line", false);
                        }
                    }

                    if (row.has("auto_place_gear")) {
                        if (row.getInt("auto_place_gear") == 1) {
                            row.put("auto_place_gear", true);
                        } else {
                            row.put("auto_place_gear", false);
                        }
                    }

                    if (row.has("auto_open_hopper")) {
                        if (row.getInt("auto_open_hopper") == 1) {
                            row.put("auto_open_hopper", true);
                        } else {
                            row.put("auto_open_hopper", false);
                        }
                    }

                    if (row.has("tele_climbed")) {
                        if (row.getInt("tele_climbed") == 1) {
                            row.put("tele_climbed", true);
                        } else {
                            row.put("tele_climbed", false);
                        }
                    }

                    if (row.has("tele_touchpad")) {
                        if (row.getInt("tele_touchpad") == 1) {
                            row.put("tele_touchpad", true);
                        } else {
                            row.put("tele_touchpad", false);
                        }
                    }

                    dbHelper.updateStandInfo(row);
                }
            } catch (Exception e) {
                return "Error";
            }

            try {
                // Update Pit Data
                String urlString = "http://" + IP_ADDRESS + "/getPitData.php";

                // Do your long operations here and return the result
                URL url = new URL(urlString);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                results = CharStreams.toString(new InputStreamReader(
                        in, Charsets.UTF_8));

                JSONArray remoteData = new JSONArray(results);

                for (int i = 0; i < remoteData.length(); i++) {
                    JSONObject row = remoteData.getJSONObject(i);

                    if (row.has("can_shoot_high")) {
                        if (row.getInt("can_shoot_high") == 1) {
                            row.put("can_shoot_high", true);
                        } else {
                            row.put("can_shoot_high", false);
                        }
                    }

                    if (row.has("can_shoot_low")) {
                        if (row.getInt("can_shoot_low") == 1) {
                            row.put("can_shoot_low", true);
                        } else {
                            row.put("can_shoot_low", false);
                        }
                    }

                    if (row.has("floor_pickup")) {
                        if (row.getInt("floor_pickup") == 1) {
                            row.put("floor_pickup", true);
                        } else {
                            row.put("floor_pickup", false);
                        }
                    }

                    if (row.has("top_loader")) {
                        if (row.getInt("top_loader") == 1) {
                            row.put("top_loader", true);
                        } else {
                            row.put("top_loader", false);
                        }
                    }

                    if (row.has("auto_aim")) {
                        if (row.getInt("auto_aim") == 1) {
                            row.put("auto_aim", true);
                        } else {
                            row.put("auto_aim", false);
                        }
                    }

                    if (row.has("can_carry_gear")) {
                        if (row.getInt("can_carry_gear") == 1) {
                            row.put("can_carry_gear", true);
                        } else {
                            row.put("can_carry_gear", false);
                        }
                    }
                    if (row.has("can_climb")) {
                        if (row.getInt("can_climb") == 1) {
                            row.put("can_climb", true);
                        } else {
                            row.put("can_climb", false);
                        }
                    }

                    if (row.has("own_rope")) {
                        if (row.getInt("own_rope") == 1) {
                            row.put("own_rope", true);
                        } else {
                            row.put("own_rope", false);
                        }
                    }

                    if (row.has("start_left")) {
                        if (row.getInt("start_left") == 1) {
                            row.put("start_left", true);
                        } else {
                            row.put("start_left", false);
                        }
                    }

                    if (row.has("start_center")) {
                        if (row.getInt("start_center") == 1) {
                            row.put("start_center", true);
                        } else {
                            row.put("start_center", false);
                        }
                    }

                    if (row.has("start_right")) {
                        if (row.getInt("start_right") == 1) {
                            row.put("start_right", true);
                        } else {
                            row.put("start_right", false);
                        }
                    }

                    if (row.has("auto_base_line")) {
                        if (row.getInt("auto_base_line") == 1) {
                            row.put("auto_base_line", true);
                        } else {
                            row.put("auto_base_line", false);
                        }
                    }

                    if (row.has("auto_place_gear")) {
                        if (row.getInt("auto_place_gear") == 1) {
                            row.put("auto_place_gear", true);
                        } else {
                            row.put("auto_place_gear", false);
                        }
                    }

                    if (row.has("auto_high_goal")) {
                        if (row.getInt("auto_high_goal") == 1) {
                            row.put("auto_high_goal", true);
                        } else {
                            row.put("auto_high_goal", false);
                        }
                    }

                    if (row.has("auto_low_goal")) {
                        if (row.getInt("auto_low_goal") == 1) {
                            row.put("auto_low_goal", true);
                        } else {
                            row.put("auto_low_goal", false);
                        }
                    }

                    if (row.has("auto_hopper")) {
                        if (row.getInt("auto_hopper") == 1) {
                            row.put("auto_hopper", true);
                        } else {
                            row.put("auto_hopper", false);
                        }
                    }

                    dbHelper.updatePitInfo(row);
                }
            } catch (Exception e) {
                return "Error";
            }

            return "Success";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Syncing");
            progress.setMessage("Please wait...");
            progress.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progress.dismiss();

            Toast.makeText(mRootView.getContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    private class ClearData extends AsyncTask<String, Void, String> {

        ProgressDialog progress;

        private ClearData(Context context) {
            progress = new ProgressDialog(context);
        }

        @Override
        protected String doInBackground(String... params) {
            String results;

            try {
                // Update Stand Data
                String urlString = "http://" + IP_ADDRESS + "/clearData.php";

                // Do your long operations here and return the result
                URL url = new URL(urlString);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (Exception e) {
                return "Error";
            }

            return "Success";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Deleting");
            progress.setMessage("Please wait...");
            progress.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progress.dismiss();

            Toast.makeText(mRootView.getContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    public String GetKindleName() {
        try {
            String titleFilename = ExportData.getDocumentStorageDir() + "/kindlename.txt";
            String title = ExportData.ImportData(titleFilename);
            return title;
        } catch (Exception e) {
            return "Scouting 2017";
        }
    }

}
