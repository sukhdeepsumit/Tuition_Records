package com.example.tuitionrecords.Chats.Message;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.Chats.Model.ChatShowModel;
import com.example.tuitionrecords.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

        if(position==myChats.size()-1)
        {
            if(chatModel.isIsseen())
            {
                holder.text_seen.setText("Seen");
            }
            else {
                holder.text_seen.setText("Delivered");
            }
        }
        else {
            holder.text_seen.setVisibility(View.GONE);
        }

        holder.messageLayout.setOnClickListener(view -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(myContext);
            builder.setTitle("Delete?");
            builder.setCancelable(false);
            builder.setMessage("Unsend message?");
            builder.setPositiveButton("Yes", (dialog, which) -> deleteMessage(position));
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void deleteMessage(int position) {
        String message=myChats.get(position).getMessage();
        String myUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Chats");
        Query query=ref.orderByChild("message").equalTo(message);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren())
                {
                    if(snap.child("sender").getValue().equals(myUid))
                    {
                        snap.getRef().removeValue();
                        Toast.makeText(myContext,"Message unsent",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(myContext,"You can unsend only your message",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
    }

    @Override
    public int getItemCount() {
        return myChats.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView showMessage;
        public CircleImageView profileImage;
        public TextView text_seen;
        public RelativeLayout messageLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.show_message);
            profileImage = itemView.findViewById(R.id.profileImage);
            text_seen=itemView.findViewById(R.id.textSeen);
            messageLayout=itemView.findViewById(R.id.messageLayout);
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
