package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SelectDepartActivity extends AppCompatActivity {
    Intent intent;
    Button btn;
    UserData user;
    String corp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_depart_page);
        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");
        corp = intent.getExtras().getString("corp");

        btn = findViewById(R.id.departBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SelectDepartActivity.this, SpecCompareBarChartActivity.class);
                intent.putExtra("depart",btn.getText());
                intent.putExtra("corp",corp);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
    }
}
