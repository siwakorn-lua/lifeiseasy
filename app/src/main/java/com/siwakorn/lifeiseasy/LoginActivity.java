package com.siwakorn.lifeiseasy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String ticket = sp.getString("ticket",null);
        if(ticket!=null){
            Log.i("debug","logged in:"+ticket);
            Intent home = new Intent(LoginActivity.this, MainActivity.class);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(home);
            finish();
            return;
        }

        login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://account.it.chula.ac.th/login?service=jj%3A%2F%2Flogin.sso"));
                startActivity(intent);
            }
        });
    }
}
