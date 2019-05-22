package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {
    MaterialEditText editID, editPW;
    String email, password;
    FirebaseAuth mAuth;
    Intent intent;
    int logBtnState = 0;
    int joinBtnState = 0;
    ActionProcessButton logBtn, joinBtn;
    UserData user;
    FirebaseFirestore db;
    DocumentReference dr;
    DocumentSnapshot ds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        //파이어베이스 인증 객체 선언
        mAuth = FirebaseAuth.getInstance();
        editID = findViewById(R.id.loginID);
        editPW = findViewById(R.id.loginPW);
        logBtn = findViewById(R.id.loginBtn);
        joinBtn = findViewById(R.id.joinBtn);
        db = FirebaseFirestore.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        editID.setText("");
        editPW.setText("");
        logBtn.setProgress(0);
        joinBtn.setProgress(0);
        editID.requestFocus();
    }

    //로그인버튼 함수
    public void signIn(View view){
        email = editID.getText().toString();
        password = editPW.getText().toString();
        if(!email.equals("")&&!password.equals("")){
            logBtnState=50;
            logBtn.setProgress(logBtnState);
            logBtn.setText("로그인 중");
            requestLogin(email, password);
        }else if(!email.equals("")){
            Toast.makeText(getApplicationContext(),"이메일을 입력하세요!",Toast.LENGTH_LONG).show();
        }else if(!password.equals("")){
            Toast.makeText(getApplicationContext(),"비밀번호를 입력하세요!",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"이메일과 비밀번호를 입력하세요!",Toast.LENGTH_LONG).show();
        }
    }

    //로그인
    private void requestLogin(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                System.out.println(email+" : "+password);
                //로그인 성공
                if(task.isSuccessful()){
                    //데이터 불러오기
                    logBtn.setText("데이터 불러오는 중");
                    System.out.println("불러오기 성공");
                    dr = db.collection("userData").document(email);
                    System.out.println("dr 성공");
                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            ds = task.getResult();

                            System.out.println("ds 성공");
                            if (ds.exists()) {
                                System.out.println("ds 존재");
                                logBtnState=100;
                                System.out.println("logBtnState 변경");
                                logBtn.setProgress(logBtnState);
                                System.out.println("logBtn 프로세스 변경");
                                logBtn.setText("로그인 성공");
                                System.out.println(ds.getString("name"));
                                user = new UserData(ds.getString("email"), ds.getString("name"),ds.getString("college"),ds.getString("major"),ds.getString("opic"),ds.getString("toeicSpeaking"), (double) ds.get("grade"), (long) ds.get("toeic"), (long) ds.get("award"), (long) ds.get("license"), (long) ds.get("intern"),(long) ds.get("overseas"), (long) ds.get("volun"));
                                System.out.print(user);
                                intent = new Intent( MainActivity.this, WelecomActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            } else {
                                System.out.println("No Data");
                                logBtnState=-1;
                                logBtn.setProgress(logBtnState);
                                logBtn.setText("프로필 미등록");
                                Toast.makeText(getApplicationContext(), "프로필을 등록해주세요.", Toast.LENGTH_LONG).show();
                                intent = new Intent(MainActivity.this, SpecRegistActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            logBtnState=-1;
                            logBtn.setProgress(logBtnState);
                            logBtn.setText("데이터 불러오기 실패");
                        }
                    });
                }else {
                    //로그인 실패
                    logBtnState=-1;
                    logBtn.setProgress(logBtnState);
                    Log.w("에러",task.getException().getMessage());
                    if (task.getException().getMessage().equals("The email address is badly formatted.")){
                        Toast.makeText(getApplicationContext(), "이메일 주소의 형식이 잘못되었습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    //가입버튼 함수
    public void signUp(View view){
        email = editID.getText().toString();
        password = editPW.getText().toString();

        if(!email.equals("")&&!password.equals("")) {
            joinBtnState=50;
            joinBtn.setProgress(joinBtnState);
            createUser(email, password);
        }else{
            Toast.makeText(getApplicationContext(),"아이디와 비밀번호를 모두 입력해야합니다.",Toast.LENGTH_LONG).show();
        }
    }

    //회원 가입
    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            joinBtnState=100;
                            joinBtn.setProgress(joinBtnState);
                        } else {
                            // 회원가입 실패
                            joinBtnState=-1;
                            joinBtn.setProgress(joinBtnState);
                            Toast.makeText(MainActivity.this, "존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                            editID.setText("");
                            editPW.setText("");
                        }
                    }
                });
    }

}
