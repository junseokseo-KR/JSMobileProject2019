package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Radar;
import com.anychart.core.radar.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.MarkerType;
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

public class SpecCompareBarChartActivity extends AppCompatActivity {
    FirebaseFirestore db;
    DocumentReference dr;
    DocumentSnapshot ds;
    CollectionReference dataRef;
    Query query;
    Intent intent;
    String email,college,major,opic,corp,depart,toeicS;
    long award, license,intern,oversea,toeic,volun;
    double grade;
    ArrayList saraminArr;
    Radar radar;
    AnyChartView anyChartView;
    UserData user;
    Button btn;
    double gradeMax;
    long internMax, licenseMax, awardMax, toeicMax;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spec_compare_bar_chart);

        gradeMax=0;
        toeicMax=0;
        internMax=0;
        licenseMax=0;
        awardMax=0;

        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        radar = AnyChart.radar();

        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");
        corp = intent.getExtras().getString("corpName");
        depart = intent.getExtras().getString("departName");

        db = FirebaseFirestore.getInstance();
        dataRef = db.collection("specData");
//        query = dataRef.whereEqualTo("corporation", corp).whereEqualTo("department", depart);
        query = dataRef.whereEqualTo("corporation", "DB lnc.").whereEqualTo("department", "네트워크관라");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "데이터 불러오기 성공", Toast.LENGTH_LONG).show();
                    for (QueryDocumentSnapshot qds : task.getResult()) {
                        Log.i("사용자",qds.getId());
                        gradeMax = setDoubleMax(gradeMax,grade,"grade",qds);
                        grade = (double)qds.get("grade");
                        if (gradeMax<grade)
                            gradeMax = grade;
                        toeicMax = setLongMax(toeicMax,toeic,"toeic",qds);
                        internMax = setLongMax(internMax,intern,"intern",qds);
                        awardMax = setLongMax(awardMax,award,"award",qds);
                        licenseMax = setLongMax(licenseMax,license,"license",qds);
                    }

                    radar.title(corp+" / "+depart);

                    radar.yScale().minimum(0d);
                    radar.yScale().minimumGap(0d);
                    radar.yScale().ticks().interval(2d);

                    radar.xAxis().labels().padding(5d, 5d, 5d, 5d);

                    radar.legend()
                            .align(Align.CENTER)
                            .enabled(true);

                    List<DataEntry> data = new ArrayList<>();
                    data.add(new CustomDataEntry("학점", user.getGrade(), gradeMax));
                    data.add(new CustomDataEntry("토익", (user.getToeic()/100), (toeicMax/100)));
                    data.add(new CustomDataEntry("자격증", user.getLicense(), licenseMax));
                    data.add(new CustomDataEntry("인턴경험", user.getIntern(), internMax));
                    data.add(new CustomDataEntry("수상경험", user.getAward(), awardMax));

                    Set set = Set.instantiate();
                    set.data(data);
                    Mapping userData = set.mapAs("{ x: 'x', value: 'value' }");
                    Mapping saraminData = set.mapAs("{ x: 'x', value: 'value2' }");

                    Line userLine = radar.line(userData);
                    userLine.name(user.getName());
                    userLine.markers()
                            .enabled(true)
                            .type(MarkerType.CIRCLE)
                            .size(3d);

                    Line maxLine = radar.line(saraminData);
                    maxLine.name("최고");
                    maxLine.markers()
                            .enabled(true)
                            .type(MarkerType.DIAMOND)
                            .size(3d);

                    radar.tooltip().format("Value: {%Value}");

                    anyChartView.setChart(radar);
                }
            }
        });
//        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                ds = task.getResult();
//                if (ds.exists()) {
//                    Toast.makeText(getApplicationContext(), "데이터 불러오기 성공", Toast.LENGTH_LONG).show();
//                    college = ds.getString("college");
//                    major = ds.getString("major");
//                    opic = ds.getString("opic");
//                    corp = ds.getString("corporation");
//                    depart = ds.getString("department");
//                    toeicS = ds.getString("toeicSpeaking");
//                    toeic = (long) ds.get("toeic");
//                    intern = (long) ds.get("intern");
//                    oversea = (long) ds.get("overseas");
//                    volun = (long) ds.get("volunteer activity");
//                    award = (long) ds.get("award");
//                    license = (long) ds.get("license");
//                    grade = (double) ds.get("grade");
//
//                    radar.title(corp+" / "+depart);
//
//                    radar.yScale().minimum(0d);
//                    radar.yScale().minimumGap(0d);
//                    radar.yScale().ticks().interval(2d);
//
//                    radar.xAxis().labels().padding(5d, 5d, 5d, 5d);
//
//                    radar.legend()
//                            .align(Align.CENTER)
//                            .enabled(true);
//
//                    List<DataEntry> data = new ArrayList<>();
//                    data.add(new CustomDataEntry("학점", user.getGrade(), grade));
//                    data.add(new CustomDataEntry("토익", (user.getToeic()/100), (toeic/100)));
//                    data.add(new CustomDataEntry("자격증", user.getLicense(), license));
//                    data.add(new CustomDataEntry("인턴경험", user.getIntern(), intern));
//                    data.add(new CustomDataEntry("수상경험", user.getAward(), award));
//
//                    Set set = Set.instantiate();
//                    set.data(data);
//                    Mapping userData = set.mapAs("{ x: 'x', value: 'value' }");
//                    Mapping saraminData = set.mapAs("{ x: 'x', value: 'value2' }");
//
//                    Line userLine = radar.line(userData);
//                    userLine.name(user.getName());
//                    userLine.markers()
//                            .enabled(true)
//                            .type(MarkerType.CIRCLE)
//                            .size(3d);
//
//                    Line maxLine = radar.line(saraminData);
//                    maxLine.name("최고");
//                    maxLine.markers()
//                            .enabled(true)
//                            .type(MarkerType.DIAMOND)
//                            .size(3d);
//
//                    radar.tooltip().format("Value: {%Value}");
//
//                    anyChartView.setChart(radar);
//                    System.out.println("da"+saraminArr);
//                } else {
//                    System.out.println("No Data");
//                    Toast.makeText(getApplicationContext(), "데이터 불러오기 실패", Toast.LENGTH_LONG).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "데이터 불러오기 실패", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private class CustomDataEntry extends ValueDataEntry {
        public CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }

    protected long setLongMax(long valueMax, long value, String fieldName, QueryDocumentSnapshot qds){
        value = (long)qds.get(fieldName);
        if (valueMax<value)
            valueMax = value;
        return valueMax;
    }

    protected double setDoubleMax(double valueMax, double value, String fieldName, QueryDocumentSnapshot qds){
        value = (double)qds.get(fieldName);
        if (valueMax<value)
            valueMax = value;
        return valueMax;
    }
}
