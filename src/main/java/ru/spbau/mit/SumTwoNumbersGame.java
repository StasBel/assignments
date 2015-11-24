package ru.spbau.mit;

import java.util.*;


public class SumTwoNumbersGame implements Game {

    private GameServer server;
    private int number1, number2;
    private Random random = new Random();

    private void generateNumbers() {
        number1 = Math.abs(random.nextInt());
        number2 = Math.abs(random.nextInt());
    }

    private String numbersToString(){
        return Integer.toString(number1) + " " + Integer.toString(number2);
    }

    public SumTwoNumbersGame(GameServer server) {
        this.server = server;
        generateNumbers();
    }

    @Override
    public void onPlayerConnected(String id) {
        server.sendTo(id, numbersToString());
    }

    @Override
    public void onPlayerSentMsg(String id, String msg) {
        try {
            if (Integer.parseInt(msg) == number1 + number2){
                server.sendTo(id, "Right");
                server.broadcast(id + " won");
                generateNumbers();
                server.broadcast(numbersToString());
            } else {
                server.sendTo(id, "Wrong");
            }
        } catch (NumberFormatException e) {
            //...
        }
    }
}
