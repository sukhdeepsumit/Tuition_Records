package com.example.tuitionrecords.Chats.Users;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tuitionrecords.Chats.Chat;
import com.example.tuitionrecords.Chats.Users.UsersShowAdapter;
import com.example.tuitionrecords.Chats.Users.UsersShowModel;
import com.example.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class UsersFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference reference;
    UsersShowAdapter adapter;
    String who;


    public UsersFragment() {
        // Required empty public constructor
    }

    public UsersFragment(String role)
    {
        who=role;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Accepted_Students").child(currentUser);

        FirebaseRecyclerOptions<UsersShowModel> options =
                new FirebaseRecyclerOptions.Builder<UsersShowModel>()
                        .setQuery(reference,UsersShowModel.class)
                        .build();

        adapter=new UsersShowAdapter(options, getApplicationContext(), who);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}