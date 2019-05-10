package com.example.chatapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.chatapplication.MessageActivity;
import com.example.chatapplication.Model.User;
import com.example.chatapplication.R;
import com.example.chatapplication.databinding.UserItemBinding;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    UserItemBinding userItemBinding;

    Context context;
    List<User> user;

    public UserAdapter(Context context, List<User> user) {
        this.context = context;
        this.user = user;
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
    }
}
