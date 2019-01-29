package com.example.joan.practica2ame_joajara_i_aleixiglesias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main4Activity extends AppCompatActivity {
    EditText foot, goal;
    String names, id, day, date;
    String userName, obj;
    String password;
    int days;
    String[] t_feet, t_footBefore, t_stepsDone, t_date;
    DatabaseHelper my_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        t_stepsDone = new String[15];
        t_feet = new String[15];
        t_footBefore = new String[15];
        t_date = new String[15];
        my_db = new DatabaseHelper(this);
        Bundle parameters = this.getIntent().getExtras();

        if (parameters != null) {
            id = parameters.getString("id");
            names = parameters.getString("name");
            userName = parameters.getString("user");
            password = parameters.getString("pass");
            day = parameters.getString("days");
            date = parameters.getString("dayOfTheWeek");
            obj = parameters.getString("objective");

            String foot = parameters.getString("feet");
            String footBefore = parameters.getString("feetBefore");
            String stepsDone = parameters.getString("steps");

            if (day.equals("")) days = 1;
            else days = Integer.parseInt(day);

            if (foot!=null ){
                if(!foot.equals(""))t_feet = foot.split(", ");
            }

            if(footBefore!=null) {
                if (!footBefore.equals("")) {
                    t_footBefore = footBefore.split(", ");
                } else {
                    t_footBefore[days - 1] = "0";
                }
            }else {
                t_footBefore[days - 1] = "0";
            }

            if(stepsDone!=null) {
                if (!stepsDone.equals("")) {
                    t_stepsDone = stepsDone.split(", ");
                } else {
                    t_stepsDone[days - 1] = "0";
                }
            }else {
                t_stepsDone[days - 1] = "0";
            }

            if(date!=null) {
                if (!date.equals("")) {
                    t_date = date.split(", ");
                } else {
                    t_date[days - 1] = "";
                }
            }else {
                t_date[days - 1] = "";
            }

        }else {
            id="";
            names="";
            userName="";
            password="";
            obj= "";
            day="1";
            days=1;
            t_date[0]="";
            t_stepsDone[0]="0";
            t_footBefore[0]="0";
        }

        Button continueBtn = findViewById(R.id.continueAnonymous);
        foot = findViewById(R.id.lengthFeetAnonymous);
        goal = findViewById(R.id.objective2);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String footSize = String.valueOf(foot.getText());
                String goals = String.valueOf(goal.getText());
                if (!footSize.equals("") && !goals.equals("")) {
                    t_feet[days - 1] = footSize;

                    Intent mainAnonymous = new Intent(getApplicationContext(), Main5Activity.class);
                    Bundle information = new Bundle();
                    information.putString("id", id);
                    information.putString("name", names);
                    information.putString("user", userName);
                    information.putString("pass", password);
                    information.putString("days", day);
                    information.putString("feet", EncryptionPass.transformArraytoString(t_feet));
                    information.putString("feetBefore", EncryptionPass.transformArraytoString(t_footBefore));
                    information.putString("dayOfTheWeek", EncryptionPass.transformArraytoString(t_date));
                    information.putString("steps", EncryptionPass.transformArraytoString(t_stepsDone));
                    information.putString("objective", goals);
                    mainAnonymous.putExtras(information);
                    startActivity(mainAnonymous);
                    finish();
                } else {
                    Toast.makeText(Main4Activity.this, "You have to complete all the backets.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
