package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static Socket s;
    public static PrintWriter out;
    public static BufferedReader in;
    public static RecyclerView rvb, rvp;
    public static CardAdapter cai, cap;
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
        rvb.setLayoutManager(new LinearLayoutManager(this));
        rvp = (RecyclerView) findViewById(R.id.rvplayer);
        rvp.setHasFixedSize(true);
        rvp.setLayoutManager(new LinearLayoutManager(this));

        contactServer("deal");

        displayHand();
        rvb.getAdapter().notifyDataSetChanged();
        rvp.getAdapter().notifyDataSetChanged();
    }

    public void playerAction(View v) {
        contactServer(v.getTag().toString());
    }

    private void contactServer(String command) {
        try {
            r.contact(command);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void parseHand(String hand) {
        String[] info = hand.split("<-->");
        Matcher m = Pattern.compile("\\w+[\u2660\u2665\u2666\u2663]|\\*\\*").matcher(info[0]);
        ArrayList<String> temp = new ArrayList<>();
        while (m.find()) temp.add(m.group());
        banca = temp;
        m = Pattern.compile("\\w+[\u2660\u2665\u2666\u2663]|\\*\\*").matcher(info[1]);
        temp = new ArrayList<>();
        while (m.find()) temp.add(m.group());
        player = temp;

        if (info.length>2){
            if(info[2].matches("(\\w+.)+")){
                resp = info[2];
            }else{
                resp = "Mano en curso";
            }
        }
    }

    protected void displayHand() {

        runOnUiThread(()-> {
            ArrayList<Card> li = new ArrayList<>();
            for (String s : banca) {
                if(s.matches("\\w+[\u2660\u2665\u2666\u2663]||\\*\\*"))li.add(new Card(s));
            }
            rvb.setAdapter(new CardAdapter(li));
            li.clear();
            for (String s : player) {
                if (s.matches("\\w+[\u2660\u2665\u2666\u2663]|\\*\\*")) li.add(new Card(s));
            }
            rvp.setAdapter(new CardAdapter(li));
            info.setText(resp);
        });
    }
}