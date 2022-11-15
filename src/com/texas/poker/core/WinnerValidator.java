package com.texas.poker.core;

import com.texas.poker.pojo.Card;
import com.texas.poker.pojo.Player;
import com.texas.poker.util.MaxFinderUtil;

import java.util.*;
import java.util.stream.Collectors;


//Class that validates Winner results
public class WinnerValidator {


    public boolean isWinnerWon(List<List<Card>> otherPlayersCurrentList, List<Card> computedCommunityCards, List<Card> winnerCardList, Integer winnerWinLevel) {

        for(List<Card> playerCards : otherPlayersCurrentList ){

            List<Card> mergedPlayerCards = new ArrayList<Card>();
            mergedPlayerCards.addAll(computedCommunityCards);
            mergedPlayerCards.addAll(playerCards);
           boolean otherPlayerWon = isOtherPlayerWon(mergedPlayerCards,winnerWinLevel);
            if(otherPlayerWon == true){

                return false;   
            }
        }

        return true;
    }

    boolean validateStraight(List<Card> cardList){
        int winnerHighCardValue = -1;
        Comparator<Integer> initialComp = Integer::compare;
        Comparator<Integer> revComp = initialComp.reversed();

        List<List<Integer>> possibleLists = new ArrayList<List<Integer>>();

        List<Integer> cardValues= new ArrayList<Integer>();
        Iterator<Card> cardIterator = cardList.iterator();
        while (cardIterator.hasNext()) {
            Card card = cardIterator.next();
            if(!cardValues.contains(card.value)) { //remove same card with multi-color for straight
                cardValues.add(card.value);
                if (card.value == 14)    //adds zero for Ace
                    cardValues.add(0);
            }
        }
        Collections.sort( cardValues, revComp); //reverse order sorted collection
        if(cardValues.size()<5){
            return false;
        }
        else {
            for (int i = 0; i < cardValues.size() - 4; i++) {
                List<Integer> cardListForConsecutiveCheck = new ArrayList<Integer>();
                int currentStartCard =  cardValues.get(i);
                cardListForConsecutiveCheck.add(currentStartCard);

                for (int j = i + 1; (j < cardValues.size() && currentStartCard - cardValues.get(j)<5 ); j++) {
                    cardListForConsecutiveCheck.add(cardValues.get(j));
                }
                if(cardListForConsecutiveCheck.size()==5){
                     winnerHighCardValue = cardListForConsecutiveCheck.get(0);
                    return true;

                }
            }

        }

        return false;

    }

    boolean validateFlush(List<Card> cardList){
        MaxLevelResolver maxLevelResolver = new MaxLevelResolver();
        char color =  maxLevelResolver.checkForFlush(cardList,5);
        if(!(color == 'o')) {
            return true;
        }
        return false;
    }

    boolean validateStraightFlush(List<Card> cardList) {
        MaxLevelResolver maxLevelResolver = new MaxLevelResolver();
        char color =  maxLevelResolver.checkForFlush(cardList,5);
        boolean isStraightFlushPossible = false;
        if(!(color == 'o')) {
            List<Card> maxColorCardList = cardList.stream().filter(card -> (card.type == color)).collect(Collectors.toList());
            System.out.println(maxColorCardList);
             isStraightFlushPossible =  validateStraight(maxColorCardList);

        }
        return isStraightFlushPossible;
    }
    boolean validateFourCard(List<Card> cardList, Map<Integer, Integer> denominationCounterMap) {

        if (denominationCounterMap.values().contains(4))
            return true;
        return false;

    }
    boolean validateFullHouse(List<Card> cardList, Map<Integer, Integer> denominationCounterMap) {

            if (denominationCounterMap.values().contains(3) && denominationCounterMap.values().contains(2)) {
                return true;
            }
        return false;

    }

    boolean validateThreeOfaKind(List<Card> cardList, Map<Integer, Integer> denominationCounterMap) {

        if (denominationCounterMap.values().contains(3)) {
            return true;
        }
        return false;

    }
    boolean validatePair(List<Card> cardList, Map<Integer, Integer> denominationCounterMap) {

        if (denominationCounterMap.values().contains(2)) {
            return true;
        }
        return false;

    }

    boolean validateTwoPairs(List<Card> cardList, Map<Integer, Integer> denominationCounterMap) {
        int counter = 0;
        for (Integer key : denominationCounterMap.keySet()) {
            if (2 == (denominationCounterMap.get(key)))
                counter++;
        }
        if(counter== 2)
            return true;
        return false;
    }

    boolean validateHighCard(List<Card> cardList)
    {
        Comparator<Integer> initialComp = Integer::compare;
        Comparator<Integer> revComp = initialComp.reversed();
        return true;
    }


    boolean isOtherPlayerWon(List<Card> cardList, Integer winnerWinLevel) {
        int levelCheck = 1;
        boolean isOtherPlayerWon = true;

        Map<Integer, Integer> denominationCounterMap = getDenominationCount(cardList);
        boolean result= false;

        while (levelCheck <= winnerWinLevel) {
            switch (levelCheck) {
                case 1:
                    result =  validateStraightFlush(cardList);
                    break;
                case 2:
                    result =  validateFourCard(cardList, denominationCounterMap);
                    break;

                case 3:
                    result =  validateFullHouse(cardList, denominationCounterMap);
                    break;
                case 4:
                    result =  validateFlush(cardList);
                    break;
                case 5:
                    result =  validateStraight(cardList);
                    break;
                case 6:
                    result =  validateThreeOfaKind(cardList,denominationCounterMap);
                    break;
                case 7:
                    result =  validateTwoPairs(cardList, denominationCounterMap);
                    break;
                case 8:
                    result =  validatePair(cardList, denominationCounterMap);
                    break;
                case 9:
                    result =  validateHighCard(cardList);
                    break;
                default:
                    break;

            }
            if(result == true){
                return isOtherPlayerWon ;
            }
            levelCheck++;

        }
        return false;
    }


    public Map<Integer, Integer>  getDenominationCount(List<Card> cardList) {

        Map<Integer, Integer> denominationCounterMap = new HashMap<Integer, Integer>();

        for (Card card : cardList) {
            Integer count = denominationCounterMap.get(card.value);
            denominationCounterMap.put(card.value, (count == null) ? 1 : count + 1);
        }
        return denominationCounterMap;
    }


}
