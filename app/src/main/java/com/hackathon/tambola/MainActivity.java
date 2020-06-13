package com.hackathon.tambola;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int start;
    private int end;
    boolean isBannerVisible;
    private static final int DEFAULT_START = 1;
    private static final int DEFAULT_END = 100;
    private Button mGenerator;
    private Button mClear;
    private Button mDefault;
    private EditText mStartInterval;
    private EditText mEndInterval;
    private TextView notFoundBanner;
    private TextView numberContainer;
    private ListView generatedNumbers;
    private ArrayList<String> numbersList;
    private ArrayAdapter numbersAdapter;
    private HashMap<Integer, Integer> numbersMap;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setAction();
    }

    private void updateList(int number) {
        numbersList.add(String.valueOf(number));
        numbersAdapter.notifyDataSetChanged();
        if (isBannerVisible) hideBanner();
    }

    private void clearNumberList(){
        showBanner();
        numberContainer.setText(DEFAULT_START+"");
        numbersList.clear();
        numbersMap.clear();
        numbersAdapter.notifyDataSetChanged();
    }

    private void showBanner() {
        isBannerVisible = true;
        notFoundBanner.setVisibility(View.VISIBLE);
    }

    private void hideBanner() {
        isBannerVisible = false;
        notFoundBanner.setVisibility(View.GONE);
    }

    private void clearInterval(){
        mStartInterval.setText(String.valueOf(DEFAULT_START));
        mEndInterval.setText(String.valueOf(DEFAULT_END));
    }

    private void generateNumbers() {
        int minBound, maxBound;

        if (start <= end) {minBound = start; maxBound = end;}
        else {minBound = end; maxBound = start;}

        int number = random.nextInt(maxBound+1);

        if(numbersMap.containsKey(number)) {
            if (numbersList.size() <= (maxBound-minBound)){
                generateNumbers();
            }
        }else if (number < minBound){
            generateNumbers();
        }else if(number > maxBound){
            generateNumbers();
        }else {
            numberContainer.setText(String.valueOf(number));
            numbersMap.put(number, number);
            updateList(number);
        }

    }

    private void init() {
        start = DEFAULT_START;
        end = DEFAULT_END;
        isBannerVisible = true;
        random = new Random();
        numbersMap = new HashMap<>();
        numbersList = new ArrayList<>();
        numbersAdapter = new ArrayAdapter(MainActivity.this, R.layout.textview, numbersList);

        mGenerator = findViewById(R.id.generator);
        mClear = findViewById(R.id.clear);
        mStartInterval = findViewById(R.id.start);
        mEndInterval = findViewById(R.id.end);
        mDefault = findViewById(R.id.default_interval);
        notFoundBanner = findViewById(R.id.not_found_banner);
        numberContainer = findViewById(R.id.number);
        generatedNumbers = findViewById(R.id.generated_numbers);
        generatedNumbers.setAdapter(numbersAdapter);
    }

    private void setAction() {

        mGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIntervalChanges()){
                    generateNumbers();
                }
            }
        });

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearNumberList();
            }
        });

        mDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearInterval();
            }
        });
    }

    private boolean checkIntervalChanges() {
        int tStart = 0, tEnd = 0;

        if (!TextUtils.isEmpty(mStartInterval.getText().toString())) tStart = Integer.parseInt(mStartInterval.getText().toString());
        else return false;

        if (!TextUtils.isEmpty(mEndInterval.getText().toString())) tEnd = Integer.parseInt(mEndInterval.getText().toString());
        else return false;

        if(tStart == start && tEnd == end) return true;

        showAlertDialog(tStart, tEnd);

        return false;
    }

    private void showAlertDialog(final int tStart, final int tEnd) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Warning");
        alertDialogBuilder.setMessage("All the records will be deleted!");

        alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                start = tStart;
                end = tEnd;
                clearNumberList();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        alertDialogBuilder.create().show();

    }
}
