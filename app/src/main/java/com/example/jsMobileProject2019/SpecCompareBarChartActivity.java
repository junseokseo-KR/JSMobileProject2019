package com.example.jsMobileProject2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SpecCompareBarChartActivity extends AppCompatActivity {
    FirebaseFirestore db;
    DocumentReference dr;
    DocumentSnapshot ds;
    Intent intent;
    String email,college,major,opic,corp,depart,toeicS;
    long award, license,intern,oversea,toeic,volun;
    double grade;
    ArrayList saraminArr;
    Radar radar;
    AnyChartView anyChartView;
    UserData user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spec_compare_bar_chart);


        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        radar = AnyChart.radar();
        intent = getIntent();
        user = (UserData) intent.getSerializableExtra("user");

        db = FirebaseFirestore.getInstance();
        dr = db.collection("specData").document("user"+921+"_"+2);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ds = task.getResult();
                if (ds.exists()) {
                    Toast.makeText(getApplicationContext(), "데이터 불러오기 성공", Toast.LENGTH_LONG).show();
                    college = ds.getString("college");
                    major = ds.getString("class");
                    opic = ds.getString("opic");
                    corp = ds.getString("corporation");
                    depart = ds.getString("department");
                    toeicS = ds.getString("toeic speaking");
                    toeic = (long) ds.get("toeic");
                    intern = (long) ds.get("intern");
                    oversea = (long) ds.get("overseas experience");
                    volun = (long) ds.get("volunteer activity");
                    award = (long) ds.get("award");
                    license = (long) ds.get("license");
                    grade = (double) ds.get("grade");

                    radar.title("테스트 차트");

                    radar.yScale().minimum(0d);
                    radar.yScale().minimumGap(0d);
                    radar.yScale().ticks().interval(2d);

                    radar.xAxis().labels().padding(5d, 5d, 5d, 5d);

                    radar.legend()
                            .align(Align.CENTER)
                            .enabled(true);

                    List<DataEntry> data = new ArrayList<>();
                    data.add(new CustomDataEntry("학점", 3.4, grade));
                    data.add(new CustomDataEntry("토익", 0, (toeic/100)));
                    data.add(new CustomDataEntry("자격증", 0, license));
                    data.add(new CustomDataEntry("인턴경험", 1, intern));
                    data.add(new CustomDataEntry("수상경험", 1, award));

                    Set set = Set.instantiate();
                    set.data(data);
                    Mapping userData = set.mapAs("{ x: 'x', value: 'value' }");
                    Mapping saraminData = set.mapAs("{ x: 'x', value: 'value2' }");

                    Line shamanLine = radar.line(userData);
                    shamanLine.name("사용자");
                    shamanLine.markers()
                            .enabled(true)
                            .type(MarkerType.CIRCLE)
                            .size(3d);

                    Line warriorLine = radar.line(saraminData);
                    warriorLine.name("사람인");
                    warriorLine.markers()
                            .enabled(true)
                            .type(MarkerType.CIRCLE)
                            .size(3d);

                    radar.tooltip().format("Value: {%Value}");

                    anyChartView.setChart(radar);
                    System.out.println("da"+saraminArr);
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

    private class CustomDataEntry extends ValueDataEntry {
        public CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }

}
