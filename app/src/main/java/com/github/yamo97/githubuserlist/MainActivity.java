package com.github.yamo97.githubuserlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.yamo97.githubuserlist.app.AppController;
import com.github.yamo97.githubuserlist.model.GitHubUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public String API_URL = "https://api.github.com/users";
    ArrayList<GitHubUser> userList = new ArrayList<>();
    ProgressBar pb;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show Progress Bar
        pb = findViewById(R.id.progress_circle);

        // Initialize Recycler View
        initRecyclerView();

        // Make request for User List
        requestUserList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pb.setVisibility(ProgressBar.INVISIBLE);
    }

    private void requestUserList() {
        // Tag used to cancel the request
        String tag_json_array = "json_array_req";

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
                                    new TypeToken<ArrayList<GitHubUser>>() {
                                    }.getType());

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
        AppController.getInstance().addToRequestQueue(gitHubUserJsonRequest, tag_json_array);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.user_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new UserListAdapter(userList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GitHubUser user = userList.get(recyclerView.getChildLayoutPosition(v));

                Toast.makeText(getApplicationContext(), user.getLogin() + " is selected!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                intent.putExtra("user", user);
                MainActivity.this.startActivity(intent);
            }
        });

        // Add Divider between Items.
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}
