package com.example.libraryproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private EditText txt_email,txt_password,txt_username;
    DatabaseReference db;
    private Context con ;
    private String username, email,password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("sign up");

        txt_username = findViewById(R.id.edusername);
        txt_email = findViewById(R.id.edemail);
        txt_password = findViewById(R.id.edpassword);

        db = FirebaseDatabase.getInstance().getReference("projLib");
        Button signup_btn = findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();            }
        });

    }


    public void Register() {

        username = txt_username.getText().toString().trim();
        email = txt_email.getText().toString().trim().toLowerCase();
        password = txt_password.getText().toString().trim();

        if (username.isEmpty()) {
            txt_username.setError("please enter your username");

            return;
        } else if (email.isEmpty() || !email.matches(emailPattern)) {
            txt_email.setError("please enter your email");

            return;
        } else if (password.isEmpty()) {
            txt_password.setError("please enter your password");

            return;
        }

        checkIfUserIsExists();
    }


    private void checkIfUserIsExists(){
        db.child("USER")
                .child(Login.setEmailAsKey(email))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("Data")){
                            Toast.makeText(getApplicationContext(),"this email is already registered",Toast.LENGTH_SHORT).show();
                        }else {
                            registerUser();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void registerUser(){

        HashMap<String, Object> user_map = new HashMap<>();
        user_map.put("Data",new User(username,email,password));

        db.child("USER")
                .child(Login.setEmailAsKey(email))
                .updateChildren(user_map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"connection error please try again",Toast.LENGTH_SHORT).show();

            }
        }) ;

    }

}
