package com.example.blackjack;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkHandler implements  Runnable{


    @Override
    public void run() {
        try {
            MainActivity.s = new Socket("83.54.41.84", 9999);
            MainActivity.out = new PrintWriter(MainActivity.s.getOutputStream());
            MainActivity.in = new BufferedReader(new InputStreamReader(MainActivity.s.getInputStream()));
            MainActivity.out.println("nueva:player"); //TODO: Implement nickname input
            MainActivity.out.flush();
            String resp = MainActivity.in.readLine();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void contact(String command) throws InterruptedException {
        Thread aux = new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.out.println(command);
                MainActivity.out.flush();
                try {
                    String s = MainActivity.in.readLine();
                    MainActivity.parseHand(s);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        aux.start();
        Thread.sleep(50);
        aux.join();
    }
}
