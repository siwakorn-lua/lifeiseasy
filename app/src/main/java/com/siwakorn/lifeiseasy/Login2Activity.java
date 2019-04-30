package com.siwakorn.lifeiseasy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Login2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Uri uri = getIntent().getData();
        String ticket = uri.getQueryParameter("ticket");
        Log.i("debug", ticket);

        String url = "http://54.179.153.2:9000/login?ticket="+ticket;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET,
            url,
            (String) null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("test",response.toString());
                    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
                    Intent home = new Intent(Login2Activity.this, MainActivity.class);
                    startActivity(home);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Login2Activity.this, "network error:" + error.getMessage(), Toast.LENGTH_LONG).show();
                }
             }
        );
        queue.add(jsonObjectRequest);

    }
}
