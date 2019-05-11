package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MySpecActivity extends AppCompatActivity {
    String email, name;
    long toeic;
    double grade;
    Intent intent;
    TextView viewEmail, viewName, viewGrade, viewToeic;
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spec_page);
        intent = getIntent();
        email = intent.getExtras().getString("email");
        viewEmail = findViewById(R.id.viewEMAIL);
        viewName = findViewById(R.id.viewNAME);
        viewGrade = findViewById(R.id.viewGRADE);
        viewToeic = findViewById(R.id.viewTOEIC);

        db = FirebaseFirestore.getInstance();
        final DocumentReference dr = db.collection("userData").document(email);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                if (ds.exists()) {
                    Toast.makeText(getApplicationContext(), "데이터 불러오기 성공", Toast.LENGTH_LONG).show();
                    email = ds.getString("email");
                    name = ds.getString("name");
                    toeic = (long) ds.get("toeic");
                    grade = (double) ds.get("grade");

                    viewEmail.setText(email);
                    viewName.setText(name);
                    viewGrade.setText(""+ grade);
                    viewToeic.setText(""+ toeic);
                } else {
                    System.out.println("No Data");
                    Toast.makeText(getApplicationContext(), "데이터 불러오기 실패", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "데이터 불러오기 실패", Toast.LENGTH_LONG).show();
            }
        });
    }
}
