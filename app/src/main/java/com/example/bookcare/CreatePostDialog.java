package com.example.bookcare;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CreatePostDialog extends DialogFragment {

    private CreatePostListener createPostListener;

    // Interface to handle post creation callbacks
    public interface CreatePostListener {
        void onCreatePost(String title, String message);
    }

    @Override
    public void onAttach(@NonNull android.content.Context context) {
        super.onAttach(context);
        try {
            createPostListener = (CreatePostListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement CreatePostListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_post, null);

        final EditText etTitle = view.findViewById(R.id.etPostTitle);
        final EditText etMessage = view.findViewById(R.id.etPostMessage);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view)
                .setTitle("Create New Post")
                .setPositiveButton("Create", (dialog, id) -> {
                    String title = etTitle.getText().toString();
                    String message = etMessage.getText().toString();

                    if (title.trim().isEmpty() || message.trim().isEmpty()) {
                        showValidationError();
                        return;
                    }

                    if (createPostListener != null) {
                        createPostListener.onCreatePost(title, message);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // Dialog will be automatically dismissed
                });

        return builder.create();
    }

    private void showValidationError() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Validation Error")
                .setMessage("Please fill in both title and message fields.")
                .setPositiveButton("OK", null)
                .show();
    }
}