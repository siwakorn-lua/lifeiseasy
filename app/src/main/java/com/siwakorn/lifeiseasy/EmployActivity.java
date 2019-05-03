package com.siwakorn.lifeiseasy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EmployActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employ);

        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        final String ticket = sharedPreferences.getString("ticket", "");
        final Bundle extras = getIntent().getExtras();

        TextView name = findViewById(R.id.name);
        TextView job = findViewById(R.id.job);
        TextView detail = findViewById(R.id.jobDescription);
        TextView price = findViewById(R.id.price);
        TextView date = findViewById(R.id.date);

        name.setText(extras.getString("name"));
        job.setText(extras.getString("job"));
        detail.setText(extras.getString("detail"));
        price.setText(extras.getString("price"));
        date.setText(extras.getString("date"));

        Button hireButton = findViewById(R.id.hireButton);
        hireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue requestQueue = Volley.newRequestQueue(EmployActivity.this);
                String url = "http://54.179.153.2:9000/employ?ticket=" + ticket;
                JSONObject postParams = new JSONObject();
                try {
                    postParams.put("id", extras.getString("jobID"));
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                            url,
                            postParams,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getBoolean("ok")) {
                                            Toast.makeText(EmployActivity.this, "Success", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(EmployActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(EmployActivity.this, "Error", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(EmployActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    requestQueue.add(jsonObjectRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
