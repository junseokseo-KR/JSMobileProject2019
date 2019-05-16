package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SpecRegistActivity extends AppCompatActivity {
    MaterialEditText editGrade,editToeic,editName,editSchool,editLisence,editOversea,editAward,editIntern,editMajor;    //9
    Spinner editToeicSpeaking, editOPIc;    //2
    String name,email,college,major, opic, toeicSpeaking; //6
    long award, license,intern, overseas,toeic;   //5
    double grade;   //1
    FirebaseFirestore db;
    Intent intent;
    UserData user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spec_regist_page);
        intent = getIntent();
        db = FirebaseFirestore.getInstance();
        email = intent.getExtras().getString("email");

        editGrade = findViewById(R.id.editGrade);
        editToeic = findViewById(R.id.editToeic);
        editToeicSpeaking = findViewById(R.id.editToeicS);
        editSchool = findViewById(R.id.editSchool);
        editName = findViewById(R.id.editName);
        editAward = findViewById(R.id.editAwards);
        editIntern = findViewById(R.id.editIntern);
        editLisence = findViewById(R.id.editLincense);
        editOPIc = findViewById(R.id.editOpic);
        editOversea = findViewById(R.id.editOversea);
        editMajor = findViewById(R.id.editMajor);
    }

    public void createDoc(View view){
        DocumentReference doc = db.collection("userData").document(email);
        grade = Float.parseFloat(editGrade.getText().toString());
        toeic = Integer.parseInt(editToeic.getText().toString());
        award = Integer.parseInt(editToeic.getText().toString());
        license = Integer.parseInt(editToeic.getText().toString());
        intern = Integer.parseInt(editToeic.getText().toString());
        overseas = Integer.parseInt(editToeic.getText().toString());
        name = editName.getText().toString();
        college = editSchool.getText().toString();
        major = editMajor.getText().toString();
        opic = editOPIc.getSelectedItem().toString();
        toeicSpeaking = editToeicSpeaking.getSelectedItem().toString();

        //String email, String name, String college, String major, String opic, String toeicSpeaking, double grade, long toeic, long award, long license, long intern, long overseas
        user = new UserData(email, name, college, major, opic, toeicSpeaking, grade, toeic, award, license, intern, overseas);
        doc.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"스펙 등록 성공",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"스펙 등록 실패",Toast.LENGTH_LONG).show();
            }
        });
        intent = new Intent(SpecRegistActivity.this, WelecomActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

}
