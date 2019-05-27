package com.example.jsMobileProject2019;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.ornach.nobobutton.NoboButton;

public class WelecomActivity extends AppCompatActivity {
    TextView viewID,viewSchool,myGrade,myToeic,myToeicS,myAward,myIntern,myOversea,myLicense,myOpic;
    Intent intent;
    UserData user;
    ImageView profileImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_page);
        profileImg = findViewById(R.id.profileImg);
        myGrade = findViewById(R.id.myGrade);
        myToeic = findViewById(R.id.myToeic);
        myToeicS = findViewById(R.id.myToeicS);
        myAward = findViewById(R.id.myAward);
        myIntern = findViewById(R.id.myIntern);
        myOversea = findViewById(R.id.myOversea);
        myLicense = findViewById(R.id.myLicense);
        myOpic = findViewById(R.id.myOpic);

        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");
        if (user.getSex().equals("남자")){
            profileImg.setImageResource(R.drawable.student_boy);
        }
        else if (user.getSex().equals("여자")){
            profileImg.setImageResource(R.drawable.student_girl);
        }
        myGrade.setText((Math.round(user.getGrade()*100)/100.0)+"");
        myToeic.setText(Long.toString(user.getToeic()));
        myToeicS.setText(user.getToeicSpeaking());
        myAward.setText((int) user.getAward()+"회");
        myIntern.setText((int)user.getIntern()+"회");
        myOversea.setText((int)user.getOverseas()+"회");
        myLicense.setText((int)user.getLicense()+"개");
        myOpic.setText(user.getOpic());

        viewID = findViewById(R.id.viewID);
        viewID.setText(user.getName());
        viewSchool = findViewById(R.id.viewSchool);
        viewSchool.setText(user.getCollege());
    }
    //비교바차트 액티비티 이동
    public void moveBarChart(View v){
        intent = new Intent(WelecomActivity.this, SelectCorpActivity.class);
        intent.putExtra("user", user);
        Log.i("인텐드", intent.toString());
        startActivity(intent);
    }
    public void moveModifyProfile(View v){
        intent = new Intent(WelecomActivity.this, SpecModifyActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        intent = new Intent(WelecomActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
