package com.example.jsMobileProject2019;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class SelectCorpActivity extends AppCompatActivity{
    FirebaseFirestore db;
    CollectionReference corpRef;
    Query query;
    DocumentReference dr;
    DocumentSnapshot ds;
    QuerySnapshot qs;
    Button tv;
    private SmartMaterialSpinner departSpinner;
    private List<String> corpList,fireList; //리스트에 사용할 리스트, firebase에서 추출할 리스트
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SpinnerAdapter departAdap;
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> searchList;   //검색에 사용할 리스트
    Intent intent;
    UserData user;
    String corpName, departName;
    TextView corpView, departView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_corp_page);

        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");
        corpName = null;
        departName = null;

        departSpinner = findViewById(R.id.departTest);

        db = FirebaseFirestore.getInstance();
        corpRef = db.collection("corpData");

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);

        corpView = findViewById(R.id.corpName);
        departView = findViewById(R.id.departName);

        corpList = new ArrayList<>();
        searchList = new ArrayList<String>();

        query = corpRef;
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    fireList = new ArrayList<>();
                    for (QueryDocumentSnapshot qds : task.getResult()) {
                        fireList.add(qds.getId());
                    }
                }
                corpList.addAll(fireList);  //리스트에 사용할 리스트
                searchList.addAll(corpList); //검색에 사용할 리스트
                adapter = new SearchAdapter(corpList,SelectCorpActivity.this);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listView.setAdapter(adapter);
                editSearch.setHint("원하는 기업을 검색하세요.");
                // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
                editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String text = editSearch.getText().toString();
                        search(text);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String selectItem = corpList.get(position).toString();
                                editSearch.setText(selectItem);
                                setCorpSpinner(selectItem);
                            }
                        });
                    }
                });
                corpList.clear();
            }
        });

    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.x
        corpList.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0; i < searchList.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (searchList.get(i).toUpperCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    corpList.add(searchList.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    protected void setCorpSpinner(String corp){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference departRef = db.collection("corpData").document(corp);
        departRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ds = task.getResult();
                if (ds.exists()){
                    final ArrayList<String> departList = (ArrayList<String>) ds.get("department");
                    departSpinner.setItems(departList);
                    departSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            departName = departList.get(position);
                            corpView.setText(editSearch.getText());
                            departView.setText(departName);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        });
    }

    public void submitCorpDepart(View v){
        corpName = corpView.getText().toString();
        if (corpView.getText().equals("")){
            Toast.makeText(getApplicationContext(),"원하는 기업과 부서를 선택하세요!", Toast.LENGTH_SHORT).show();
            Log.i("결과", String.valueOf(corpView.getText().equals("")));
        }else{
            intent = new Intent(SelectCorpActivity.this, SpecCompareBarChartActivity.class);
            Log.i("기업", corpName);
            Log.i("부서",departName);
            intent.putExtra("corpName", corpName);
            intent.putExtra("departName", departName);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }
}