package com.example.bookcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<ForumPost> postsList;

    public PostAdapter(List<ForumPost> postsList) {
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ForumPost post = postsList.get(position);
        holder.tvPostTitle.setText(post.getTitle());
        holder.tvPostMessage.setText(post.getMessage());
        holder.tvPostAuthor.setText("by " + post.getPosterName());
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPostTitle;
        public TextView tvPostMessage;
        public TextView tvPostAuthor;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPostTitle = itemView.findViewById(R.id.tvPostTitle);
            tvPostMessage = itemView.findViewById(R.id.tvPostMessage);
            tvPostAuthor = itemView.findViewById(R.id.tvPostAuthor);
        }
    }
}