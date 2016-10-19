package com.bertrobotics.bertscout2017;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Statistics extends Fragment {

    public View rootView;
    public DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.statistics, container, false);
        dbHelper = new DBHelper(rootView.getContext());

        JSONArray data = dbHelper.getData();

        ArrayList<String> teams = new ArrayList<String>();

        for (int i = 0; i < data.length(); i++) {
            try {
                String team = data.getJSONObject(i).getString("team");
                teams.add(team);

            } catch (JSONException e) {

            }
        }

        ListView listView = (ListView) rootView.findViewById(R.id.data_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1, teams);

        listView.setAdapter(adapter);

        return inflater.inflate(R.layout.statistics, container, false);
    }
}