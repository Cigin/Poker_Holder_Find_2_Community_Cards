package com.texas.poker.core;

import com.texas.poker.pojo.Card;
import com.texas.poker.pojo.Player;
import com.texas.poker.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Main Class
public class Main {
    public static void main(String[] args) {

        List<Card> usedCards= new ArrayList<Card>();
        List<Player> playersList= new ArrayList<Player>();


        try {
            playersList = FileUtil.readCSVFile(usedCards); //Gets List of used cards and players-card list
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String communityCardsStr= "S3,H10,Cj";
        List<Card> communityCardsList = FileUtil.communityCardParser(communityCardsStr);

        usedCards.addAll(communityCardsList);
        MaxFinder maxFinder = new MaxFinder();
        List<String> result = maxFinder.makeTheWinner(playersList,usedCards,communityCardsList,"John");
        System.out.println(result);

        System.out.println("communityCardsStr:"+communityCardsStr);



    }



}