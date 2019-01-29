package com.example.joan.practica2ame_joajara_i_aleixiglesias;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

public class Main6Activity extends AppCompatActivity {
    String name, id ;
    String day, date;
    String username ;
    String password, obj ;
    Button back;
    int days, cont;
    float cont2 , feet;
    GraphView graph, graph2;
    String[] t_feet, t_footBefore, t_stepsDone, t_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        t_stepsDone = new String[15];
        t_feet = new String[15];
        t_footBefore = new String[15];
        t_date = new String[15];

        Bundle parameters = this.getIntent().getExtras();

        if (parameters != null) {
            id = parameters.getString("id");
            obj = parameters.getString("objective");
            name = parameters.getString("name");
            username = parameters.getString("user");
            password = parameters.getString("pass");
            day = parameters.getString("days");
            String foot = parameters.getString("feet").replace("[","");
            foot = foot.replace("]","");
            String footBefore = parameters.getString("feetBefore").replace("[","");
            footBefore=footBefore.replace("]","");
            date = parameters.getString("dayOfTheWeek").replace("[","");
            date = date.replace("]","");
            String stepsDone = parameters.getString("steps").replace("[","");
            stepsDone=stepsDone.replace("]","");

            if (day.equals("")) days = 1;
            else days = Integer.parseInt(day);

            if(date!=null){
                if (!date.equals("")) {
                    t_date = date.split(", ");
                } else {
                    t_date[days - 1] = "";
                }
            }else{
                t_date[days - 1] = "";
            }

            if(foot!=null) {
                if (!foot.equals("")) {
                    t_feet = foot.split(", ");
                    feet = Float.parseFloat(t_feet[days - 1])/100;
                } else {
                    feet = (float) 0.0;
                }
            }else {
                feet = (float) 0.0;
            }

            if(footBefore!=null) {
                if (!footBefore.equals("")) {
                    t_footBefore = footBefore.split(", ");
                    cont2 = Float.parseFloat(t_footBefore[days - 1]);
                } else {
                    t_footBefore[days - 1] = "0";
                    cont2 = 0;
                }
            }else {
                t_footBefore[days - 1] = "0";
                cont2 = 0;
            }

            if(stepsDone!=null) {
                if (!stepsDone.equals("")) {
                    t_stepsDone = stepsDone.split(", ");
                    cont = Integer.parseInt(t_stepsDone[days - 1]);
                } else {
                    t_stepsDone[days - 1] = "0";
                    cont = 0;
                }
            }else {
                t_stepsDone[days - 1] = "0";
                cont=0;
            }

        }else {
            finish() ;
        }

        back = findViewById(R.id.buttonBack);

        graph = findViewById(R.id.graph);
        graph2 =findViewById(R.id.graph2);

        int i=0;

        while(i<7){
            if(t_footBefore[i].equals("null")) t_footBefore[i]="0";
            if(t_stepsDone[i].equals("null")) t_stepsDone[i]="0";
            i++;
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, Integer.parseInt(t_stepsDone[0])),
                new DataPoint(2, Integer.parseInt(t_stepsDone[1])),
                new DataPoint(3, Integer.parseInt(t_stepsDone[2])),
                new DataPoint(4, Integer.parseInt(t_stepsDone[3])),
                new DataPoint(5, Integer.parseInt(t_stepsDone[4])),
                new DataPoint(6, Integer.parseInt(t_stepsDone[5])),
                new DataPoint(7, Integer.parseInt(t_stepsDone[6]))
        });
        graph.addSeries(series);

        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, Float.parseFloat(t_footBefore[0])),
                new DataPoint(2, Float.parseFloat(t_footBefore[1])),
                new DataPoint(3, Float.parseFloat(t_footBefore[2])),
                new DataPoint(4, Float.parseFloat(t_footBefore[3])),
                new DataPoint(5, Float.parseFloat(t_footBefore[4])),
                new DataPoint(6, Float.parseFloat(t_footBefore[5])),
                new DataPoint(7, Float.parseFloat(t_footBefore[6]))
        });
        graph2.addSeries(series2);

// styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);
        series2.setSpacing(50);

// draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

        series2.setDrawValuesOnTop(true);
        series2.setValuesOnTopColor(Color.RED);
//series.setValuesOnTopSize(50);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conf = new Intent(getApplicationContext(), Main5Activity.class);
                Bundle information = new Bundle();
                information.putString("id", id);
                information.putString("name", name);
                information.putString("user", username);
                information.putString("pass", password);
                information.putString("days", day);
                information.putString("steps", EncryptionPass.transformArraytoString(t_stepsDone));
                information.putString("feet", EncryptionPass.transformArraytoString(t_feet));
                information.putString("feetBefore", EncryptionPass.transformArraytoString(t_footBefore));
                information.putString("dayOfTheWeek",EncryptionPass.transformArraytoString(t_date));
                information.putString("objective", obj);
                conf.putExtras(information);
                startActivity(conf);
                finish() ;
            }
        });

    }
}
