package com.texas.poker.pojo;

public class Card {

    public Card(String cardStr){
      type = cardStr.charAt(0);
      denomination = cardStr.substring(1);
      value = findCardValue(denomination);
    }
    public Card(char typePassed,int valuePasssed ){
        type = typePassed;
        denomination = findCardDenomination(valuePasssed);
        value = valuePasssed;
    }



    public char type;
    public String denomination;
    public int value;

    int findCardValue(String denomination){
        switch (denomination){

            case "j":
                return 11;
            case "q":
                return 12;
            case "k":
                return 13;
            case "a":
                return 14;
            default:
                return Integer.parseInt(denomination);

        }

    }

    String findCardDenomination(int value){
        switch (value){

            case 11:
                return "j";
            case 12:
                return "q";
            case 13:
                return "k";
            case 14:
                return "a";
            default:
                return String.valueOf(value);

        }

    }

}
