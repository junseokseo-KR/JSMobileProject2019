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
    private ArrayList<RadarEntry> userEntry, maxEntry, minEntry, avgEntry;
    private ArrayList<Double> gradeArr;
    private ArrayList<Long> internArr, licenseArr, awardArr, overseaArr,volunArr;
    RadarDataSet userSet, maxSet, minSet, avgSet;

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
                    for (QueryDocumentSnapshot qds : task.getResult()) {
                        gradeArr.add((double) qds.get("grade"));
                        volunArr.add((long) qds.get("volun"));
                        overseaArr.add((long) qds.get("overseas"));
                        internArr.add((long) qds.get("intern"));
                        awardArr.add((long) qds.get("award"));
                        licenseArr.add((long) qds.get("license"));
                        userCnt++;
                    }
                    userEntry.add(new RadarEntry((float) user.getGrade()));
                    userEntry.add(new RadarEntry((float) user.getVolun()));
                    userEntry.add(new RadarEntry((float) user.getOverseas()));
                    userEntry.add(new RadarEntry((float) user.getLicense()));
                    userEntry.add(new RadarEntry((float) user.getIntern()));
                    userEntry.add(new RadarEntry((float) user.getAward()));

                    maxEntry.add(new RadarEntry((float) (double)Collections.max(gradeArr)));
                    maxEntry.add(new RadarEntry((float) (long)Collections.max(volunArr)));
                    maxEntry.add(new RadarEntry((float) (long)Collections.max(overseaArr)));
                    maxEntry.add(new RadarEntry((float) (long)Collections.max(licenseArr)));
                    maxEntry.add(new RadarEntry((float) (long)Collections.max(internArr)));
                    maxEntry.add(new RadarEntry((float) (long)Collections.max(awardArr)));

                    minEntry.add(new RadarEntry((float) (double)Collections.min(gradeArr)));
                    minEntry.add(new RadarEntry((float) (long)Collections.min(volunArr)));
                    minEntry.add(new RadarEntry((float) (long)Collections.min(overseaArr)));
                    minEntry.add(new RadarEntry((float) (long)Collections.min(licenseArr)));
                    minEntry.add(new RadarEntry((float) (long)Collections.min(internArr)));
                    minEntry.add(new RadarEntry((float) (long)Collections.min(awardArr)));

                    avgEntry.add(new RadarEntry((float) setDoubleAvg(gradeArr)));
                    avgEntry.add(new RadarEntry((float) setLongAvg(volunArr)));
                    avgEntry.add(new RadarEntry((float) setLongAvg(overseaArr)));
                    avgEntry.add(new RadarEntry((float) setLongAvg(licenseArr)));
                    avgEntry.add(new RadarEntry((float) setLongAvg(internArr)));
                    avgEntry.add(new RadarEntry((float) setLongAvg(awardArr)));

                    Log.i("평균", Double.toString(setDoubleAvg(gradeArr))+" : "+Double.toString(setLongAvg(volunArr))+" : "+Double.toString(setLongAvg(overseaArr))+" : "+Double.toString(setLongAvg(licenseArr))+" : "+Double.toString(setLongAvg(internArr))+" : "+Double.toString(setLongAvg(awardArr)));

                    Log.w("max", maxEntry.toString());
                    Log.w("min", minEntry.toString());

                    userSet = new RadarDataSet(userEntry, "사용자");
                    userSet.setColor(Color.RED);
                    userSet.setFillColor(Color.RED);
                    userSet.setDrawFilled(true);
                    userSet.setFillAlpha(180);
                    userSet.setLineWidth(2f);
                    userSet.setDrawHighlightCircleEnabled(true);
                    userSet.setDrawHorizontalHighlightIndicator(false);

                    maxSet = new RadarDataSet(maxEntry, "최고");
                    maxSet.setColor(Color.rgb(121, 162, 175));
                    maxSet.setFillColor(Color.rgb(121, 162, 175));
                    maxSet.setDrawFilled(true);
                    maxSet.setFillAlpha(180);
                    maxSet.setLineWidth(2f);
                    maxSet.setDrawHighlightCircleEnabled(true);
                    maxSet.setDrawHorizontalHighlightIndicator(false);

                    minSet = new RadarDataSet(minEntry, "최저");
                    minSet.setColor(Color.MAGENTA);
                    minSet.setFillColor(Color.MAGENTA);
                    minSet.setDrawFilled(true);
                    minSet.setFillAlpha(180);
                    minSet.setLineWidth(2f);
                    minSet.setDrawHighlightCircleEnabled(true);
                    minSet.setDrawHorizontalHighlightIndicator(false);

                    avgSet = new RadarDataSet(minEntry, "평균");
                    avgSet.setColor(Color.YELLOW);
                    avgSet.setFillColor(Color.YELLOW);
                    avgSet.setDrawFilled(true);
                    avgSet.setFillAlpha(180);
                    avgSet.setLineWidth(2f);
                    avgSet.setDrawHighlightCircleEnabled(true);
                    avgSet.setDrawHorizontalHighlightIndicator(false);

                    ArrayList<IRadarDataSet> sets = new ArrayList<>();
                    sets.add(userSet);
                    sets.add(maxSet);
                    sets.add(minSet);
                    sets.add(avgSet);

                    RadarData data = new RadarData(sets);
                    data.setValueTextSize(8f);
                    data.setDrawValues(false);
                    data.setValueTextColor(Color.BLACK);

                    userCntTextView.setText(userCnt+"명의 데이터 입니다.");

                    rchart.setData(data);
                    rchart.invalidate();
                }
            }
        });
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


}
