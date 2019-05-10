package com.example.chatapplication.notification;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();

        String refreshTocken= FirebaseInstanceId.getInstance().getToken();

        if(fuser != null){
            updateToken(refreshTocken);
        }
    }

    private void updateToken(String refreshTocken) {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token=new Token(refreshTocken);
        reference.child(firebaseUser.getUid()).setValue(token);
    }
}
