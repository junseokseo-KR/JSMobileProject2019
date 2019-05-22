package com.example.jsMobileProject2019;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private ArrayList<Long> internArr, licenseArr, awardArr, overseaArr,volunArr;
    RadarDataSet userSet, maxSet, minSet, avgSet, saraminSet;

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
        volunArr = new ArrayList<>();
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
            private final String[] mActivites = new String[]{"학점","봉사활동","해외경험","자격증","인턴","수상"};

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
        l.setDrawInside(false);
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
        query = dataRef.whereEqualTo("corporation", corp).whereEqualTo("department", depart);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "데이터 불러오기 성공", Toast.LENGTH_LONG).show();
                    ArrayList<IRadarDataSet> sets = new ArrayList<>();
                    for (QueryDocumentSnapshot qds : task.getResult()) {
                        gradeArr.add((double) qds.get("grade"));
                        volunArr.add((long) qds.get("volun"));
                        overseaArr.add((long) qds.get("overseas"));
                        internArr.add((long) qds.get("intern"));
                        awardArr.add((long) qds.get("award"));
                        licenseArr.add((long) qds.get("license"));
                        userCnt++;
                    }
                    if (userCnt==1){
                        userChart();

                        saraminSet = new RadarDataSet(setEntry(saraminEntry,gradeArr.get(0),volunArr.get(0),overseaArr.get(0),licenseArr.get(0),internArr.get(0),awardArr.get(0)), "사람인");
                        setChartAttribute(saraminSet,Color.rgb(121, 162, 175));

                        sets.add(userSet);
                        sets.add(saraminSet);
                    }else {
                        userChart();

                        maxSet = new RadarDataSet(setEntry(maxEntry,Collections.max(gradeArr),Collections.max(volunArr),Collections.max(overseaArr),Collections.max(licenseArr),Collections.max(internArr),Collections.max(awardArr)), "최고");
                        setChartAttribute(maxSet,Color.rgb(121, 162, 175));

                        minSet = new RadarDataSet(setEntry(minEntry,Collections.min(gradeArr),Collections.min(volunArr),Collections.min(overseaArr),Collections.min(licenseArr),Collections.min(internArr),Collections.min(awardArr)), "최저");
                        setChartAttribute(minSet,Color.MAGENTA);

                        avgSet = new RadarDataSet(setEntry(avgEntry,setDoubleAvg(gradeArr),(long)setLongAvg(volunArr),(long)setLongAvg(overseaArr),(long)setLongAvg(licenseArr),(long)setLongAvg(internArr),(long)setLongAvg(awardArr)), "평균");
                        setChartAttribute(avgSet,Color.YELLOW);

                        sets = new ArrayList<>();
                        sets.add(userSet);
                        sets.add(maxSet);
                        sets.add(minSet);
                        sets.add(avgSet);
                    }

                    RadarData data = new RadarData(sets);
                    data.setValueTextSize(8f);
                    data.setDrawValues(false);
                    data.setValueTextColor(Color.BLACK);

                    userCntTextView.setText(userCnt + "명의 데이터 입니다.");

                    rchart.setData(data);
                    rchart.invalidate();
                }
            }
        });
    }


    private void setChartAttribute(RadarDataSet dataSet, int color){
        dataSet.setColor(color);
        dataSet.setFillColor(color);
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(180);
        dataSet.setLineWidth(2f);
        dataSet.setDrawHighlightCircleEnabled(true);
        dataSet.setDrawHorizontalHighlightIndicator(false);
    }


    protected double setDoubleAvg(ArrayList arr){
        double len = arr.size();
        double sum = 0;
        double avg = 0;
        for (int i=0;i<len;i++){
            sum += (double)arr.get(i);
        }
        Log.i("평균 double", Double.toString(sum));
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
        Log.i("평균 long", Double.toString(sum));
        avg = sum/len;
        return avg;
    }

    protected ArrayList setEntry(ArrayList entry, double gradeVal, long volunVal, long overseaVal, long licenseVal, long internVal, long awardVal){
        entry.add(new RadarEntry((float) gradeVal));
        entry.add(new RadarEntry((float) volunVal));
        entry.add(new RadarEntry((float) overseaVal));
        entry.add(new RadarEntry((float) licenseVal));
        entry.add(new RadarEntry((float) internVal));
        entry.add(new RadarEntry((float) awardVal));
        return entry;
    }

    protected void userChart(){
        userSet = new RadarDataSet(setEntry(userEntry,user.getGrade(),user.getVolun(),user.getOverseas(),user.getLicense(),user.getIntern(),user.getAward()), user.getName());
        userSet.setColor(Color.RED);
        userSet.setFillColor(Color.RED);
        userSet.setDrawFilled(true);
        userSet.setFillAlpha(180);
        userSet.setLineWidth(2f);
        userSet.setDrawHighlightCircleEnabled(true);
        userSet.setDrawHorizontalHighlightIndicator(false);
    }


}
