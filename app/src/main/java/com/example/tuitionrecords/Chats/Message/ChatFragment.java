package com.example.tuitionrecords.Chats.Message;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tuitionrecords.Chats.Model.Chat_ID;
import com.example.tuitionrecords.Chats.Users.UsersShowAdapter;
import com.example.tuitionrecords.Notifications.Token;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.Authentication.StudentModel;
import com.example.tuitionrecords.StudentActivity.Request.TeacherShowModel;
import com.example.tuitionrecords.TeacherActivity.Authentication.TeacherModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//commit check

public class ChatFragment extends Fragment {

    RecyclerView recyclerView;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    List<StudentModel> Teacher;
    List<TeacherModel> Student;
    List<Chat_ID> userList;

    ChatUserAdapter adapter;
    String who;

    /*public ChatFragment() {
    }*/

    public ChatFragment(String who) {
        this.who = who;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat_ID chatList = dataSnapshot.getValue(Chat_ID.class);
                    userList.add(chatList);
                }
                chatListId();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }

    private void chatListId() {
        Student = new ArrayList<>();
        Teacher = new ArrayList<>();

        if (who.equals("teacher")) {
            reference = FirebaseDatabase.getInstance().getReference("Students_Profile");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Teacher.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        StudentModel model = dataSnapshot.getValue(StudentModel.class);
                        for (Chat_ID id : userList) {
                            if (model.getId().equals(id.getId())) {
                                Teacher.add(model);
                            }
                        }
                    }

                    adapter = new ChatUserAdapter(Teacher, getContext(), who, true);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {  }
            });
        }
        else {
            reference = FirebaseDatabase.getInstance().getReference("Teacher_profile");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Student.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        TeacherModel model = dataSnapshot.getValue(TeacherModel.class);
                        for (Chat_ID id : userList) {
                            assert model != null;
                            if (model.getId().equals(id.getId())) {
                                Student.add(model);
                            }
                        }
                    }

                    adapter = new ChatUserAdapter(getContext(), Student, who, true);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {  }
            });
        }
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token tk = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(tk);
    }
}