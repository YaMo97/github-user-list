package com.github.yamo97.githubuserlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.yamo97.githubuserlist.model.GitHubUser;

public class UserDetailActivity extends AppCompatActivity {
    private static final String TAG = UserDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Intent intent = getIntent();

        GitHubUser user = (GitHubUser) intent.getSerializableExtra("user");

        Log.d(TAG, user.getLogin());
    }
}
