package com.example.amartya.srijanchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupPage extends AppCompatActivity {

    EditText groupName, userName, passWord;
    Button tncButton, registerButton;
    CheckBox tnc;
    String sGroupName, sUserName, sPassWord;
    DatabaseReference  userRef;
    boolean DNE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        Intent intent = getIntent();
        setTitle("Sign Up");
        groupName = (EditText) findViewById(R.id.groupName);
        userName = (EditText) findViewById(R.id.userName);
        passWord = (EditText) findViewById(R.id.passWord);
        tncButton = (Button) findViewById(R.id.tncButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        tnc = (CheckBox) findViewById(R.id.tnc);

        tncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignupPage.this, "I won't spam.", Toast.LENGTH_SHORT).show();
            }
        });



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sGroupName = groupName.getText().toString();
                sUserName = userName.getText().toString();
                sPassWord = passWord.getText().toString();


                userRef = FirebaseDatabase.getInstance().getReference().child(sGroupName).child("users");
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(sUserName)){
                            DNE = false;
                        }
                        else{
                            DNE = true;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if(DNE){
                    if(tnc.isChecked()) {
                        userRef.child(sUserName).setValue(sPassWord);
                        Toast.makeText(SignupPage.this, "You have successfully registered to the group " + sGroupName + " with username " + sUserName + ". Please go back and sign in to start chat.", 7000).show();
                    }
                    else{
                        Toast.makeText(SignupPage.this, "Agree to terms and conditions first.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SignupPage.this, "Username already exists.", 5000).show();
                }


            }
        });
    }
}
