package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import android.widget.AdapterView;
import android.widget.Button;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectCorpActivity extends AppCompatActivity {
    Intent intent;
    SmartMaterialSpinner corpSpinner, departSpinner;
    Button btn;
    UserData user;
    List<String> list,list2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_corp_page);
        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");
//        initSpinner();
    }

//    protected void initSpinner(){
//        corpSpinner = findViewById(R.id.corpSpinner);
//        departSpinner = findViewById(R.id.departSpinner);
//
//        list = Arrays.asList(getResources().getStringArray(R.array.corp_name));
//        corpSpinner.setItems(list);
//        corpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (String.valueOf(corpSpinner.getItemAtPosition(position)).equals("NAVER")){
//                    list2 = Arrays.asList(getResources().getStringArray(R.array.naver_depart));
//                    departSpinner.setItems(list2);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
}
