package com.example.amartya.srijanchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserPage extends AppCompatActivity {

    ImageView send;
    TextView msg;
    String sMsg;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;
    DatabaseReference myRef, noticeRef;
    ListView listView;
    String sUserName, sGroupName, sNotice;
    EditText notice;
    Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        Intent intent = getIntent();
        sUserName = intent.getStringExtra("userName");
        sGroupName = intent.getStringExtra("groupName");
        setTitle(sGroupName + " : " + sUserName);
        notice = (EditText)findViewById(R.id.notice);
        postButton = (Button)findViewById(R.id.post);

        noticeRef = FirebaseDatabase.getInstance().getReference(sGroupName).child("notice");
        noticeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sNotice = dataSnapshot.getValue(String.class);
                notice.setHint(sNotice);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        postButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeRef.setValue(notice.getText().toString());
                noticeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sNotice= dataSnapshot.getValue(String.class);
                        notice.setText("");
                        notice.setHint(sNotice);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView = (ListView) findViewById(R.id.messagesList);
        listView.setAdapter(adapter);

        myRef = FirebaseDatabase.getInstance().getReference(sGroupName).child("messages");
        msg = (TextView) findViewById(R.id.msg);

        send = (ImageView) findViewById(R.id.send);
        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sMsg = msg.getText().toString();
                if (!sMsg.isEmpty()) {
                    msg.setText("");
                    DatabaseReference msgRef = myRef.push();
                    msgRef.setValue(sUserName + " : " + sMsg);
                }
                else{
                    Toast.makeText(UserPage.this, "Message can't be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                list.add(dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
