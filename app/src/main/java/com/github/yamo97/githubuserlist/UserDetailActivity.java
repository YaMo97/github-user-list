package com.github.yamo97.githubuserlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.github.yamo97.githubuserlist.app.AppController;
import com.github.yamo97.githubuserlist.model.GitHubUser;
import com.google.gson.Gson;

import org.json.JSONObject;

public class UserDetailActivity extends AppCompatActivity {
    private static final String TAG = UserDetailActivity.class.getSimpleName();

    public String API_URL = "https://api.github.com/users";

    NetworkImageView thumbnail;
    ProgressBar pb;
    TextView name;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private GitHubUser userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        pb = findViewById(R.id.user_details_progress_circle);
        name = findViewById(R.id.user_details_name);
        thumbnail = findViewById(R.id.user_details_thumbnail);

        Intent intent = getIntent();
        GitHubUser user = (GitHubUser) intent.getSerializableExtra("user");

        requestUserData(user);
    }

    private void requestUserData(GitHubUser user) {
        // Tag used to cancel the request
        String tag_user_data_req = "user_data_req";

        // Creating volley request obj

        JsonObjectRequest userDataRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_URL + "/" + user.getLogin(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        userDetails = new Gson().fromJson(response.toString(), GitHubUser.class);
                        name.setText(userDetails.getLogin());
                        thumbnail.setImageUrl(userDetails.getAvatar_url(), imageLoader);

                        // Hide Progress Bar
                        pb.setVisibility(ProgressBar.INVISIBLE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Volley Error: " + error.getMessage());

                // Hide Progress Bar
                pb.setVisibility(ProgressBar.INVISIBLE);

            }
        });

        AppController.getInstance().addToRequestQueue(userDataRequest, tag_user_data_req);
    }
}
