package com.example.joan.practica2ame_joajara_i_aleixiglesias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signUp =  findViewById(R.id.SignUp);
        Button signIn =  findViewById(R.id.SignIn);
        final Button anonymous = findViewById(R.id.Anonymous);

        anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anonymousUser = new Intent(getApplicationContext(),Main4Activity.class);
                startActivity(anonymousUser);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logIn = new Intent(getApplicationContext(),Main3Activity.class);
                startActivity(logIn);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(register);
            }
        });
    }
}
