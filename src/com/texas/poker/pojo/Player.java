package com.texas.poker.pojo;

public class Player {

    public String name;
    public Card card1;
    public Card card2;
    public Player(String[] player){
        name = player[0];

        card1 = new Card(player[1]);
        card2 = new Card(player[2]);
    }



}
