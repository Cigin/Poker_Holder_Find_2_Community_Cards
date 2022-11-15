package com.texas.poker.util;

import com.texas.poker.pojo.Card;
import com.texas.poker.pojo.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileUtil {

    public static List<Player> readCSVFile(List<Card> usedCards) throws IOException {

        String line = "";
        String splitBy = ",";

        List<Player> playersList= new ArrayList<Player>();

        BufferedReader br = new BufferedReader(new java.io.FileReader("D:\\KPMG\\players.csv"));

        while ((line = br.readLine()) != null)
        {

            String[] player = line.split(splitBy);
            if(!player[1].equals("Card1") ){
                Player p = new Player(player);
                usedCards.add(p.card1);
                usedCards.add(p.card2);
                playersList.add(p);

            }else
                continue; // case for header

        }
        Iterator<Card> iterator = usedCards.iterator();

        return playersList;

    }

    public static  List<Card> communityCardParser(String communityCardsStr){
        String splitBy = ",";
        List<Card> communityCardList= new ArrayList<Card>();


        String[] cards = communityCardsStr.split(splitBy);
        communityCardList.add(new Card(cards[0]));
        communityCardList.add(new Card(cards[1]));
        communityCardList.add(new Card(cards[2]));

        return communityCardList;

    }
}