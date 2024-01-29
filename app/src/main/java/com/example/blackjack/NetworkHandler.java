package com.example.blackjack;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkHandler implements  Runnable{

    String username;

    public NetworkHandler (String username){
        this.username = username;
    }

    @Override
    public void run() {
        try {
            MainActivity.s = new Socket("83.54.41.84", 9999);
            MainActivity.out = new PrintWriter(MainActivity.s.getOutputStream());
            MainActivity.in = new BufferedReader(new InputStreamReader(MainActivity.s.getInputStream()));
            MainActivity.out.println("nueva:"+username); //TODO: Implement nickname input
            MainActivity.out.flush();
            String resp = MainActivity.in.readLine();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void contact(String command, Context c) throws InterruptedException {
        Thread aux = new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.out.println(command);
                MainActivity.out.flush();
                try {
                    String s = MainActivity.in.readLine();
                    parseHand(s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        aux.start();
        Thread.sleep(100);
        aux.join();
        ((MainActivity) c).displayHand();
    }

    protected static void parseHand(String hand) {
        String[] info = hand.split("<-->");
        Matcher m = Pattern.compile("\\w+[\u2660\u2665\u2666\u2663]|\\*\\*").matcher(info[0]);
        ArrayList<String> temp = new ArrayList<>();
        while (m.find()) temp.add(m.group());
        MainActivity.banca = temp;
        m = Pattern.compile("\\w+[\u2660\u2665\u2666\u2663]|\\*\\*").matcher(info[1]);
        temp = new ArrayList<>();
        while (m.find()) temp.add(m.group());
        MainActivity.player = temp;

        if (info.length>2){
            if(info[2].matches("(\\w+.)+")){
                Log.d("semen", "que cojonazos pasa aqui" + info[2]);
                MainActivity.resp = info[2];
                MainActivity.activeHand = false;
            }
        }else {
            Log.d("semen", "que cojonazos pasa aqui");
            MainActivity.resp = "Mano en curso";
            MainActivity.activeHand = true;
        }
    }
}
