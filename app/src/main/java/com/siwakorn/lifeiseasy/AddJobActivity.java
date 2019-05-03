package com.siwakorn.lifeiseasy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddJobActivity extends AppCompatActivity {

    private EditText priceField, detailField;
    private CalendarView jobDateCalendar;
    private String jobValue, jobDate;
    private Button addButton;
    private Toast priceNullError, detailNullError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        SharedPreferences sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        final String ticket = sharedPreferences.getString("ticket", "");

        priceField = findViewById(R.id.priceField);
        detailField = findViewById(R.id.detail);

        jobDateCalendar = findViewById(R.id.jobDate);
        jobDateCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                jobDate = year + "-";
                if (month < 9) {
                    jobDate += "0" + (month + 1) + "-" + dayOfMonth;
                } else {
                    jobDate += (month + 1) + "-" + dayOfMonth;
                }
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        jobDate = sdf.format(new Date());

        Spinner jobSpinner = findViewById(R.id.jobSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jobListWithoutAll, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(adapter);
        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jobValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        jobValue = adapter.getItem(0).toString();

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!priceField.getText().toString().equals("") && !detailField.getText().toString().equals("")) {
                    RequestQueue requestQueue = Volley.newRequestQueue(AddJobActivity.this);
                    String url = "http://54.179.153.2:9000/job?ticket=" + ticket;
                    JSONObject postParams = new JSONObject();
                    try {
                        postParams.put("name", jobValue);
                        postParams.put("detail", detailField.getText().toString());
                        postParams.put("price", priceField.getText().toString());
                        postParams.put("date", jobDate);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                                url,
                                postParams,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            if (response.getBoolean("ok")) {
                                                Toast.makeText(AddJobActivity.this, "Success.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(AddJobActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                                        Toast.makeText(AddJobActivity.this, "Error, try again.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(AddJobActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        requestQueue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if(detailField.getText().toString().equals("")){
                        if (detailNullError == null) {
                            detailNullError = Toast.makeText(AddJobActivity.this, "Please enter your job description", Toast.LENGTH_LONG);
                            detailNullError.show();
                        } else {
                            detailNullError.cancel();
                            detailNullError = Toast.makeText(AddJobActivity.this, "Please enter your job description", Toast.LENGTH_LONG);
                            detailNullError.show();
                        }
                    }else{
                        if (priceNullError == null) {
                            priceNullError = Toast.makeText(AddJobActivity.this, "Please enter your price", Toast.LENGTH_LONG);
                            priceNullError.show();
                        } else {
                            priceNullError.cancel();
                            priceNullError = Toast.makeText(AddJobActivity.this, "Please enter your price", Toast.LENGTH_LONG);
                            priceNullError.show();
                        }
                    }
                }
            }
        });

    }
}
