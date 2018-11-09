package com.github.yamo97.githubuserlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.yamo97.githubuserlist.model.GitHubUser;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private ArrayList<GitHubUser> userDataSet;

    public UserListAdapter(ArrayList<GitHubUser> dataSet) {
        this.userDataSet = dataSet;
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
        // - replace the contents of the view with that element
        GitHubUser user = userDataSet.get(position);
        viewHolder.name.setText(user.getLogin());
    }

    // Return the size of your dataSet (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return userDataSet.size();
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public UserListViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
        }
    }
}
