package com.example.jsMobileProject2019;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SelectCorpActivity extends AppCompatActivity{
    FirebaseFirestore db;
    CollectionReference corpRef;
    Query query;
    DocumentSnapshot ds;
    private SmartMaterialSpinner departSpinner;
    private List<String> corpList,fireList; //리스트에 사용할 리스트, firebase에서 추출할 리스트
    private ListView listView;          // 검색을 보여줄 리스트변수
    private MaterialEditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> searchList;   //검색에 사용할 리스트
    Intent intent;
    UserData user;
    String corpName, departName;
    TextView corpView, departView,userCntTextView;
    CollectionReference dataRef;
    int userCnt;
    private RadarChart rchart;
    private ArrayList<RadarEntry> userEntry, maxEntry, minEntry, avgEntry, saraminEntry;
    private ArrayList<Double> gradeArr;
    private ArrayList<Long> internArr, licenseArr, awardArr, overseaArr,toeicSArr;
    RadarDataSet userSet, maxSet, minSet, avgSet, saraminSet;
    RadarData data;
    boolean makeChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_corp_page);

        makeChart = false;
        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");
        corpName = null;
        departName = null;
        departSpinner = findViewById(R.id.departTest);

        db = FirebaseFirestore.getInstance();
        corpRef = db.collection("corpData");

        editSearch = (MaterialEditText) findViewById(R.id.editSearch);
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

        userCnt = 0;
        gradeArr = new ArrayList<>();
        internArr = new ArrayList<>();
        licenseArr = new ArrayList<>();
        awardArr = new ArrayList<>();
        overseaArr = new ArrayList<>();
        toeicSArr = new ArrayList<>();
        userCntTextView = findViewById(R.id.userCntTextView);

        rchart = findViewById(R.id.radarChart);
        rchart.setBackgroundColor(Color.WHITE);
        rchart.getDescription().setEnabled(false);
        rchart.setWebColor(Color.BLACK);
        rchart.setWebLineWidth(2.5f);
        rchart.setWebColorInner(Color.BLACK );
        rchart.setWebAlpha(100);
        rchart.setWebLineWidthInner(1.5f);
        rchart.setNoDataText("추출된 데이터가 없습니다.");

        rchart.animateXY(1400, 1400, Easing.EaseInOutQuad);

        XAxis xAxis = rchart.getXAxis();
        xAxis.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        xAxis.setTextSize(12f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(){
            private final String[] mActivites = new String[]{"학점","토익스피킹","해외경험","자격증","인턴","수상"};

            @Override
            public String getFormattedValue(float value) {
                return mActivites[(int) value%mActivites.length];
            }
        });
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = rchart.getYAxis();
        yAxis.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        yAxis.setLabelCount(4,false);
        yAxis.setTextSize(9f);
        yAxis.setMinWidth(0f);
        yAxis.setMaxWidth(80f);
        yAxis.setDrawLabels(true);

        Legend l = rchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);
        l.setTextSize(15f);
    }

    private void setData() {
        userEntry = new ArrayList<>();
        saraminEntry = new ArrayList<>();
        maxEntry = new ArrayList<>();
        minEntry = new ArrayList<>();
        avgEntry = new ArrayList<>();

//        intent = getIntent();
//        user = (UserData) intent.getSerializableExtra("user");
//        corp = intent.getStringExtra("corpName");
//        depart = intent.getStringExtra("departName");

//        topTextView.setText(corpName);
//        bottomTextView.setText(departName);
//        userCntTextView.setText("데이터를 불러오는 중...");

        db = FirebaseFirestore.getInstance();
        dataRef = db.collection("specData");
        if (departName.equals("모든부서")){
            query = dataRef.whereEqualTo("corporation",corpName);
        }else {
            query = dataRef.whereEqualTo("corporation", corpName).whereEqualTo("department", departName);
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "데이터 불러오기 성공", Toast.LENGTH_LONG).show();
                    ArrayList<IRadarDataSet> sets = new ArrayList<>();
                    for (QueryDocumentSnapshot qds : task.getResult()) {
                        gradeArr.add((double) qds.get("grade"));
                        toeicSArr.add(loadToeicS((String) qds.get("toeicSpeaking")));
                        overseaArr.add((long) qds.get("overseas"));
                        internArr.add((long) qds.get("intern"));
                        awardArr.add((long) qds.get("award"));
                        licenseArr.add((long) qds.get("license"));
                        Log.i("토익스피킹",toeicSArr.toString());
                        userCnt++;
                    }

                    userChart();
                    if (userCnt==1){
                        saraminSet = new RadarDataSet(setEntry(saraminEntry,gradeArr.get(0),toeicSArr.get(0),overseaArr.get(0),licenseArr.get(0),internArr.get(0),awardArr.get(0)), "사람인");
                        setChartAttribute(saraminSet,Color.RED);

                        sets.add(saraminSet);
                    }else if(userCnt==2){
                        saraminSet = new RadarDataSet(setEntry(avgEntry,setDoubleAvg(gradeArr),setLongAvg(toeicSArr),setLongAvg(overseaArr),setLongAvg(licenseArr),setLongAvg(internArr),setLongAvg(awardArr)), "평균");
                        setChartAttribute(saraminSet,Color.RED);

                        sets.add(saraminSet);
                    }
                    else {
                        maxSet = new RadarDataSet(setEntry(maxEntry, Collections.max(gradeArr),Collections.max(toeicSArr),Collections.max(overseaArr),Collections.max(licenseArr),Collections.max(internArr),Collections.max(awardArr)), "최고");
                        setChartAttribute(maxSet,Color.rgb(183,71,42));

                        minSet = new RadarDataSet(setEntry(minEntry,Collections.min(gradeArr),Collections.min(toeicSArr),Collections.min(overseaArr),Collections.min(licenseArr),Collections.min(internArr),Collections.min(awardArr)), "최저");
                        setChartAttribute(minSet,Color.rgb(33,115,70));

                        avgSet = new RadarDataSet(setEntry(avgEntry,setDoubleAvg(gradeArr),setLongAvg(toeicSArr),setLongAvg(overseaArr),setLongAvg(licenseArr),setLongAvg(internArr),setLongAvg(awardArr)), "평균");
                        setChartAttribute(avgSet,Color.YELLOW);

                        sets.add(minSet);
                        sets.add(avgSet);
                        sets.add(maxSet);
                    }
                    sets.add(userSet);

                    data = new RadarData(sets);
                    data.setValueTextSize(8f);
                    data.setDrawValues(false);
                    data.setValueTextColor(Color.BLACK);

                    userCntTextView.setText(userCnt + "명");

                    makeChart = true;
                    rchart.setData(data);
                    rchart.invalidate();
                    rchart.setTouchEnabled(true);
                }
            }
        });
    }

    // 검색수행 메소드
    public void search(String charText) {

        corpList.clear();

        if (!(charText.length() == 0))
        {
            for(int i = 0; i < searchList.size(); i++)
            {
                if (searchList.get(i).toUpperCase().contains(charText))
                {
                    corpList.add(searchList.get(i));
                }
            }
        }

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
        }else{
            userCnt=0;
            setData();
        }
    }

    public void moveWidget(View v){
        Log.i("결과", String.valueOf(makeChart));
        if (!makeChart){
            Toast.makeText(getApplicationContext(),"먼저 차트를 추출하세요!", Toast.LENGTH_SHORT).show();
        }else{
            try {
                Bitmap chartBit = rchart.getChartBitmap();
                Bitmap resized = null;
                int resizedScale = 400;
                int height = chartBit.getHeight();
                int width = chartBit.getWidth();
                while (height > resizedScale) {
                    resized = Bitmap.createScaledBitmap(chartBit, (width * resizedScale) / height, resizedScale, true);
                    height = resized.getHeight();
                    width = resized.getWidth();
                }
                intent = new Intent(SelectCorpActivity.this, chartwidget_provider.class);
                intent.putExtra("bit", (Bitmap) resized);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    private void setChartAttribute(RadarDataSet dataSet, int color){
        dataSet.setColor(color);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(color);
        dataSet.setFillAlpha(0);
        dataSet.setLineWidth(3f);
        dataSet.setDrawHighlightCircleEnabled(false);
        dataSet.setDrawHorizontalHighlightIndicator(false);
    }


    protected double setDoubleAvg(ArrayList arr){
        double len = arr.size();
        double sum = 0;
        double avg = 0;
        for (int i=0;i<len;i++){
            sum += (double)arr.get(i);
        }
        avg = sum/len;
        return avg;
    }
    protected double setLongAvg(ArrayList arr){
        double len = arr.size();

        long sum = 0;
        double avg;
        for (int i=0;i<len;i++){
            sum += (long)arr.get(i);
        }
        avg = (double)sum/len;
        return avg;
    }

    protected ArrayList setEntry(ArrayList entry, double gradeVal, double toeicSVal, double overseaVal, double licenseVal, double internVal, double awardVal){
        entry.add(new RadarEntry((float) gradeVal));
        entry.add(new RadarEntry((float) toeicSVal));
        entry.add(new RadarEntry((float) overseaVal));
        entry.add(new RadarEntry((float) licenseVal));
        entry.add(new RadarEntry((float) internVal));
        entry.add(new RadarEntry((float) awardVal));
        return entry;
    }

    protected void userChart(){
        userSet = new RadarDataSet(setEntry(userEntry,user.getGrade(),loadToeicS(user.getToeicSpeaking()),user.getOverseas(),user.getLicense(),user.getIntern(),user.getAward()), user.getName());
        userSet.setColor(Color.rgb(121, 162, 175));
        userSet.setFillColor(Color.rgb(121, 162, 175));
        userSet.setDrawFilled(true);
        userSet.setFillAlpha(150);
        userSet.setLineWidth(2.5f);
        userSet.setDrawHighlightCircleEnabled(true);
        userSet.setDrawHorizontalHighlightIndicator(false);
    }


    protected long loadToeicS(String toeicS){
        long tsValue = 0;
        switch (toeicS){
            case "없음":
                tsValue=0;
                break;
            case "L1":
                tsValue=1;
                break;
            case "L2":
                tsValue=2;
                break;
            case "L3":
                tsValue=3;
                break;
            case "L4":
                tsValue=4;
                break;
            case "L5":
                tsValue=5;
                break;
            case "L6":
                tsValue=6;
                break;
            case "L7":
                tsValue=7;
                break;
            case "L8":
                tsValue=8;
                break;
        }
        return tsValue;
    }

}