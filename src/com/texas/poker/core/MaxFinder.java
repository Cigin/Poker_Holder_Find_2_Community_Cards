package com.texas.poker.core;

import com.texas.poker.pojo.Card;
import com.texas.poker.pojo.Player;
import com.texas.poker.util.MaxFinderUtil;

import java.util.stream.Collectors;
import java.util.*;


//Class to find winner combinations
public class MaxFinder {

   /* def make_the_winner(players_csv_file, first_three_community_cards, the winner):

            return ["4th Card", "5th Card"]


    */
    //Calculates Winner Level
    List<String> makeTheWinner(List<Player> playersList, List<Card> usedCards, List<Card> communityCardsList, String winner) {

        List<Card> winnerCardList = new ArrayList<Card>();
        List<Card> winnerTwoCards = new ArrayList<Card>();
        List<Card> allCardsUsedList = new ArrayList<Card>();
        MaxLevelResolver maxLevelResolver = new MaxLevelResolver();

       // List<Card> communityCardList = new ArrayList<Card>();

        List<List<Card>> otherPlayersCurrentList =  MaxFinderUtil.createCardListAllPlayers(playersList, communityCardsList, winnerCardList, winner, winnerTwoCards, allCardsUsedList);
        boolean result = false, winNotPossible = false;
        Map<Integer, Integer> denominationCounterMap = getDenominationCount(winnerCardList);

        List<Card> newCommunityCardList = new ArrayList<Card>();//Variable that bring result
        int level = 1;
        List<String> finalResult = new ArrayList<String>();


        while(!(result ||  level > 8)) {
            switch (level) {
                case 1:
                    result = maxLevelResolver.checkForStraightFlush(winnerCardList, true, allCardsUsedList,otherPlayersCurrentList,newCommunityCardList);
                    break;
                case 2:
                    result = maxLevelResolver.checkCardCounts(winnerCardList,  allCardsUsedList, denominationCounterMap, otherPlayersCurrentList,newCommunityCardList, winnerTwoCards, level);
                    break;

                case 3:
                    result = maxLevelResolver.checkCardCounts(winnerCardList,  allCardsUsedList,denominationCounterMap,otherPlayersCurrentList,newCommunityCardList, winnerTwoCards, level);
                    break;
                case 4:
                    result =  maxLevelResolver.checkIsFlushPossible(winnerCardList, allCardsUsedList,otherPlayersCurrentList,newCommunityCardList);
                    break;
                case 5:
                    result =   maxLevelResolver.checkForStraight(winnerCardList, false, 'o', allCardsUsedList,otherPlayersCurrentList,newCommunityCardList);
                    break;
                case 6:
                    result = maxLevelResolver.checkCardCounts(winnerCardList,  allCardsUsedList,denominationCounterMap,otherPlayersCurrentList,newCommunityCardList, winnerTwoCards, level);
                    break;
                case 7:
                    result = maxLevelResolver.checkCardCounts(winnerCardList,  allCardsUsedList,denominationCounterMap,otherPlayersCurrentList,newCommunityCardList, winnerTwoCards, level);
                    break;
                case 8:
                    result = maxLevelResolver.checkCardCounts(winnerCardList,  allCardsUsedList,denominationCounterMap,otherPlayersCurrentList,newCommunityCardList, winnerTwoCards, level);
                    break;

            }
            if (result == true) {
                System.out.println("WON @ "+ "level"+level);

                for(Card card: newCommunityCardList){
                    finalResult.add(card.type+(card.denomination));
                }
                return finalResult;
            }
            newCommunityCardList.clear();

            level++;

        }


        if(level > 8)
        {
            System.out.println("NotPossible");
        }
        return finalResult;

    }

    public  boolean isWinnerWon(List<Card> winnerCardList, WinnerValidator winnerValidator, List<List<Card>> otherPlayersCurrentList, List<Card> computedcommunityCards) {
        boolean won;
        int winnerWinLevel = 1;

        won = winnerValidator.isWinnerWon(otherPlayersCurrentList, computedcommunityCards, winnerCardList, winnerWinLevel);
        return won;
    }

    private Map<Integer, Integer>  getDenominationCount(List<Card> cardList) {

        Map<Integer, Integer> denominationCounterMap = new HashMap<Integer, Integer>();

        for (Card card : cardList) {
            Integer count = denominationCounterMap.get(card.value);
            denominationCounterMap.put(card.value, (count == null) ? 1 : count + 1);
        }
        return denominationCounterMap;
    }

}


