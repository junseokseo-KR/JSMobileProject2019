package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.ornach.nobobutton.NoboButton;

public class WelecomActivity extends AppCompatActivity {
    TextView viewID;
    Intent intent;
    UserData user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_page);

        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");

        viewID = findViewById(R.id.viewID);
        viewID.setText(user.getName());

        NoboButton getDB = findViewById(R.id.specBtn);
        getDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(WelecomActivity.this, MySpecActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }
    //비교바차트 액티비티 이동
    public void moveBarChart(View v){
        intent = new Intent(WelecomActivity.this, SelectCorpActivity.class);
        intent.putExtra("user", user);
        Log.i("인텐드", intent.toString());
        startActivity(intent);
    }
    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        intent = new Intent(WelecomActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
