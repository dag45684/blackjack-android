package com.example.blackjack;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class Card {

    private Drawable d;

    public Card(String input){
        StringBuilder value = new StringBuilder();
        char symbol = input.replaceAll("\\w+", "").charAt(0);
        switch (symbol){
            case '\u2660':
                value.append("spades_");
                break;
            case '\u2665':
                value.append("hearts_");
                break;
            case '\u2666':
                value.append("diamonds_");
                break;
            case '\u2663':
                value.append("clubs_");
                break;
            default:
                value = new StringBuilder().append("card_back");
        }
        if (input.matches("10.")){
            value.append("10");
        }else if (input.matches("[1-9KQJ].")) {
            value.append(input.charAt(0));
        }
        this.d = Drawable.createFromPath("C:\\Users\\Carlos\\AndroidStudioProjects\\Blackjack\\app\\src\\main\\res\\drawable"+value.toString()+".png");
    }

    public Drawable getD(){
        return this.d;
    }

}
