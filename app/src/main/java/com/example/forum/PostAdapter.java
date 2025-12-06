package com.example.forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public interface OnPostInteractionListener {
        void onLikeClicked(ForumPost post, int position);
        void onCommentClicked(ForumPost post);
    }

    private final List<ForumPost> postsList;
    private final OnPostInteractionListener listener;
    private final FirebaseForumService firebaseService;
    private final String currentUserId = "user123";

    public PostAdapter(List<ForumPost> postsList, OnPostInteractionListener listener, FirebaseForumService firebaseService) {
        this.postsList = postsList;
        this.listener = listener;
        this.firebaseService = firebaseService;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ForumPost post = postsList.get(position);
        Context context = holder.itemView.getContext();

        holder.ivProfilePic.setImageResource(R.drawable.ic_profile_placeholder);
        holder.tvPosterName.setText(post.getPosterName());
        holder.tvPostType.setText(post.getPostType());
        holder.tvTitle.setText(post.getTitle());
        holder.tvMessage.setText(post.getMessage());
        holder.tvUpvoteCount.setText(String.valueOf(post.getUpvotes()));
        holder.tvCommentCount.setText(String.valueOf(post.getCommentCount()));

        switch (post.getPostType()) {
            case "Review":
                holder.tvPostType.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_review));
                break;
            case "Event":
                holder.tvPostType.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_event));
                break;
            default:
                holder.tvPostType.setBackground(ContextCompat.getDrawable(context, R.drawable.badge_discussion));
                break;
        }

        if (post.getTimestamp() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            holder.tvTimestamp.setText(sdf.format(post.getTimestamp()));
        } else {
            holder.tvTimestamp.setText("");
        }

        firebaseService.getLikes(post.getPostId(), likes -> {
            boolean likedByCurrentUser = false;
            for (Like like : likes) {
                if (like.getUserId().equals(currentUserId)) {
                    likedByCurrentUser = true;
                    break;
                }
            }
            holder.ivLike.setImageResource(likedByCurrentUser ? R.drawable.ic_like_filled : R.drawable.ic_like_outline);
        });

        holder.likeLayout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLikeClicked(post, position);
            }
        });

        holder.commentLayout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCommentClicked(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPosterName, tvPostType, tvTitle, tvMessage, tvTimestamp, tvUpvoteCount, tvCommentCount;
        ImageView ivLike, ivProfilePic;
        LinearLayout likeLayout, commentLayout;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvPosterName = itemView.findViewById(R.id.tvPosterName);
            tvPostType = itemView.findViewById(R.id.tvPostType);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvUpvoteCount = itemView.findViewById(R.id.tvUpvoteCount);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            ivLike = itemView.findViewById(R.id.ivLike);
            likeLayout = itemView.findViewById(R.id.likeLayout);
            commentLayout = itemView.findViewById(R.id.commentLayout);
        }
    }
}
