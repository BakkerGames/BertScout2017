package com.bertrobotics.bertscout2017;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PitScouting extends Fragment {

    public View rootView;
    public DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.pit_scouting, container, false);
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

        ListView listView = (ListView) rootView.findViewById(R.id.listview1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1, teams);

        listView.setAdapter(adapter);



        return inflater.inflate(R.layout.pit_scouting, container, false);
    }
}