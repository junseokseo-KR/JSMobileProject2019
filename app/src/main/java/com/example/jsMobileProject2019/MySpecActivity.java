package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MySpecActivity extends AppCompatActivity {
    Intent intent;
    TextView viewEmail, viewName, viewGrade, viewToeic;
    UserData user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spec_page);
        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");

        viewEmail = findViewById(R.id.viewEMAIL);
        viewName = findViewById(R.id.viewNAME);
        viewGrade = findViewById(R.id.viewGRADE);
        viewToeic = findViewById(R.id.viewTOEIC);

        viewEmail.setText(user.getEmail());
        viewName.setText(user.getName());
        viewGrade.setText("" + user.getGrade());
        viewToeic.setText("" + user.getToeic());
    }
}