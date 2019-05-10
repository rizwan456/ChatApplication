package com.example.chatapplication.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.chatapplication.Model.Chat;
import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ChatItemLeftBinding;
import com.example.chatapplication.databinding.ChatItemRightBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    ChatItemLeftBinding leftBinding;
    ChatItemRightBinding rightBinding;

    Context context;
    List<Chat> mchat;
    String imageURL;

    FirebaseUser fuser;

    public MessageAdapter(Context context, List<Chat> mchat, String imageURL) {
        this.context = context;
        this.mchat = mchat;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_TYPE_RIGHT) {
            rightBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.chat_item_right, viewGroup, false);
            return new ItemRightHolder(rightBinding);
        } else {
            leftBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.chat_item_left, viewGroup, false);
            return new ItemLeftHolder(leftBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemLeftHolder) {
            ItemLeftHolder itemLeftHolder = (ItemLeftHolder) viewHolder;
            itemLeftHolder.bindData(i);
        } else if (viewHolder instanceof ItemRightHolder) {
            ItemRightHolder rightHolder = (ItemRightHolder) viewHolder;
            rightHolder.bindData(i);
        }
    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    class ItemLeftHolder extends RecyclerView.ViewHolder {
        ChatItemLeftBinding leftBinding;

        public ItemLeftHolder(ChatItemLeftBinding leftBinding) {
            super(leftBinding.getRoot());
            this.leftBinding = leftBinding;
        }

        public void bindData(int position) {
            if (imageURL.equals("default")) {
                leftBinding.image.setImageResource(R.mipmap.ic_launcher);
            } else {
                leftBinding.image.setImageURI(Uri.parse(imageURL));
            }
            leftBinding.rMessage.setText(mchat.get(position).getMessage());
        }
    }

    class ItemRightHolder extends RecyclerView.ViewHolder {
        ChatItemRightBinding rightBinding;

        public ItemRightHolder(ChatItemRightBinding rightBinding) {
            super(rightBinding.getRoot());
            this.rightBinding = rightBinding;
        }

        public void bindData(int position) {
            rightBinding.sMessage.setText(mchat.get(position).getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mchat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
