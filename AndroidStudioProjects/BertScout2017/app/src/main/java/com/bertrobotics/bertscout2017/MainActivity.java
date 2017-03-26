package com.bertrobotics.bertscout2017;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.test.espresso.core.deps.guava.base.Charsets;
import android.support.test.espresso.core.deps.guava.io.CharStreams;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.net.Uri.encode;

public class MainActivity extends AppCompatActivity {

    StandScoutingFragment mStandScoutingFragment;
    PitScoutingFragment mPitScoutingFragment;
    StatisticsFragment mStatisticsFragment;
    ManageDataFragment mManageDataFragment;

    View mRootView;

    public static String ScoutName = "";

    public DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRootView = findViewById(R.id.main_layout);

        mStandScoutingFragment = new StandScoutingFragment();
        mPitScoutingFragment = new PitScoutingFragment();
        mStatisticsFragment = new StatisticsFragment();
        mManageDataFragment = new ManageDataFragment();

        dbHelper = new DBHelper(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Stand"));
        tabLayout.addTab(tabLayout.newTab().setText("Pit"));
        tabLayout.addTab(tabLayout.newTab().setText("Statistics"));
        tabLayout.addTab(tabLayout.newTab().setText("Data"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        enterScoutNameDialog(mRootView);

        SetMainTitle();

        getSupportFragmentManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
//            case R.id.north_shore:
//                setTitle("North Shore");
//
////                mStandScoutingFragment.buildMatchSpinner("north_shore");
//                mStandScoutingFragment.buildStandTeamSpinner("teams_list");
//
//                mPitScoutingFragment.buildPitTeamSpinner("teams_list");
//
//                mStatisticsFragment.populateList();
//                break;
//            case R.id.pine_tree:
//                setTitle("Pine Tree");
//
////                mStandScoutingFragment.buildMatchSpinner("pine_tree");
//                mStandScoutingFragment.buildStandTeamSpinner("teams_list");
//
//                mPitScoutingFragment.buildPitTeamSpinner("teams_list");
//
//                mStatisticsFragment.populateList();
//                break;
            case R.id.enter_scout_name:
                enterScoutNameDialog(mRootView);
                break;
//            case R.id.export_data:
//                openExportDataDialog(mRootView);
//                break;
//            case R.id.sync_data:
//                openSyncDataDialog(mRootView);
//                break;
//            case R.id.clear_data:
//                openClearDataDialog(mRootView);
//                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.getTabAt(0).select();

            Integer match_no = data.getIntExtra("match_no", 0);
            Integer team = data.getIntExtra("team", 0);

            TextView matchText = (TextView) mRootView.findViewById(R.id.stand_match_number);
            matchText.setText(Integer.toString(match_no));

            TextView teamText = (TextView) mRootView.findViewById(R.id.stand_team_number);
            teamText.setText(Integer.toString(team));

//        Spinner standTeamSpinner = (Spinner) findViewById(R.id.stand_team_spinner);
//        standTeamSpinner.setSelection(((ArrayAdapter) standTeamSpinner.getAdapter()).getPosition(team.toString()));
//
//        Spinner matchSpinner = (Spinner) findViewById(R.id.match_spinner);
//        matchSpinner.setSelection(((ArrayAdapter) matchSpinner.getAdapter()).getPosition(match_no.toString()));
//
//        mStandScoutingFragment.loadScreen();

//        Button standOKButton = (Button) mRootView.findViewById(R.id.stand_ok_btn);
//        standOKButton.callOnClick();
            mStandScoutingFragment.loadScreen();
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return mStandScoutingFragment;
                case 1:
                    return mPitScoutingFragment;
                case 2:
                    return mStatisticsFragment;
                case 3:
                    return mManageDataFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

//    public void openSyncDataDialog(final View view) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("Sync data for " + getTitle() + "?");
//
//        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                AsyncTaskSyncData syncData = new AsyncTaskSyncData(view.getContext());
//                syncData.execute();
//            }
//        });
//
//        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }
//
//    private class AsyncTaskSyncData extends AsyncTask<String, Void, String> {
//
//        ProgressDialog progress;
//
//        private AsyncTaskSyncData(Context context) {
//            progress = new ProgressDialog(context);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String results;
//
//            try {
//                String urlString = "http://76.179.97.182/getData.php?event=" + getTitle().toString().replace(" ", "%20");
//
//                // Do your long operations here and return the result
//                URL url = new URL(urlString);
//
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//
//                results = CharStreams.toString(new InputStreamReader(
//                        in, Charsets.UTF_8));
//
//                JSONArray remoteData = new JSONArray(results);
//
//                for (int i = 0; i < remoteData.length(); i++) {
//                    JSONObject row = remoteData.getJSONObject(i);
//                    dbHelper.updateStandInfo(row);
//                }
//
//                JSONArray localData = dbHelper.getDataAllStand(0);
//
//                String insertResult;
//
//                for (int i = 0; i < localData.length(); i++) {
//                    JSONObject row = localData.getJSONObject(i);
//
//                    try {
//                        String insertString = "http://76.179.97.182/insertData.php?" +
//                                "event=" + encode(row.getString("event")) +
//                                "&match_no=" + row.getInt("match_no") +
//                                "&team=" + row.getInt("team") +
//                                "&auto_high=" + row.getInt("auto_high") +
//                                "&auto_low=" + row.getInt("auto_low") +
//                                "&auto_cross=" + row.getInt("auto_cross") +
//                                "&tele_high=" + row.getInt("tele_high") +
//                                "&tele_low=" + row.getInt("tele_low") +
//                                "&tele_cross=" + row.getInt("tele_cross") +
//                                "&endgame=" + row.getInt("endgame") +
//                                "&comment=" + encode(row.getString("comment"));
//
//                        // Do your long operations here and return the result
//
//                        URL insertUrl = new URL(insertString);
//
//                        HttpURLConnection insertUrlConnection = (HttpURLConnection) insertUrl.openConnection();
//
//                        InputStream insertIn = new BufferedInputStream(insertUrlConnection.getInputStream());
//
//                        insertResult = CharStreams.toString(new InputStreamReader(
//                                insertIn, Charsets.UTF_8));
//
//                    } catch (Exception e) {
//                        return "Error";
//                    }
//                }
//            } catch (Exception e) {
//                return "Error";
//            }
//
//            return "Success";
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progress.setTitle("Syncing");
//            progress.setMessage("Please wait...");
//            progress.show();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            //mStatisticsFragment.populateList();
//
//            progress.dismiss();
//
//            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//        }
//    }

    public void openExportDataDialog(final View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Export data to Documents folder?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String result;
                try {
                    String kindleName = GetKindleName();
                    // stand info
                    JSONObject standInfo = new JSONObject();
                    standInfo.put("dbversion", DBHelper.DATABASE_VERSION);
                    JSONArray currStandInfoArray = dbHelper.getDataAllStand(0);
                    standInfo.put("stand_data", currStandInfoArray);
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
                Context context = getApplicationContext();
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
    }

    public void openClearDataDialog(final View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");
        final String realPassword = "3.14";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to *** ERASE *** ALL *** DATA *** ???");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String result;
                try {
                    dbHelper.deleteStandScouting();
                    dbHelper.deletePitInfo();
                    result = "Clear done! Please exit app!";
                } catch (Exception e) {
                    result = "Error during clear";
                }
                Context context = getApplicationContext();
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

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String clearPassword = input.getText().toString();
                if (clearPassword.equals(realPassword)) {
                    alertDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void enterScoutNameDialog(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Scout Name");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.ScoutName = input.getText().toString();
                SetMainTitle();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

//    public void openClearDataDialog(final View view){
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("Clear all data?");
//
//        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                AsyncTaskClearData clearData = new AsyncTaskClearData(view.getContext());
//                clearData.execute();
//            }
//        });
//
//        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // do nothing
//            }
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }

//    private class AsyncTaskClearData extends AsyncTask<String, Void, String> {
//
//        ProgressDialog progress;
//
//        private AsyncTaskClearData(Context context) {
//            progress = new ProgressDialog(context);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            progress.setTitle("Saving");
//            progress.setMessage("Please wait...");
//            progress.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                dbHelper.deleteStandScouting();
//                dbHelper.deletePitInfo();
//            } catch (Exception e) {
//                return "Failure";
//            }
//            return "Success";
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            mStatisticsFragment.populateList();
//
//            progress.dismiss();
//
//            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//        }
//    }

    public void SetMainTitle() {
        String tempScoutName;
        if (ScoutName.equals("")) {
            tempScoutName = "";
        } else {
            tempScoutName = " - " + ScoutName;
        }
        setTitle(GetKindleName() + tempScoutName);
    }

    public static String GetKindleName() {
        try {
            String titleFilename = ExportData.getDocumentStorageDir() + "/kindlename.txt";
            String title = ExportData.ImportData(titleFilename);
            return title;
        } catch (Exception e) {
            return "Kindle NoName";
        }
    }
}