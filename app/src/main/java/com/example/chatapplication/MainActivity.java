package com.example.chatapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.example.chatapplication.Model.User;
import com.example.chatapplication.databinding.ActivityMainBinding;
import com.example.chatapplication.fragments.ChatsFragment;
import com.example.chatapplication.fragments.UsersFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setUp();
    }

    private void setUp() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) return;
                mainBinding.userName.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    mainBinding.avtarImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    mainBinding.avtarImage.setImageURI(Uri.parse(user.getImageURL()));
                    //mainBinding.avtarImage.setImageURI(Uri.parse("https://www.geek.com/wp-content/uploads/2019/04/iron-man-endgame-625x352.png"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mainBinding.logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, StartActivity.class));
        });

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(ChatsFragment.newInstance(null, null), "Chats");
        viewPagerAdapter.addFragment(UsersFragment.newInstance(null, null), "Users");

        mainBinding.viewPager.setAdapter(viewPagerAdapter);
        mainBinding.tabLayout.setupWithViewPager(mainBinding.viewPager);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
