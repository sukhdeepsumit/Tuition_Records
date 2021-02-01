package com.example.tuitionrecords.Chats.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.Chats.Model.ChatShowModel;
import com.example.tuitionrecords.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private final Context myContext;
    private final List<ChatShowModel> myChats;
    private final String imageUrl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context myContext, List<ChatShowModel> myChats, String imageUrl) {
        this.myContext = myContext;
        this.myChats = myChats;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_LEFT) {
            view = LayoutInflater.from(myContext).inflate(R.layout.chat_item_left, parent, false);
        }
        else {
            view = LayoutInflater.from(myContext).inflate(R.layout.chat_item_right, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatShowModel chatModel = myChats.get(position);

        holder.showMessage.setText(chatModel.getMessage());
        Glide.with(myContext).load(imageUrl).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return myChats.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView showMessage;
        public CircleImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.show_message);
            profileImage = itemView.findViewById(R.id.profileImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (myChats.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        }
        return MSG_TYPE_LEFT;
    }
}
