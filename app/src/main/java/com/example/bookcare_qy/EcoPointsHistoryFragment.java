package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EcoPointsHistoryFragment extends Fragment {

    private PointTransactionAdapter transactionAdapter;
    private List<PointTransaction> transactionList = new ArrayList<>();
    private DatabaseReference transactionsRef;
    private ValueEventListener valueEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eco_points_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvHistory = view.findViewById(R.id.historyRecyclerView);
        transactionAdapter = new PointTransactionAdapter(transactionList);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistory.setAdapter(transactionAdapter);

        ImageView btnBack = view.findViewById(R.id.backButton);
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Load transaction history from Firebase
        loadTransactionHistory();
    }

    private void loadTransactionHistory() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;
        
        transactionsRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                .getReference(Constants.PATH_POINT_TRANSACTIONS);
        
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionList.clear();
                String userId = currentUser.getUid();
                
                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    PointTransaction transaction = transactionSnapshot.getValue(PointTransaction.class);
                    if (transaction != null && userId.equals(transaction.getUserId())) {
                        // Only show transactions related to books (Exchange/Donation)
                        String type = transaction.getType();
                        if ("Exchange".equalsIgnoreCase(type) || "Donation".equalsIgnoreCase(type)) {
                            transactionList.add(transaction);
                        }
                    }
                }
                
                // Sort by timestamp (newest first)
                transactionList.sort((t1, t2) -> {
                    if (t1.getTimestamp() == null || t2.getTimestamp() == null) return 0;
                    return t2.getTimestamp().compareTo(t1.getTimestamp());
                });
                
                transactionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        };
        
        transactionsRef.addValueEventListener(valueEventListener);
    }
    
    @Override
    public void onStop() {
        super.onStop();
        if (transactionsRef != null && valueEventListener != null) {
            transactionsRef.removeEventListener(valueEventListener);
        }
    }
}
