package com.example.joan.practica2ame_joajara_i_aleixiglesias;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {
    EditText username, password;
    EncryptionPass encrypt;
    String[] id,users, passw, name_C, days, foot, stepsDone, meters, dataOfweek, obj;
    DatabaseHelper my_db;
    Cursor res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        encrypt = new EncryptionPass();
        my_db = new DatabaseHelper(this);
        Button logInBtn = findViewById(R.id.logIn);
        username = findViewById(R.id.usernameLogIn);
        password = findViewById(R.id.passwordLogIn);


        res = my_db.getAllData();
        passw = new String[15];
        users = new String[15];
        id = new String[15];
        obj = new String[15];
        name_C=new String[15];
        days=new String[15];
        foot = new String[15];
        stepsDone = new String[15];
        meters = new String[15];
        dataOfweek = new String[15];
        int i=0;
        while(res.moveToNext()){
            id[i] = res.getString(0);
            name_C[i]=res.getString(1);
            users[i]=res.getString(2);
            passw[i]= res.getString(3);
            days[i]=res.getString(4);
            foot[i] = res.getString(5);
            stepsDone[i] = res.getString(6);
            meters[i] = res.getString(7);
            dataOfweek[i] = res.getString(8);
            obj[i]= res.getString(9);
            i++;
        }
        final int j = i;


        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = String.valueOf(password.getText());
                try {
                    pass = encrypt.encrypt(pass);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                String user = String.valueOf(username.getText());
                int i=0;
                boolean auxiliar = false;
                while(i<j && !auxiliar){
                    if(users[i].equals(user) && pass.equals(passw[i]))auxiliar = true;
                    i++;
                }
                i--;
                if(!pass.equals("") && !user.equals("")) {
                    if(auxiliar) {
                        Intent mainLogin = new Intent(getApplicationContext(), Main5Activity.class);
                        Bundle information = new Bundle();
                        information.putString("id", id[i]);
                        information.putString("name", name_C[i]);
                        information.putString("user", users[i]);
                        information.putString("pass", passw[i]);
                        information.putString("feet", foot[i]);
                        information.putString("days", days[i]);
                        information.putString("steps", stepsDone[i]);
                        information.putString("feetBefore", meters[i]);
                        information.putString("dayOfTheWeek", dataOfweek[i]);
                        information.putString("objective", obj[i]);
                        mainLogin.putExtras(information);
                        startActivity(mainLogin);
                    }else{
                        Toast.makeText(Main3Activity.this, "Error, the username or the password are incorrect.",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Main3Activity.this, "You have to complete all the backets.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
