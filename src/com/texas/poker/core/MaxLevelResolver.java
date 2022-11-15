package com.texas.poker.core;

import com.texas.poker.pojo.Card;
import com.texas.poker.util.MaxFinderUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaxLevelResolver {

    boolean checkForStraight(List<Card> cardList, boolean isStraightFlush, char color, List<Card> allCardsUsedList, List<List<Card>> otherPlayersCurrentList, List<Card> newCommunityCardList) {
        Comparator<Integer> initialComp = Integer::compare;
        Comparator<Integer> revComp = initialComp.reversed();
        List<List<Integer>> possibleLists = new ArrayList<List<Integer>>();

        List<Integer> cardValues = new ArrayList<Integer>();
        Iterator<Card> cardIterator = cardList.iterator();
        while (cardIterator.hasNext()) {
            Card card = cardIterator.next();
            if (!cardValues.contains(card.value)) {
                cardValues.add(card.value);
                if (card.value == 14)
                    cardValues.add(0);
            }
        }
        Collections.sort(cardValues, revComp); //reverse order sorted collection

        for (int i = 0; i < cardValues.size() - 2; i++) {
            List<Integer> cardListForConsecutiveCheck = new ArrayList<Integer>();
            int currentStartCard = cardValues.get(i);
            cardListForConsecutiveCheck.add(currentStartCard);

            for (int j = i + 1; (j < cardValues.size() && currentStartCard - cardValues.get(j) < 5); j++) {
                cardListForConsecutiveCheck.add(cardValues.get(j));
            }
            if (cardListForConsecutiveCheck.size() >= 3) {
                possibleLists.add(cardListForConsecutiveCheck);
            }
        }

        if (possibleLists.isEmpty())
            return false;
        else
            return computeAndCheckStraightCards(possibleLists, isStraightFlush, color, allCardsUsedList, otherPlayersCurrentList, cardList, newCommunityCardList);

    }


    boolean checkCardCounts(List<Card> cardList,List<Card> allCardsUsedList, Map<Integer, Integer> denominationCounterMap, List<List<Card>> otherPlayersCurrentList, List<Card> newCommunityCardList, List<Card> winnerTwoCards, int level) {

      //  Card newCommunityCard = null;
        newCommunityCardList.clear();
        int communityCardCountAdded = 0;
        List<Character> colors = Arrays.asList('C', 'S', 'D', 'H');
        if (winnerTwoCards.get(0).denomination.equals(winnerTwoCards.get(1).denomination)) {

            int count = (int) cardList.stream().filter(card -> (card.value == winnerTwoCards.get(1).value)).count();
            for (int i = 0; i < colors.size() && communityCardCountAdded <= 2; i++) {
                boolean isCardUsed = false;

                isCardUsed = MaxFinderUtil.isCardUsed(winnerTwoCards.get(0).value, colors.get(i), allCardsUsedList);
                if (!isCardUsed) {
                    Card newCommunityCard = new Card(colors.get(i), winnerTwoCards.get(1).value);
                    newCommunityCardList.add(newCommunityCard);
                    communityCardCountAdded++;
                }
            }

        } else {
            int countFirst = (int) cardList.stream().filter(card -> (card.value == winnerTwoCards.get(0).value)).count();

            int countSecond = (int) cardList.stream().filter(card -> (card.value == winnerTwoCards.get(1).value)).count();

            if (countFirst > countSecond) {

                for (int i = 0; i < colors.size() && communityCardCountAdded < 2; i++) {
                    boolean isCardUsed = false;

                    isCardUsed = MaxFinderUtil.isCardUsed(winnerTwoCards.get(0).value, colors.get(i), allCardsUsedList);
                    if (!isCardUsed) {
                        Card newCommunityCard = new Card(colors.get(i), winnerTwoCards.get(0).value);
                        newCommunityCardList.add(newCommunityCard);
                        communityCardCountAdded++;
                    }
                }
            } else if (countFirst < countSecond) {

                for (int i = 0; i < colors.size() && communityCardCountAdded < 2; i++) {
                    boolean isCardUsed = false;

                    isCardUsed = MaxFinderUtil.isCardUsed(winnerTwoCards.get(1).value, colors.get(i), allCardsUsedList);
                    if (!isCardUsed) {
                        Card newCommunityCard = new Card(colors.get(i), winnerTwoCards.get(1).value);
                        newCommunityCardList.add(newCommunityCard);
                        communityCardCountAdded++;
                    }
                }

            }

            if (winnerTwoCards.get(1).value > winnerTwoCards.get(0).value) {

                for (int i = 0; i < colors.size() && communityCardCountAdded < 2; i++) {
                    boolean isCardUsed = false;

                    isCardUsed = MaxFinderUtil.isCardUsed(winnerTwoCards.get(1).value, colors.get(i), allCardsUsedList);
                    if (!isCardUsed) {
                        Card newCommunityCard = new Card(colors.get(i), winnerTwoCards.get(1).value);
                        newCommunityCardList.add(newCommunityCard);
                        communityCardCountAdded++;
                    }
                }
            }
                for (int i = 0; i < colors.size() && communityCardCountAdded < 2; i++) {
                    boolean isCardUsed = false;

                    isCardUsed = MaxFinderUtil.isCardUsed(winnerTwoCards.get(0).value, colors.get(i), allCardsUsedList);
                    if (!isCardUsed) {
                        Card newCommunityCard = new Card(colors.get(i), winnerTwoCards.get(0).value);
                        newCommunityCardList.add(newCommunityCard);
                        communityCardCountAdded++;
                    }
                }


            }

        if(communityCardCountAdded<2){
            while(communityCardCountAdded<2)
            {
               Card generatedCard = MaxFinderUtil.generateRandomCard(allCardsUsedList);
                newCommunityCardList.add(generatedCard);
                communityCardCountAdded++;

            }

        }
        List<Card> mergedList = new ArrayList<Card>();

        mergedList.addAll(cardList);
        if(!newCommunityCardList.isEmpty())
            mergedList.addAll(newCommunityCardList);


        WinnerValidator winnerValidator = new WinnerValidator();
        Map<Integer, Integer> updateddenominationCounterMap = winnerValidator.getDenominationCount(mergedList);
        if (updateddenominationCounterMap.values().contains(4) && level ==2) {
            return winnerValidator.isWinnerWon(otherPlayersCurrentList, newCommunityCardList, cardList, 2);

        }
        if (updateddenominationCounterMap.values().contains(3) && denominationCounterMap.values().contains(2) && level ==3) {
            return winnerValidator.isWinnerWon(otherPlayersCurrentList, newCommunityCardList, cardList, 3);

        }
        if (updateddenominationCounterMap.values().contains(3)&& level ==6) {
            return winnerValidator.isWinnerWon(otherPlayersCurrentList, newCommunityCardList, cardList, 6);

        }

        int counter = 0;
        for (Integer key : updateddenominationCounterMap.keySet()) {
            if (2 == (updateddenominationCounterMap.get(key)))
                counter++;
        }
        if (counter == 2 && level ==7)
            return winnerValidator.isWinnerWon(otherPlayersCurrentList, newCommunityCardList, cardList, 7);

        if (counter == 1 && level ==8)
            return winnerValidator.isWinnerWon(otherPlayersCurrentList, newCommunityCardList, cardList, 8);

    return false;

    }



    boolean checkIsFlushPossible(List<Card> cardList, List<Card> allCardsUsedList, List<List<Card>> otherPlayersCurrentList, List<Card> newCommunityCardList) {
        Character ch = checkForFlush(cardList, 3);

        if(!(ch == 'o')) {


            Card newCommunityCard = null;
            int countOfChar = (int) cardList.stream().filter(card -> (card.type == ch)).count();
            Random random = new Random();
            int communityCardCountAdded = 0;

            for (int i = 0; i < countOfChar - 3; i++) {

                MaxFinderUtil.generateRandomCard(allCardsUsedList);

                boolean isCardUsed = false;

                while (!(isCardUsed)) {
                    int num1 = random.nextInt(14);
                    if (ch != 'D') {
                        isCardUsed = MaxFinderUtil.isCardUsed(num1, 'D', allCardsUsedList);
                        if (!isCardUsed) {
                            newCommunityCard = new Card('D', num1);
                        }

                    } else {

                        isCardUsed = MaxFinderUtil.isCardUsed(num1, 'C', allCardsUsedList);
                        if (!isCardUsed) {
                            newCommunityCard = new Card('C', num1);
                        }
                    }
                }

                newCommunityCardList.add(newCommunityCard);

                communityCardCountAdded++;

            }

            while (communityCardCountAdded < 2) {
                Card generatedCard = MaxFinderUtil.generateRandomCard(ch, allCardsUsedList);
                newCommunityCardList.add(generatedCard);
                communityCardCountAdded++;

            }
            WinnerValidator winnerValidator = new WinnerValidator();
            return winnerValidator.isWinnerWon(otherPlayersCurrentList, newCommunityCardList, cardList, 4);
        }
        return false;
    }



        char checkForFlush(List<Card> cardList, int validityBuffer) {

        Iterator<Card> cardIterator = cardList.iterator();
        int cCounter=0,sCounter=0,dCounter=0,hCounter=0;
        while (cardIterator.hasNext()) {
            Card card = cardIterator.next();

            switch (card.type) {

                case 'C':
                    cCounter++;
                    break;
                case 'S':
                    sCounter++;
                    break;

                case 'D':
                    dCounter++;
                    break;

                case 'H':
                    hCounter++;
                    break;


            }
        }
        if(cCounter >=validityBuffer)
            return 'C';
        if(sCounter >=validityBuffer)
            return 'S';
        if(dCounter >=validityBuffer)
            return 'D';
        if(hCounter >=validityBuffer)
            return 'H';

        return 'o';


    }

    boolean checkForStraightFlush(List<Card> cardList, boolean isStraightFlush,List<Card> allCardsUsedList,  List<List<Card>> otherPlayersCurrentList, List<Card> newCommunityCardList) {
        MaxLevelResolver maxLevelResolver = new MaxLevelResolver();
        char color =  maxLevelResolver.checkForFlush(cardList, 3);

        if(!(color == 'o')) {
            List<Card> maxColorCardList = cardList.stream().filter(card -> (card.type == color)).collect(Collectors.toList());
           return maxLevelResolver.checkForStraight(maxColorCardList, isStraightFlush, color, allCardsUsedList,otherPlayersCurrentList,newCommunityCardList);

        }
        return false;
    }

    boolean computeAndCheckStraightCards(List<List<Integer>> possibleLists, boolean isStraightFlush, char color, List<Card> allCardsUsedList,  List<List<Card>> otherPlayersCurrentList,List<Card> cardList, List<Card> newCommunityCardList) {
        List<List<Integer>> valuesToAddList = new ArrayList<List<Integer>>();

        for(List<Integer> possibleList: possibleLists)
        {
            int listSize = possibleList.size();
            int totalGapFound =0;
            int gap =0;
            List<Integer> valuesToAdd= new ArrayList<Integer>();
            if(listSize < 5){

                for (int i = 0; i < listSize-1; i++){
                    gap = possibleList.get(i) - possibleList.get(i+1);
                    if(gap == 2){
                        valuesToAdd.add(possibleList.get(i)-1);
                        totalGapFound += 1;
                    }if(gap == 3){
                        valuesToAdd.add(possibleList.get(i)-1);
                        valuesToAdd.add(possibleList.get(i)-2);
                        totalGapFound += 2;

                    }

                }
                if(totalGapFound == 2)
                    valuesToAddList.add(valuesToAdd);

                if(totalGapFound == 1){
                    int placeHolder = valuesToAdd.get(0);
                    valuesToAdd.add(possibleList.get(0)+1);
                    valuesToAddList.add(valuesToAdd);

                    valuesToAdd.clear();
                    valuesToAdd.add(placeHolder);
                    valuesToAdd.add(possibleList.get(listSize-1)-1);
                    valuesToAddList.add(valuesToAdd);

                }
                if(totalGapFound == 0 && listSize==3){
                    valuesToAdd.clear();
                    valuesToAdd.add(possibleList.get(0)+1);
                    valuesToAdd.add(possibleList.get(0)+2);

                    valuesToAddList.add(valuesToAdd);
                    valuesToAdd.clear();

                    valuesToAdd.add(possibleList.get(listSize-1)-1);
                    valuesToAdd.add(possibleList.get(listSize-1)-2);
                    valuesToAddList.add(valuesToAdd);


                }


                if(totalGapFound == 0 && listSize==4){
                    valuesToAdd.clear();
                    valuesToAdd.add(possibleList.get(0)+1);

                    valuesToAddList.add(valuesToAdd);
                    valuesToAdd.clear();

                    valuesToAdd.add(possibleList.get(listSize-1)-1);
                    valuesToAddList.add(valuesToAdd);

                }

            }

            for(List<Integer> newCommunityCards: valuesToAddList)
            {

                boolean result = checkIsWinnerForEachList(newCommunityCards, possibleList, isStraightFlush, color, allCardsUsedList,  otherPlayersCurrentList,  cardList, newCommunityCardList);
                if(result == true)
                    return true;
            }
        }

        return false;
    }

    private boolean checkIsWinnerForEachList(List<Integer> newCommunityCards, List<Integer> possibleList, boolean isStraightFlush, char color, List<Card> allCardsUsedList,  List<List<Card>> otherPlayersCurrentList, List<Card> cardList, List<Card> newCommunityCardList) {
        WinnerValidator winnerValidator = new WinnerValidator();

        if(newCommunityCards.size()==2){
            if(isStraightFlush){
                boolean isCard1Used = MaxFinderUtil.isCardUsed(newCommunityCards.get(0), color, allCardsUsedList);
                if(!isCard1Used) {

                    boolean isCard2Used = MaxFinderUtil.isCardUsed(newCommunityCards.get(1), color, allCardsUsedList);
                    if (!isCard2Used) {
                        Card newCommunityCard1 = new Card(color, newCommunityCards.get(0));

                        newCommunityCardList.add(newCommunityCard1);

                        Card newCommunityCard2 = new Card(color, newCommunityCards.get(1));

                        newCommunityCardList.add(newCommunityCard2);
                    }
                }
            }else{

                List<Character> colors = Arrays.asList('C', 'S', 'D', 'H');

                boolean isCard1Used = MaxFinderUtil.isCardUsed(newCommunityCards.get(0), 'D', allCardsUsedList);
                if(!isCard1Used) {
                    Card newCommunityCard1 = new Card('D', newCommunityCards.get(0));

                    newCommunityCardList.add(newCommunityCard1);
                }else{
                    Card newCommunityCard1 = new Card('C', newCommunityCards.get(0));

                    newCommunityCardList.add(newCommunityCard1);
                }


                boolean isCard2Used = MaxFinderUtil.isCardUsed(newCommunityCards.get(1), 'C', allCardsUsedList);
                if(!isCard2Used) {
                    Card newCommunityCard2 = new Card('C', newCommunityCards.get(1));

                    newCommunityCardList.add(newCommunityCard2);
                }else{
                    Card newCommunityCard2 = new Card('S', newCommunityCards.get(1));

                    newCommunityCardList.add(newCommunityCard2);
                }
            }

        }else if(newCommunityCards.size()==1){
            if(isStraightFlush){
                boolean isCard1Used = MaxFinderUtil.isCardUsed(newCommunityCards.get(0), color, allCardsUsedList);
                if(!isCard1Used) {

                    boolean isCard2Used = MaxFinderUtil.isCardUsed(13, 'C', allCardsUsedList);
                    if(!isCard2Used) {
                        Card newCommunityCard2 = new Card('C', 13);

                        newCommunityCardList.add(newCommunityCard2);
                    }else{
                        Card newCommunityCard2 = new Card('S', 13);

                        newCommunityCardList.add(newCommunityCard2);
                    }
                }
            }else{
                boolean isCard1Used = MaxFinderUtil.isCardUsed(newCommunityCards.get(0), 'D', allCardsUsedList);
                if(!isCard1Used) {
                    Card newCommunityCard1 = new Card('D', newCommunityCards.get(0));

                    newCommunityCardList.add(newCommunityCard1);
                }else{
                    Card newCommunityCard1 = new Card('C', newCommunityCards.get(0));

                    newCommunityCardList.add(newCommunityCard1);
                }


                boolean isCard2Used = MaxFinderUtil.isCardUsed(13, 'C', allCardsUsedList);
                if(!isCard2Used) {
                    Card newCommunityCard2 = new Card('C', 13);

                    newCommunityCardList.add(newCommunityCard2);
                }else{
                    Card newCommunityCard2 = new Card('S', 13);

                    newCommunityCardList.add(newCommunityCard2);
                }
            }

        }else if(newCommunityCards.size()==0){
            boolean isCard1Used = MaxFinderUtil.isCardUsed(12, 'C', allCardsUsedList);
            if(!isCard1Used) {
                Card newCommunityCard1 = new Card('C',12);

                newCommunityCardList.add(newCommunityCard1);
            }else{
                Card newCommunityCard2 = new Card('S', 12);

                newCommunityCardList.add(newCommunityCard2);
            }
            boolean isCard2Used = MaxFinderUtil.isCardUsed(13, 'C', allCardsUsedList);
            if(!isCard2Used) {
                Card newCommunityCard2 = new Card('C', 13);

                newCommunityCardList.add(newCommunityCard2);
            }else{
                Card newCommunityCard2 = new Card('S', 13);

                newCommunityCardList.add(newCommunityCard2);
            }
        }
      if(newCommunityCardList.size() ==2) {
          if (isStraightFlush == true)
              return winnerValidator.isWinnerWon(otherPlayersCurrentList, newCommunityCardList, cardList, 1);
          else
              return winnerValidator.isWinnerWon(otherPlayersCurrentList, newCommunityCardList, cardList, 5);
      }return false;
    }

}
