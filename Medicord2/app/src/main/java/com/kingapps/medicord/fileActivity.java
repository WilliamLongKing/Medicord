package com.kingapps.medicord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class fileActivity extends AppCompatActivity {
    private static final String TAG = "fileActivity";

    //vars
    private ArrayList<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        Log.d(TAG, "onCreate: started.");
        initData();
    }

    private void initData() {
        Log.d(TAG, "initData: preparing data.");
        mData.add("Name: ");
        mData.add("Age: ");
        mData.add("Gender: ");
        mData.add("DOB: ");
        mData.add("1");
        mData.add("2");
        mData.add("3");
        mData.add("4");
        mData.add("5");
        mData.add("6");
        mData.add("7");
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
