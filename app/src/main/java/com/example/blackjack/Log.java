package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Log extends AppCompatActivity {

    EditText edt = (EditText) findViewById(R.id.edt);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
    }

    public void start(View v){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", edt.getText().toString());
        startActivity(i);
    }

}