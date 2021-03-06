package com.github.yamo97.githubuserlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.github.yamo97.githubuserlist.app.AppController;
import com.github.yamo97.githubuserlist.model.GitHubUser;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {
    private static final String TAG = "UserListAdapter";

    private ArrayList<GitHubUser> userDataSet;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private View.OnClickListener onClickListener;

    public UserListAdapter(ArrayList<GitHubUser> dataSet, View.OnClickListener onClickListener) {
        this.userDataSet = dataSet;
        this.onClickListener = onClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserListAdapter.UserListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);

        return new UserListViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(UserListViewHolder viewHolder, int position) {
        // - get element from your dataSet at this position
        GitHubUser user = userDataSet.get(position);

        // - set onClickListener on itemView
        viewHolder.itemView.setOnClickListener(onClickListener);

        // - replace the contents of the view with that element
        viewHolder.name.setText(user.getLogin());
        viewHolder.thumbnail.setImageUrl(user.getAvatar_url(), imageLoader);

    }

    // Return the size of your dataSet (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return userDataSet.size();
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public NetworkImageView thumbnail;

        public UserListViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.user_list_name);
            thumbnail = v.findViewById(R.id.user_list_thumbnail);
        }
    }
}
