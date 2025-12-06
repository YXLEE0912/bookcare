package com.example.forum;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CommentDialogFragment extends DialogFragment {

    public interface OnCommentAddedListener {
        void onCommentAdded(String postId, String commentText);
    }

    private OnCommentAddedListener listener;
    private static final String ARG_POST_ID = "post_id";
    private String postId;
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();
    private FirebaseForumService firebaseService;

    public static CommentDialogFragment newInstance(String postId) {
        CommentDialogFragment fragment = new CommentDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnCommentAddedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCommentAddedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            postId = getArguments().getString(ARG_POST_ID);
        }
        firebaseService = new FirebaseForumService(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_comments, null);

        rvComments = view.findViewById(R.id.rvComments);
        commentAdapter = new CommentAdapter(commentList);
        rvComments.setAdapter(commentAdapter);

        EditText etNewComment = view.findViewById(R.id.etNewComment);
        ImageButton btnSendComment = view.findViewById(R.id.btnSendComment);

        btnSendComment.setOnClickListener(v -> {
            String commentText = etNewComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                if (listener != null) {
                    listener.onCommentAdded(postId, commentText);
                }
                etNewComment.setText("");
            }
        });

        loadComments();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton("Close", (dialog, which) -> dismiss())
                .create();
    }

    private void loadComments() {
        firebaseService.getComments(postId, comments -> {
            commentList.clear();
            commentList.addAll(comments);
            commentAdapter.notifyDataSetChanged();
        });
    }
}
