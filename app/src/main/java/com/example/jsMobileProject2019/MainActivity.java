package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText editID, editPW;
    String email, password;
    FirebaseAuth mAuth;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        //파이어베이스 인증 객체 선언
        mAuth = FirebaseAuth.getInstance();
        editID = findViewById(R.id.loginID);
        editPW = findViewById(R.id.loginPW);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }

    //로그인버튼 함수
    public void signIn(View view){
        email = editID.getText().toString();
        password = editPW.getText().toString();
        if(!email.equals("")&&!password.equals("")){
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
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //로그인 성공
                    Toast.makeText(MainActivity.this,"로그인 성공",Toast.LENGTH_LONG).show();
                    intent = new Intent(MainActivity.this, WelecomActivity.class);
                    intent.putExtra("userID", email);
                    intent.putExtra("userPW", password);
                    startActivity(intent);

                }else {
                    //로그인 실패
                    Toast.makeText(MainActivity.this, "로그인 실패",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //가입버튼 함수
    public void signUp(View view){
        email = editID.getText().toString();
        password = editPW.getText().toString();

        if(!email.equals("")&&!password.equals("")) {
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
                            Toast.makeText(MainActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(MainActivity.this, "존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                            editID.setText("");
                            editPW.setText("");
                        }
                    }
                });
    }

}
