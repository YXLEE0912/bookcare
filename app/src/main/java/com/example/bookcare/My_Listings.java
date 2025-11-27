package com.example.bookcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class My_Listings extends Fragment {

    private ArrayList<Book> books;
    private BookAdapter adapter;

    public My_Listings() { }

    public static My_Listings newInstance(String param1, String param2) {
        My_Listings fragment = new My_Listings();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        books = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my__listings, container, false);

        final TextView tvSubTitle = view.findViewById(R.id.tvSubTitle);
        RecyclerView recyclerView = view.findViewById(R.id.rvBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BookAdapter(books);
        recyclerView.setAdapter(adapter);

        Button btnAddBook = view.findViewById(R.id.btnAddBook);
        btnAddBook.setOnClickListener(v -> {
            int newId = books.size() + 1;
            Book newBook = new Book(
                    "Book " + newId,
                    "Author " + newId,
                    "Available",
                    0,
                    0
            );
            adapter.addBook(newBook);
            tvSubTitle.setText(books.size() + " books listed");
        });

        return view;
    }
}
