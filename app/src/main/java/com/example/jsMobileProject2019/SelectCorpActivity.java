package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import android.widget.Button;

public class SelectCorpActivity extends AppCompatActivity {
    Intent intent;
    Button btn;
    UserData user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_corp_page);
        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");
        btn = findViewById(R.id.corpBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SelectCorpActivity.this, SelectDepartActivity.class);
                intent.putExtra("corp",btn.getText());
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
    }
}
