package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SpecRegistActivity extends AppCompatActivity {
    TextView viewID;
    String userID,name;
    EditText editGrade,editToeic,editName;
    float grade;
    int toeic;
    FirebaseFirestore db;
    Intent intent;
    UserDTO user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spec_regist_page);
        intent = getIntent();
        db = FirebaseFirestore.getInstance();
        viewID = findViewById(R.id.viewID);
        userID = intent.getExtras().getString("userID");
        editGrade = findViewById(R.id.editGrade);
        editToeic = findViewById(R.id.editToeic);
        editName = findViewById(R.id.editName);

        viewID.setText(userID);
    }

    public void createDoc(View view){
        DocumentReference doc = db.collection("userData").document(userID);
        grade = Float.parseFloat(editGrade.getText().toString());
        toeic = Integer.parseInt(editToeic.getText().toString());
        name = editName.getText().toString();
        user = new UserDTO(userID,name,grade,toeic);
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
    }

    public void BackActivity(View view){
        finish();
    }
}
