package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Socket s;
    public static PrintWriter out;
    public static BufferedReader in;
    public static RecyclerView rvb, rvp;
    static ArrayList<String> banca, player;
    static String resp = "Mano en curso";
    TextView info;
    Thread t;
    NetworkHandler r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView) findViewById(R.id.infogame);

        t = new Thread((r = new NetworkHandler()));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        info.setText(resp);

        rvb = (RecyclerView) findViewById(R.id.rvbanca);
        rvb.setHasFixedSize(true);
        rvb.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true){
            public boolean canScrollHorizontally() {return false; }});
        rvp = (RecyclerView) findViewById(R.id.rvplayer);
        rvp.setHasFixedSize(true);
        rvp.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false){
            public boolean canScrollHorizontally() {return false; }});

        contactServer("deal", this);

        rvb.getAdapter().notifyDataSetChanged();
        rvp.getAdapter().notifyDataSetChanged();
    }

    public void playerAction(View v) {
        contactServer(v.getTag().toString(), this);
    }

    private void contactServer(String command, Context c) {
        try {
            r.contact(command, this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void displayHand() {

        runOnUiThread(()-> {
            ArrayList<Card> li = new ArrayList<>();
            for (String s : banca) {
                if(s.matches("\\w+[\u2660\u2665\u2666\u2663]||\\*\\*"))li.add(new Card(s, this));
            }
            rvb.setAdapter(new CardAdapter(li));
            li = new ArrayList<>();
            for (String s : player) {
                if (s.matches("\\w+[\u2660\u2665\u2666\u2663]|\\*\\*")) li.add(new Card(s, this));
            }
            rvp.setAdapter(new CardAdapter(li));
            info.setText(resp);
            Log.d("semen", "info text"+resp);
        });
    }
}