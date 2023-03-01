package com.barmej.culturalwords;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnswerActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        String title = getIntent().getStringExtra(MainActivity.BUNDLE_TITLE);
        String description = getIntent().getStringExtra(MainActivity.BUNDLE_DESCRIPTION);
        TextView textView = findViewById(R.id.text_view_answer);
        textView.setText(title + " : " + description);
    }

    public void back(View view){
        finish();
    }

}