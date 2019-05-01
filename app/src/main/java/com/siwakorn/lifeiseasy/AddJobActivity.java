package com.siwakorn.lifeiseasy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddJobActivity extends Fragment {

    private EditText priceField;
    private CalendarView jobDateCalendar;
    private String jobValue, jobDate;
    private Button addButton;
    private Toast priceNullError;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_job, null);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        final String ticket = sharedPreferences.getString("ticket", "");

        priceField = view.findViewById(R.id.priceField);

        jobDateCalendar = view.findViewById(R.id.jobDate);
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

        Spinner jobSpinner = view.findViewById(R.id.jobSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
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

        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!priceField.getText().toString().equals("")) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    String url = "http://54.179.153.2:9000/job?ticket=" + ticket;
                    JSONObject postParams = new JSONObject();
                    try {
                        postParams.put("name", jobValue);
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
                                                Toast.makeText(getContext(), "Success.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getContext(), MainActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), "Error, try again.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                        requestQueue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (priceNullError == null) {
                        priceNullError = Toast.makeText(getContext(), "Please enter the price", Toast.LENGTH_LONG);
                        priceNullError.show();
                    } else {
                        priceNullError.cancel();
                        priceNullError = Toast.makeText(getContext(), "Please enter the price", Toast.LENGTH_LONG);
                        priceNullError.show();
                    }
                }
            }
        });

        return view;
    }
}
