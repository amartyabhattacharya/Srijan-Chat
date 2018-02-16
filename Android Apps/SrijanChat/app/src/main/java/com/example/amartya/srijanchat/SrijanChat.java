package com.example.amartya.srijanchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SrijanChat extends AppCompatActivity {

    //int a;
    Button loginButton, signupButton;
    TextView tv;
    EditText userName, passWord, groupName;
    String sUserName, sPassWord, sGroupName;
    boolean validGroup, validUser, validPass;
    DatabaseReference gRef, uRef;
    Object value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srijan_chat);


        loginButton = (Button)findViewById(R.id.loginButton);
        signupButton = (Button)findViewById(R.id.signupButton);
        tv = (TextView) findViewById(R.id.title);
        userName = (EditText)findViewById(R.id.userName);
        passWord = (EditText)findViewById(R.id.passWord);
        groupName = (EditText)findViewById(R.id.groupName);



        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SrijanChat.this, SignupPage.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sUserName = userName.getText().toString();
                sPassWord = passWord.getText().toString();
                sGroupName = groupName.getText().toString();


                gRef = FirebaseDatabase.getInstance().getReference();
                gRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(sGroupName)){
                            validGroup = true;
                        }
                        else{
                            Toast.makeText(SrijanChat.this, "Invalid Group Name", Toast.LENGTH_SHORT).show();
                            validGroup = false;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(validGroup){
                    uRef = FirebaseDatabase.getInstance().getReference(sGroupName).child("users");
                    uRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(sUserName)){
                                validUser = true;
                            }
                            else{
                                Toast.makeText(SrijanChat.this, "Invalid User Name", Toast.LENGTH_SHORT).show();
                                validUser = false;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if(validGroup && validUser){
                    uRef = FirebaseDatabase.getInstance().getReference(sGroupName).child("users").child(sUserName);
                    uRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            value = dataSnapshot.getValue(Object.class);
                            //Toast.makeText(SrijanChat.this, value.toString() + "  " + sPassWord, Toast.LENGTH_SHORT).show();
                            if(value.toString().equals(sPassWord)){
                                validPass = true;
                            }
                            else{
                                Toast.makeText(SrijanChat.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                                validPass = false;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                if(validGroup && validUser && validPass) {

                    Toast.makeText(SrijanChat.this, "Hello " + sUserName + ", you have successfully logged into your account.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SrijanChat.this, UserPage.class);
                    intent.putExtra("userName", sUserName);
                    intent.putExtra("groupName", sGroupName);
                    startActivity(intent);
                }
            }
        });

    }
}
