package com.example.joan.practica2ame_joajara_i_aleixiglesias;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {
    Button saveBtnSignUp;
    EditText name, objective;
    EditText username;
    EditText pass;
    EditText confpass;
    EditText length;
    DatabaseHelper my_db;

    String[] t_feet, t_footBefore, t_stepsDone, t_date;
    EncryptionPass encrypt;
    String[] users;
    String[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        t_stepsDone = new String[15];
        t_feet = new String[15];
        t_footBefore = new String[15];
        t_date = new String[15];
        my_db = new DatabaseHelper(this);
        encrypt = new EncryptionPass();

        saveBtnSignUp = findViewById(R.id.saveSignUp);
        name =  findViewById(R.id.name);
        objective = findViewById(R.id.objective);
        username =  findViewById(R.id.user);
        pass =  findViewById(R.id.password);
        confpass =  findViewById(R.id.confirmPassword);
        length =  findViewById(R.id.lengthFeet);

        final Cursor res = my_db.getAllData();
        ids = new String[15];
        users = new String[15];
        int i=0;
        while(res.moveToNext()){
            ids[i]= res.getString(0);
            users[i]=res.getString(2);
            i++;
        }
        final int j = i;
        saveBtnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (j != 15) {

                    String userName = String.valueOf(username.getText());
                    int i=0;
                    boolean auxiliar = true;
                    while(i<j && auxiliar){
                        if(users[i].equals(userName))auxiliar = false;
                        i++;
                    }
                    final int j = i;
                    String feet = String.valueOf(length.getText());
                    String names = String.valueOf(name.getText());
                    String password = String.valueOf(pass.getText());
                    String confirm = String.valueOf(confpass.getText());
                    String obj = String.valueOf(objective.getText());
                    try {
                        password = encrypt.encrypt(password);
                        confirm = encrypt.encrypt(confirm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ITALY);
                    Date d = Calendar.getInstance().getTime();
                    String dayOfTheWeek = sdf.format(d);

                    if (!obj.equals("") &&!feet.equals("") && !password.equals("") && !confirm.equals("") && !names.equals("") && !userName.equals("")) {
                        if (password.equals(confirm)) {
                            if(auxiliar) {
                                t_stepsDone[0]="0";
                                t_feet[0]=feet;

                                t_footBefore[0]="0";
                                t_date[0]=dayOfTheWeek;
                                boolean aux = my_db.insertData(names,userName,password,"1", EncryptionPass.transformArraytoString(t_feet), EncryptionPass.transformArraytoString(t_stepsDone), EncryptionPass.transformArraytoString(t_footBefore),EncryptionPass.transformArraytoString(t_date),obj);

                                if (aux) {
                                    Toast.makeText(Main2Activity.this, "Data inserted correctly.",
                                            Toast.LENGTH_SHORT).show();

                                    Intent mainSignUp = new Intent(getApplicationContext(), Main5Activity.class);
                                    Bundle information = new Bundle();
                                    information.putString("id",String.valueOf(j));
                                    information.putString("name", names);
                                    information.putString("user", userName);
                                    information.putString("pass", password);
                                    information.putString("feet", Arrays.toString(t_feet));
                                    information.putString("days", "1");
                                    information.putString("steps", Arrays.toString(t_stepsDone));
                                    information.putString("feetBefore", Arrays.toString(t_footBefore));
                                    information.putString( "dayOfTheWeek",Arrays.toString(t_date));
                                    information.putString("objective", obj);
                                    mainSignUp.putExtras(information);
                                    startActivity(mainSignUp);
                                } else {
                                    Toast.makeText(Main2Activity.this, "There was an error and the data wasn't inserted",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(Main2Activity.this, "This username is already in use. Please change your username.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Main2Activity.this, "The password is incorrect. Have to be the same.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Main2Activity.this, "You have to complete all the backets.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Main2Activity.this, "You can only store 15 users, sign in or go into anonymous.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
