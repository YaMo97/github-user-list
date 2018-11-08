package com.github.yamo97.githubuserlist;

import com.github.yamo97.githubuserlist.app.AppController;
import com.github.yamo97.githubuserlist.model.GitHubUser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ArrayList<GitHubUser> userList = new ArrayList<>();

    ProgressBar pb;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    public String API_URL = "https://api.github.com/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) findViewById(R.id.indeterminateProgressBar);
        recyclerView = findViewById(R.id.userRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UserListAdapter(userList);
        recyclerView.setAdapter(adapter);

        pb.setVisibility(ProgressBar.VISIBLE);

        // Creating volley request obj
        JsonArrayRequest gitHubUserJsonRequest = new JsonArrayRequest(API_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parse JSON
                            Gson gson = new Gson();

                            ArrayList<GitHubUser> jsonArray = gson.fromJson(response.toString(),
                                    new TypeToken<ArrayList<GitHubUser>>() {}.getType());

                            userList.addAll(jsonArray);

                            for (int i = 0; i < userList.size(); i++) {
                                Log.d(TAG, userList.get(i).getLogin());
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "Error in Parsing JSON Response");
                            e.printStackTrace();
                        }

                        // Hide Progress Bar
                        pb.setVisibility(ProgressBar.INVISIBLE);

                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Volley Error: " + error.getMessage());

                        // Hide Progress Bar
                        pb.setVisibility(ProgressBar.INVISIBLE);
                    }
                });

        // Add the request to the RequestQueue.
        AppController.getInstance().addToRequestQueue(gitHubUserJsonRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pb.setVisibility(ProgressBar.INVISIBLE);
    }
}
