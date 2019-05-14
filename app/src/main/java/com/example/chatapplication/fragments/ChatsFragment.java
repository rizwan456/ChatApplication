package com.example.chatapplication.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapplication.Model.Chat;
import com.example.chatapplication.Model.User;
import com.example.chatapplication.R;
import com.example.chatapplication.adapters.UserAdapter;
import com.example.chatapplication.databinding.FragmentChatsBinding;
import com.example.chatapplication.notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<User> musers;
    FirebaseUser fuser;
    DatabaseReference reference;
    List<String> userList;

    FragmentChatsBinding chatsBinding;


    public ChatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatsFragment newInstance(String param1, String param2) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false);
        setUp();
        return chatsBinding.getRoot();
    }

    LinearLayoutManager mLayoutManager;
    private void setUp() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot sanpshot : dataSnapshot.getChildren()) {
                    Chat chat = sanpshot.getValue(Chat.class);

                    if (chat.getSender().equals(fuser.getUid())) {
                        userList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(fuser.getUid())) {
                        userList.add(chat.getSender());
                    }
                }
                readChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void readChats() {
        musers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        //reference.orderByChild("Chats")
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    for (String id : userList) {
                        if (user.getId().equals(id)) {
                            if (musers.size() != 0) {
                                for (User user1 : new ArrayList<User>(musers)) {
                                    if (!user.getId().equals(user1.getId())) {
                                        musers.remove(user);
                                        musers.add(user);
                                    }
                                }
                            } else {
                                musers.add(user);
                            }
                        }
                    }

                    mLayoutManager.setReverseLayout(true);
                    mLayoutManager.setStackFromEnd(true);
                    chatsBinding.recyclerView.setLayoutManager(mLayoutManager);
                    /*Collections.sort(musers, new Comparator<User>() {
                        @Override
                        public int compare(User o1, User o2) {

                            return 0;
                        }
                    });*/
                    chatsBinding.recyclerView.setAdapter(new UserAdapter(getContext(), musers,0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }


    private void updateToken(String token){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }
}
