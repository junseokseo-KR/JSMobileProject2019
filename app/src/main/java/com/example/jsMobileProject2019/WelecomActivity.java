package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelecomActivity extends AppCompatActivity {
    TextView viewID;
    Intent intent;
    String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_page);

        intent = getIntent();
        userID = intent.getExtras().getString("userID");
        viewID = findViewById(R.id.viewID);
        viewID.setText(userID);

        Button getDB = findViewById(R.id.specBtn);
        getDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(WelecomActivity.this, MySpecActivity.class);
                intent.putExtra("email", userID);
                startActivity(intent);
            }
        });
    }

    public void moveRegist(View v){
        intent = new Intent(WelecomActivity.this, SpecRegistActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void logOut(View view){
        finish();
    }
}
