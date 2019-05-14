package com.example.chatapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapplication.MessageActivity;
import com.example.chatapplication.Model.Chat;
import com.example.chatapplication.Model.User;
import com.example.chatapplication.R;
import com.example.chatapplication.databinding.UserItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    UserItemBinding userItemBinding;

    Context context;
    List<User> user;
    String theLastmsg;
    String time;
    int i;

    public UserAdapter(Context context, List<User> user,int i) {
        this.context = context;
        this.user = user;
        this.i=i;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        userItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.user_item, viewGroup, false);
        return new UserItemViewHolder(userItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof UserItemViewHolder) {
            UserItemViewHolder userItemViewHolder = (UserItemViewHolder) viewHolder;
            userItemViewHolder.bindData(i);
            userItemViewHolder.lastMessage(i);
        }
    }

    @Override
    public int getItemCount() {
        return user.size();
    }


    class UserItemViewHolder extends RecyclerView.ViewHolder {
        UserItemBinding userItemBinding;

        public UserItemViewHolder(UserItemBinding userItemBinding) {
            super(userItemBinding.getRoot());
            this.userItemBinding = userItemBinding;
        }

        public void bindData(int position) {
            userItemBinding.userName.setText(user.get(position).getUsername());
            if (user.get(position).getImageURL().equals("default")) {
                userItemBinding.profileImage.setImageResource(R.mipmap.ic_launcher);
            } else {
                userItemBinding.profileImage.setImageURI(Uri.parse(user.get(position).getImageURL()));
            }

            userItemBinding.item.setOnClickListener(v->{
                Intent i=new Intent(context, MessageActivity.class);
                i.putExtra("userid",user.get(position).getId());
                context.startActivity(i);
            });

        }

        public void lastMessage(int position){
            theLastmsg="default";
            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Chat chat=snapshot.getValue(Chat.class);
                        if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(user.get(position).getId())
                                || chat.getReceiver().equals(user.get(position).getId()) && chat.getSender().equals(firebaseUser.getUid())){
                            if(i==1){
                                userItemBinding.timeText.setVisibility(View.GONE);
                                userItemBinding.lastMsg.setVisibility(View.GONE);
                            }else{
                                theLastmsg=chat.getMessage();
                                time=chat.getTime();
                            }
                        }
                    }

                    switch (theLastmsg){
                        case "default":
                            userItemBinding.lastMsg.setText("");
                            userItemBinding.timeText.setText("");
                            break;

                        default:
                            userItemBinding.lastMsg.setText(theLastmsg);
                            userItemBinding.timeText.setText(time.substring(0,5));
                            break;

                    }

                    theLastmsg="default";
                    time="default";
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
