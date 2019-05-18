package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class TestActivity extends AppCompatActivity {
    double gradeMin;
    FirebaseFirestore db;
    CollectionReference citiesRef;
    Query query;
    //    DocumentReference dr;
//    DocumentSnapshot ds;
    QuerySnapshot qs;
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        db = FirebaseFirestore.getInstance();
        gradeMin = 4.5;
        tv = findViewById(R.id.testView);

// Create a reference to the cities collection
        citiesRef = db.collection("specData");

// Create a query against the collection.
        query = citiesRef.whereEqualTo("major", "컴퓨터/시스템공학");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                            double grade = (double) ds.get("grade");
//                            if (gradeMin > grade) {
//                                gradeMin = grade;
//                            }
                    for (QueryDocumentSnapshot qds : task.getResult()) {
                        Log.i("qdsID", qds.getId());
                    }
//                }
                }
            }
        });
    }
}


//        db = FirebaseFirestore.getInstance();
//        db.collection("specData").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(Qu)
//            }
//        })
//        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                ds = task.getResult();
//                if (ds.exists()){
//                        System.out.println(ds);
//                }
//                else{
//                    System.out.println("없음");
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Overridew
//            public void onFailure(@NonNull Exception e) {
//                e.printStackTrace();
//            }
//        });