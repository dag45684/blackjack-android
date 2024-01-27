package com.example.blackjack;

import android.content.Context;

public class Card {

    final int id;

    public Card(String input, Context ct){
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
                value.append("back_card");
        }
        if (input.matches("10.")){
            value.append("10");
        }else if (input.matches("[2-9KQJA].")) {
            switch (input.charAt(0)){
                case 'J':
                    value.append("jack");
                    break;
                case 'Q':
                    value.append("queen");
                    break;
                case 'K':
                    value.append("king");
                    break;
                case 'A':
                    value.append("1");
                    break;
                default:
                    value.append(input.charAt(0));
            }
        }
        this.id = ct.getResources().getIdentifier(value.toString(), "drawable", ct.getPackageName());
    }

    public int getId(){
        return this.id;
    }
}
