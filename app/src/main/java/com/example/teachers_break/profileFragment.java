package com.example.teachers_break;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class profileFragment extends Fragment {


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private TextView tv_userName,tv_email;
    private String uid;
    private Button btn_logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        tv_userName=view.findViewById(R.id.tv_userName);
        tv_email=view.findViewById(R.id.tv_email);

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        mUser=mAuth.getCurrentUser();
        uid=mUser.getUid();

        btn_logout=view.findViewById(R.id.btn_logout);

        db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if (doc.exists()){
                        tv_userName.setText(doc.get("name").toString());
                        tv_email.setText(doc.get("email").toString());

                    }else {
                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),login.class));
            }
        });

        return view;
    }
}