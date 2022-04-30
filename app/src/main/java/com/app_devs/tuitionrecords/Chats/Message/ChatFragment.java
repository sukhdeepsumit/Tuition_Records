package com.app_devs.tuitionrecords.Chats.Message;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app_devs.tuitionrecords.Chats.Model.Chat_ID;
import com.app_devs.tuitionrecords.Notifications.Token;
import com.app_devs.tuitionrecords.R;
import com.app_devs.tuitionrecords.StudentActivity.Authentication.StudentModel;
import com.app_devs.tuitionrecords.TeacherActivity.Authentication.TeacherModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

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

    public ChatFragment() {
    }

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()).setStackFromEnd(true));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());

        Log.i("CURRENT_UID", firebaseUser.getUid());

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

                        //Log.i("MODEL_ID_CHECK", "" + model.getId());

                        for (Chat_ID id : userList) {

                            //Log.i("ID_CHECK", id.getId());

                            List<String> list = new LinkedList<>();
                            CollectionUtils.addIgnoreNull(list, model.getId());
                            if (!list.isEmpty()) {
                                if (model.getId().equals(id.getId())) {
                                    Teacher.add(model);
                                }
                            }
                        }
                    }

                    adapter = new ChatUserAdapter(Teacher, ChatFragment.this, who, true);
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
                            List<String> list = new LinkedList<>();
                            CollectionUtils.addIgnoreNull(list, model.getId());
                            if (!list.isEmpty()) {
                               // Log.i("FIRST_ITEM", list.get(0));
                                if (model.getId().equals(id.getId())) {
                                    Student.add(model);
                                }
                            }
                        }
                    }

                    adapter = new ChatUserAdapter(ChatFragment.this, Student, who, true);
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