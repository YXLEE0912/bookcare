package com.example.bookcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class Add_New_Book extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Add_New_Book() {
        // Required empty public constructor
    }

    public static Add_New_Book newInstance(String param1, String param2) {
        Add_New_Book fragment = new Add_New_Book();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add__new__book, container, false);

        // Cards
        ConstraintLayout cardExchange = view.findViewById(R.id.cardExchange);
        ConstraintLayout cardDonate   = view.findViewById(R.id.cardDonate);

        // RadioButtons inside cards
        RadioButton rbExchange = cardExchange.findViewById(R.id.rbMain);
        RadioButton rbDonate   = cardDonate.findViewById(R.id.rbMain);

        // Titles/Subtitles
        TextView tvExchangeTitle    = cardExchange.findViewById(R.id.tvTitle);
        TextView tvExchangeSubtitle = cardExchange.findViewById(R.id.tvSubtitle);
        TextView tvDonateTitle      = cardDonate.findViewById(R.id.tvTitle);
        TextView tvDonateSubtitle   = cardDonate.findViewById(R.id.tvSubtitle);

        tvExchangeTitle.setText("Exchange");
        tvExchangeSubtitle.setText("Trade with another book");

        tvDonateTitle.setText("Donate");
        tvDonateSubtitle.setText("Give away for free");

        // Helper to select one and unselect the other
        View.OnClickListener selectExchange = v -> {
            rbExchange.setChecked(true);
            rbDonate.setChecked(false);
        };

        View.OnClickListener selectDonate = v -> {
            rbDonate.setChecked(true);
            rbExchange.setChecked(false);
        };

        // Click on card or radio both work
        cardExchange.setOnClickListener(selectExchange);
        rbExchange.setOnClickListener(selectExchange);

        cardDonate.setOnClickListener(selectDonate);
        rbDonate.setOnClickListener(selectDonate);

        // Default selection
        rbExchange.setChecked(true);

        // --- SPINNER SETUP ---
        Spinner spinner = view.findViewById(R.id.SPGenre);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),       // instead of "this"
                R.array.book_genre,
                R.layout.spinner_item   // custom layout for small font
        );

        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        // --- SPINNER 2 SETUP ---
        Spinner spinner2 = view.findViewById(R.id.SPCondition);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.condition_type,   // your second array
                R.layout.spinner_item      // same small-font layout
        );

        adapter2.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(adapter2);




        return view;
    }
}