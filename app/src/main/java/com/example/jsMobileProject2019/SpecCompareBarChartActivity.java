package com.example.jsMobileProject2019;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class SpecCompareBarChartActivity extends AppCompatActivity {
    TextView topTextView, bottomTextView, userCntTextView;
    FirebaseFirestore db;
    CollectionReference dataRef;
    Query query;
    Intent intent;
    String corp,depart;
    int userCnt;
    UserData user;
    private RadarChart rchart;
    private ArrayList<RadarEntry> userEntry, maxEntry, minEntry, avgEntry, saraminEntry;
    private ArrayList<Double> gradeArr;
    private ArrayList<Long> internArr, licenseArr, awardArr, overseaArr,toeicSArr;
    RadarDataSet userSet, maxSet, minSet, avgSet, saraminSet;
    RadarData data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spec_compare_bar_chart);
        userCnt = 0;
        gradeArr = new ArrayList<>();
        internArr = new ArrayList<>();
        licenseArr = new ArrayList<>();
        awardArr = new ArrayList<>();
        overseaArr = new ArrayList<>();
        toeicSArr = new ArrayList<>();
        topTextView = findViewById(R.id.corpTextView);
        bottomTextView = findViewById(R.id.departTextView);
        userCntTextView = findViewById(R.id.userCntTextView);

        rchart = findViewById(R.id.radarChart);
        rchart.setBackgroundColor(Color.WHITE);
        rchart.getDescription().setEnabled(false);
        rchart.setWebLineWidth(1f);
        rchart.setWebColor(Color.BLACK);
        rchart.setWebLineWidth(1f);
        rchart.setWebColorInner(Color.BLACK );
        rchart.setWebAlpha(100);

        setData();

        rchart.animateXY(1400, 1400, Easing.EaseInOutQuad);

        XAxis xAxis = rchart.getXAxis();
        xAxis.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        xAxis.setTextSize(9f);
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
    }

    private void setData() {
        userEntry = new ArrayList<>();
        saraminEntry = new ArrayList<>();
        maxEntry = new ArrayList<>();
        minEntry = new ArrayList<>();
        avgEntry = new ArrayList<>();

        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");
        corp = intent.getStringExtra("corpName");
        depart = intent.getStringExtra("departName");

        topTextView.setText(corp);
        bottomTextView.setText(depart);
        userCntTextView.setText("데이터를 불러오는 중...");

        db = FirebaseFirestore.getInstance();
        dataRef = db.collection("specData");
        if (depart.equals("모든부서")){
            query = dataRef.whereEqualTo("corporation",corp);
        }else {
            query = dataRef.whereEqualTo("corporation", corp).whereEqualTo("department", depart);
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
                        maxSet = new RadarDataSet(setEntry(maxEntry,Collections.max(gradeArr),Collections.max(toeicSArr),Collections.max(overseaArr),Collections.max(licenseArr),Collections.max(internArr),Collections.max(awardArr)), "최고");
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

                    userCntTextView.setText(userCnt + "명의 데이터 입니다.");

                    rchart.setData(data);
                    rchart.invalidate();
                    rchart.setTouchEnabled(true);
                }
            }
        });
    }



    public void moveWidget(View v){
        Bitmap chartBit = rchart.getChartBitmap();
        Bitmap resized = null;
        int resizedScale = 597;
        int height = chartBit.getHeight();
        int width = chartBit.getWidth();
        while(height>resizedScale){
            resized = Bitmap.createScaledBitmap(chartBit,(width*resizedScale)/height,resizedScale,true);
            height = resized.getHeight();
            width = resized.getWidth();
        }
        intent = new Intent(SpecCompareBarChartActivity.this, chartwidget_provider.class);
        intent.putExtra("bit",(Bitmap)resized);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent. FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
    }
    private void setChartAttribute(RadarDataSet dataSet, int color){
        dataSet.setColor(color);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(color);
        dataSet.setFillAlpha(0);
        dataSet.setLineWidth(2f);
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
        userSet.setFillAlpha(50);
        userSet.setLineWidth(2f);
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
