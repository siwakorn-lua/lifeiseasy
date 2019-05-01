package com.siwakorn.lifeiseasy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, null);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        String ticket = sharedPreferences.getString("ticket", "");

        final TextView memberID = view.findViewById(R.id.memberID);
        final TextView username = view.findViewById(R.id.username);
        final TextView fullname = view.findViewById(R.id.fullname);
        final TextView email = view.findViewById(R.id.email);

        final RecyclerView jobListRecycler = view.findViewById(R.id.jobListRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        jobListRecycler.setLayoutManager(layoutManager);

        final RecyclerView employmentRecycler = view.findViewById(R.id.employmentRecycler);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        employmentRecycler.setLayoutManager(layoutManager1);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "http://54.179.153.2:9000/me?ticket=" + ticket;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject profile = response.getJSONObject("profile");
                            memberID.setText(profile.getString("_id"));
                            username.setText(profile.getString("username"));
                            fullname.setText(profile.getString("gecos"));
                            email.setText(profile.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray job = response.getJSONArray("job");
                            RecyclerView.Adapter jobAdapter = new jobAdapter(job);
                            jobListRecycler.setAdapter(jobAdapter);
                        } catch (JSONException e) {

                        }

                        try {
                            JSONArray employment = response.getJSONArray("employment");
                            RecyclerView.Adapter employmentAdapter = new employmentAdapter(employment);
                            employmentRecycler.setAdapter(employmentAdapter);
                        } catch (JSONException e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(jsonObjectRequest);

        return view;
    }

    public class jobAdapter extends RecyclerView.Adapter<jobAdapter.MyViewHolder> {
        private JSONArray mDataset;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, job, price, date;

            public MyViewHolder(View v) {
                super(v);
                job = v.findViewById(R.id.job);
                price = v.findViewById(R.id.price);
                date = v.findViewById(R.id.date);
            }
        }

        public jobAdapter(JSONArray myDataset) {
            mDataset = myDataset;
        }

        @Override
        public jobAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.job_item_profile, parent, false);
            jobAdapter.MyViewHolder vh = new jobAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(jobAdapter.MyViewHolder holder, int position) {
            try {
                holder.job.setText(mDataset.getJSONObject(position).getString("name"));
                holder.price.setText(mDataset.getJSONObject(position).getString("price"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'");
                Date date = simpleDateFormat.parse(mDataset.getJSONObject(position).getString("date"));
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                holder.date.setText(outputFormat.format(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.length();
        }
    }

    public class employmentAdapter extends RecyclerView.Adapter<employmentAdapter.MyViewHolder> {
        private JSONArray mDataset;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, job, price, date;

            public MyViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.name);
                job = v.findViewById(R.id.job);
                price = v.findViewById(R.id.price);
                date = v.findViewById(R.id.date);
            }
        }

        public employmentAdapter(JSONArray myDataset) {
            mDataset = myDataset;
        }

        @Override
        public employmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.employment_item_profile, parent, false);
            employmentAdapter.MyViewHolder vh = new employmentAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(employmentAdapter.MyViewHolder holder, int position) {
            try {
                holder.name.setText(mDataset.getJSONObject(position).getString("provider"));
                holder.job.setText(mDataset.getJSONObject(position).getString("name"));
                holder.price.setText(mDataset.getJSONObject(position).getString("price"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'");
                Date date = simpleDateFormat.parse(mDataset.getJSONObject(position).getString("date"));
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                holder.date.setText(outputFormat.format(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mDataset.length();
        }
    }

}