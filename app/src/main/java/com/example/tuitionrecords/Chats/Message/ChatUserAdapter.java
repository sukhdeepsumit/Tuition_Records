package com.example.tuitionrecords.Chats.Message;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.Chats.Model.ChatShowModel;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.Authentication.StudentModel;
import com.example.tuitionrecords.TeacherActivity.Authentication.TeacherModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder> {

    private final Context context;

    List<TeacherModel> teacher_chat;
    List<StudentModel> student_chat;

    String who;
    boolean isChat;

    String theLastMessage;

    public ChatUserAdapter(List<StudentModel> student_chat, Context context, String who, boolean isChat) {
        this.context = context;
        this.student_chat = student_chat;
        this.who = who;
        this.isChat = isChat;
    }

    public ChatUserAdapter(Context context, List<TeacherModel> teacher_chat, String who, boolean isChat) {
        this.context = context;
        this.teacher_chat = teacher_chat;
        this.who = who;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (who.equals("student")) {
            TeacherModel teacherModel = teacher_chat.get(position);

            holder.name.setText(teacherModel.getName());
            if (teacherModel.getMyUri().equals("default")) {
                holder.profilePic.setImageResource(R.drawable.anonymous_user);
            }
            else {
                Glide.with(context).load(teacherModel.getMyUri()).into(holder.profilePic);
            }

            if (isChat) {
                lastMessage(teacherModel.getId(), holder.lastMessage);
            }
            else {
                holder.lastMessage.setVisibility(View.GONE);
            }

            if (isChat) {
                if (teacherModel.getStatus() != null && teacherModel.getStatus().equals("online")) {
                    holder.online.setVisibility(View.VISIBLE);
                    holder.offline.setVisibility(View.GONE);
                }
                else {
                    holder.offline.setVisibility(View.VISIBLE);
                    holder.online.setVisibility(View.GONE);
                }
            }
            else {
                holder.online.setVisibility(View.GONE);
                holder.offline.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userId", teacherModel.getId());
                intent.putExtra("user", who);
                context.startActivity(intent);
            });
        }
        else {
            StudentModel studentModel = student_chat.get(position);

            //Log.i("ITEM_ONE", studentModel.getId());

            holder.name.setText(studentModel.getName());
            if (studentModel.getMyUri().equals("default")) {
                holder.profilePic.setImageResource(R.drawable.anonymous_user);
            }
            else {
                Glide.with(context).load(studentModel.getMyUri()).into(holder.profilePic);
            }

            if (isChat) {
                lastMessage(studentModel.getId(), holder.lastMessage);
            }
            else {
                holder.lastMessage.setVisibility(View.GONE);
            }

            if (isChat) {
                if (studentModel.getStatus() != null && studentModel.getStatus().equals("online")) {
                    holder.online.setVisibility(View.VISIBLE);
                    holder.offline.setVisibility(View.GONE);
                }
                else {
                    holder.offline.setVisibility(View.VISIBLE);
                    holder.online.setVisibility(View.GONE);
                }
            }
            else {
                holder.online.setVisibility(View.GONE);
                holder.offline.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userId", studentModel.getId());
                intent.putExtra("user", who);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (who.equals("student")) {
            return teacher_chat.size();
        }
        return student_chat.size();
    }

    private void lastMessage(String id, TextView lastMessage) {
        theLastMessage = "default";

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatShowModel chats = dataSnapshot.getValue(ChatShowModel.class);

                    if (chats.getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(id) ||
                         chats.getReceiver().equals(id) && chats.getSender().equals(firebaseUser.getUid()))
                    {
                        theLastMessage = chats.getMessage();
                    }
                }

                switch (theLastMessage) {
                    case "default" : lastMessage.setText("No Message");
                    break;

                    default: lastMessage.setText(theLastMessage);
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePic, online, offline;
        TextView name, lastMessage;

        public ViewHolder(@NonNull View itemView) {
                super(itemView);
                profilePic=itemView.findViewById(R.id.profile_image);
                name=itemView.findViewById(R.id.username);
                lastMessage = itemView.findViewById(R.id.last_msg);

                online = itemView.findViewById(R.id.img_on);
                offline = itemView.findViewById(R.id.img_off);
        }
    }
}
