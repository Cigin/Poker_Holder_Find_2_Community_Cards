package com.texas.poker.util;

import com.texas.poker.pojo.Card;
import com.texas.poker.pojo.Player;

import java.util.*;
import java.util.stream.Collectors;

public class MaxFinderUtil {


    public static List<List<Card>> createCardListAllPlayers(List<Player> playersList, List<Card> communityCardsList, List<Card> winnerCardList, String winner, List<Card> winnerTwoCards, List<Card>  allCardsUsedList) {

        List<List<Card>> otherPlayersCurrentList = new ArrayList<List<Card>>();



        Iterator<Player> iterator = playersList.iterator();

        while (iterator.hasNext()) {
            List<Card> cardList = new ArrayList<Card>();
            Player player = iterator.next();
            cardList.add(player.card1);
            cardList.add(player.card2);
            allCardsUsedList.addAll(cardList);
            cardList.addAll(communityCardsList);


            if (player.name.equals(winner)) {
                winnerCardList.addAll(cardList);
                winnerTwoCards.add(player.card1);
                winnerTwoCards.add(player.card2);

            } else
                otherPlayersCurrentList.add(cardList);
        }

        allCardsUsedList.addAll(communityCardsList);
        return otherPlayersCurrentList;
    }

    public static Card generateRandomCard(List<Card> allCardsUsedList) {
        Random random = new Random();
        boolean isCardUsed = false;

        while (!(isCardUsed)) {
            int num1 = random.nextInt(14);
            List<Character> colors = Arrays.asList('C', 'S', 'D', 'H');
            int index = random.nextInt(4);


            isCardUsed = isCardUsed(num1, colors.get(index), allCardsUsedList);
            if (!isCardUsed) {
                return new Card(colors.get(index), num1);
            }
            isCardUsed = false;
        }
        return null;

    }


    public static Card generateRandomCard(int num1, List<Card> allCardsUsedList) {
        Random random = new Random();
        boolean isCardUsed = false;

        while (!(isCardUsed)) {
            List<Character> colors = Arrays.asList('C', 'S', 'D', 'H');
            int index = (int)(Math.random() * colors.size());


            isCardUsed = isCardUsed(num1, colors.get(index), allCardsUsedList);
            if (!isCardUsed) {
                return new Card(colors.get(index), num1);
            }
            isCardUsed = false;

        }
        return null;

    }

    public static Card generateRandomCard(char ch, List<Card> allCardsUsedList) {
        Random random = new Random();
        boolean isCardUsed = false;

        while (!(isCardUsed)) {
            int num1 = random.nextInt(14);

            isCardUsed = isCardUsed(num1, ch, allCardsUsedList);
            if (!isCardUsed) {
                return new Card(ch, num1);
            }
            isCardUsed = false;

        }
        return null;

    }




    public static boolean isCardUsed(Integer newCommunityCard, char color, List<Card> allCardsUsedList) {
        boolean isCardUsed = false;
        isCardUsed = checkIfCardAlreadyUsedWithColor(newCommunityCard, color, allCardsUsedList);
        return isCardUsed;
    }


    public static boolean checkIfCardAlreadyUsedWithColor(int newCommunityCard, char color, List<Card> allCardsUsedList) {

        if (allCardsUsedList.stream().filter( usedCard -> usedCard.type == color).filter(usedCard -> usedCard.value == newCommunityCard).count() ==0 ){
            return false;
        }
        return true;



    }

}
